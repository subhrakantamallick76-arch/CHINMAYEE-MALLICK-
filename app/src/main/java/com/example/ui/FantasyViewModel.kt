package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FantasyRepository
import com.example.data.local.SKCotrageDatabase
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Matches : Screen()
    data class MatchDetail(val match: Match) : Screen()
    data class CreateTeam(val match: Match, val existingTeam: UserTeam? = null) : Screen()
    data class CaptainSelection(val match: Match, val selectedPlayers: List<Player>) : Screen()
    data class PitchPreview(val match: Match, val players: List<Player>, val captainId: String, val viceCaptainId: String) : Screen()
    data class ContestLeaderboard(val match: Match, val contest: Contest, val userPoints: Int) : Screen()
    object MyMatches : Screen()
    object GuruInsights : Screen()
    object PointsSystem : Screen()
    object Wallet : Screen()
}

class FantasyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FantasyRepository
    init {
        val db = SKCotrageDatabase.getDatabase(application)
        repository = FantasyRepository(db.fantasyDao())
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Matches)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Matches
    val matches = repository.matches
    private val _matchFilter = MutableStateFlow(MatchStatus.UPCOMING)
    val matchFilter: StateFlow<MatchStatus> = _matchFilter.asStateFlow()

    // Selected Match & Contests
    private val _selectedMatch = MutableStateFlow<Match?>(repository.matches.firstOrNull())
    val selectedMatch: StateFlow<Match?> = _selectedMatch.asStateFlow()

    private val _matchContestCategory = MutableStateFlow(ContestCategory.ALL)
    val matchContestCategory: StateFlow<ContestCategory> = _matchContestCategory.asStateFlow()

    // Team Creation State
    private val _selectedPlayerIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedPlayerIds: StateFlow<Set<String>> = _selectedPlayerIds.asStateFlow()

    private val _selectedCaptainId = MutableStateFlow<String?>(null)
    val selectedCaptainId: StateFlow<String?> = _selectedCaptainId.asStateFlow()

    private val _selectedViceCaptainId = MutableStateFlow<String?>(null)
    val selectedViceCaptainId: StateFlow<String?> = _selectedViceCaptainId.asStateFlow()

    private val _roleFilter = MutableStateFlow(PlayerRole.WK)
    val roleFilter: StateFlow<PlayerRole> = _roleFilter.asStateFlow()

    // User Data Flows
    val wallet: StateFlow<Wallet> = repository.getWallet()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Wallet(200.0, 150.0, 150.0))

    val transactions: StateFlow<List<TransactionRecord>> = repository.getTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val joinedContests: StateFlow<List<JoinedContest>> = repository.getJoinedContests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUserTeams: StateFlow<List<UserTeam>> = repository.getAllTeams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Live Match Simulation State
    private val _liveMatchEventMessage = MutableStateFlow<String?>(null)
    val liveMatchEventMessage: StateFlow<String?> = _liveMatchEventMessage.asStateFlow()

    private val _simulatedUserBonusPoints = MutableStateFlow(0)
    val simulatedUserBonusPoints: StateFlow<Int> = _simulatedUserBonusPoints.asStateFlow()

    // Notifications / SnackBar message
    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setMatchFilter(status: MatchStatus) {
        _matchFilter.value = status
    }

    fun setContestCategory(category: ContestCategory) {
        _matchContestCategory.value = category
    }

    fun setRoleFilter(role: PlayerRole) {
        _roleFilter.value = role
    }

    fun selectMatch(match: Match) {
        _selectedMatch.value = match
        _currentScreen.value = Screen.MatchDetail(match)
    }

    fun startCreateTeam(match: Match) {
        _selectedPlayerIds.value = emptySet()
        _selectedCaptainId.value = null
        _selectedViceCaptainId.value = null
        _roleFilter.value = PlayerRole.WK
        _currentScreen.value = Screen.CreateTeam(match)
    }

    fun togglePlayerSelection(player: Player, match: Match) {
        val currentSet = _selectedPlayerIds.value.toMutableSet()
        val allMatchPlayers = repository.matchPlayers[match.id] ?: emptyList()

        if (currentSet.contains(player.id)) {
            currentSet.remove(player.id)
            if (_selectedCaptainId.value == player.id) _selectedCaptainId.value = null
            if (_selectedViceCaptainId.value == player.id) _selectedViceCaptainId.value = null
            _selectedPlayerIds.value = currentSet
        } else {
            // Validation: Max 11 players
            if (currentSet.size >= 11) {
                _snackBarMessage.value = "You can only select exactly 11 players!"
                return
            }

            // Validation: Max 7 players from one team
            val teamCount = currentSet.count { pid ->
                allMatchPlayers.find { it.id == pid }?.teamCode == player.teamCode
            }
            if (teamCount >= 7) {
                _snackBarMessage.value = "Maximum 7 players allowed from ${player.teamCode}!"
                return
            }

            // Validation: Credits check
            val currentCreditsUsed = currentSet.sumOf { pid ->
                allMatchPlayers.find { it.id == pid }?.credits ?: 0.0
            }
            if (currentCreditsUsed + player.credits > 100.0) {
                _snackBarMessage.value = "Not enough credits! 100 credit limit exceeded."
                return
            }

            // Role constraints
            val selectedByRole = currentSet.mapNotNull { pid -> allMatchPlayers.find { it.id == pid } }
                .groupBy { it.role }

            val countInThisRole = selectedByRole[player.role]?.size ?: 0
            if (countInThisRole >= player.role.maxCount) {
                _snackBarMessage.value = "Maximum ${player.role.maxCount} allowed for ${player.role.displayName}!"
                return
            }

            currentSet.add(player.id)
            _selectedPlayerIds.value = currentSet
        }
    }

    fun canProceedToCaptainSelection(match: Match): Pair<Boolean, String?> {
        val selected = _selectedPlayerIds.value
        val allMatchPlayers = repository.matchPlayers[match.id] ?: emptyList()

        if (selected.size != 11) {
            return false to "Please select exactly 11 players (Currently ${selected.size}/11)"
        }

        val selectedPlayers = selected.mapNotNull { pid -> allMatchPlayers.find { it.id == pid } }
        val wkCount = selectedPlayers.count { it.role == PlayerRole.WK }
        val batCount = selectedPlayers.count { it.role == PlayerRole.BAT }
        val arCount = selectedPlayers.count { it.role == PlayerRole.AR }
        val bowlCount = selectedPlayers.count { it.role == PlayerRole.BOWL }

        if (wkCount < 1) return false to "Must select at least 1 Wicket Keeper (WK)"
        if (batCount < 3) return false to "Must select at least 3 Batters (BAT)"
        if (arCount < 1) return false to "Must select at least 1 All-Rounder (AR)"
        if (bowlCount < 3) return false to "Must select at least 3 Bowlers (BOWL)"

        return true to null
    }

    fun setCaptain(playerId: String) {
        if (_selectedViceCaptainId.value == playerId) {
            _selectedViceCaptainId.value = null
        }
        _selectedCaptainId.value = playerId
    }

    fun setViceCaptain(playerId: String) {
        if (_selectedCaptainId.value == playerId) {
            _selectedCaptainId.value = null
        }
        _selectedViceCaptainId.value = playerId
    }

    fun saveTeam(match: Match, onSaved: (String) -> Unit) {
        val cap = _selectedCaptainId.value
        val vc = _selectedViceCaptainId.value
        if (cap == null || vc == null) {
            _snackBarMessage.value = "Please select both Captain (2x) and Vice-Captain (1.5x)!"
            return
        }

        viewModelScope.launch {
            val teamNumber = (allUserTeams.value.filter { it.matchId == match.id }.size) + 1
            val teamName = "SK Cotrage Team $teamNumber"
            val teamId = repository.saveTeam(
                matchId = match.id,
                teamName = teamName,
                captainId = cap,
                viceCaptainId = vc,
                playerIds = _selectedPlayerIds.value.toList()
            )
            _snackBarMessage.value = "Team '$teamName' created successfully! 🏏"
            onSaved(teamId)
        }
    }

    fun joinContest(match: Match, contest: Contest, team: UserTeam) {
        viewModelScope.launch {
            val userWallet = wallet.value
            if (contest.entryFee > userWallet.totalBalance) {
                _snackBarMessage.value = "Insufficient balance! Please add cash to join."
                return@launch
            }
            repository.joinContest(
                matchId = match.id,
                contest = contest,
                teamId = team.id,
                teamName = team.teamName
            )
            _snackBarMessage.value = "🎉 Contest Joined successfully with ${team.teamName}!"
        }
    }

    fun addCash(amount: Double) {
        viewModelScope.launch {
            repository.addCash(amount)
            _snackBarMessage.value = "₹$amount successfully added to your SK Cotrage wallet! 💰"
        }
    }

    fun withdrawCash(amount: Double) {
        viewModelScope.launch {
            val w = wallet.value
            if (amount > w.winningBalance) {
                _snackBarMessage.value = "You can only withdraw up to your Winnings balance (₹${w.winningBalance})"
                return@launch
            }
            repository.withdrawWinnings(amount)
            _snackBarMessage.value = "₹$amount withdrawal initiated to your UPI account! ⚡"
        }
    }

    // Live Match Interactive Event Simulation
    fun simulateLiveBall(match: Match) {
        val events = listOf(
            "🔥 Virat Kohli drives down the ground for FOUR! (+5 pts)",
            "🚀 Suryakumar Yadav scoops over fine leg for a HUGE SIX! (+8 pts)",
            "🎯 Jasprit Bumrah knocks out middle stump with an unplayable yorker! (+25 pts)",
            "🧤 Rishabh Pant takes a flying diving catch behind the stumps! (+12 pts)",
            "⚡ Hardik Pandya bowls a tight maiden over! (+12 pts)",
            "🏏 Travis Head pulls powerfully through midwicket for FOUR! (+5 pts)",
            "🎯 Pat Cummins claims an LBW review success! (+25 pts)"
        )
        val event = events.random()
        _liveMatchEventMessage.value = event
        val bonus = (10..35).random()
        _simulatedUserBonusPoints.value += bonus
        _snackBarMessage.value = "$event (+${bonus} pts to your Fantasy Team!)"
    }

    fun clearSnackBar() {
        _snackBarMessage.value = null
    }

    fun getContestsForMatch(matchId: String): List<Contest> {
        val list = repository.getContestsForMatch(matchId)
        val filter = _matchContestCategory.value
        return if (filter == ContestCategory.ALL) list else list.filter { it.category == filter }
    }

    fun getMatchPlayers(matchId: String): List<Player> {
        return repository.matchPlayers[matchId] ?: emptyList()
    }

    fun getGuruInsight(matchId: String): GuruInsight {
        return repository.getGuruInsight(matchId)
    }

    fun getLeaderboard(contestTitle: String): List<LeaderboardItem> {
        val currentPoints = 390 + _simulatedUserBonusPoints.value
        return repository.getLeaderboard(contestTitle, currentPoints)
    }
}
