package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.*
import com.example.ui.*
import com.example.ui.components.AppHeader
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: FantasyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SKCotrageTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: FantasyViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val wallet by viewModel.wallet.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val joinedContests by viewModel.joinedContests.collectAsStateWithLifecycle()
    val allUserTeams by viewModel.allUserTeams.collectAsStateWithLifecycle()
    val matchFilter by viewModel.matchFilter.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.matchContestCategory.collectAsStateWithLifecycle()
    val selectedPlayerIds by viewModel.selectedPlayerIds.collectAsStateWithLifecycle()
    val captainId by viewModel.selectedCaptainId.collectAsStateWithLifecycle()
    val viceCaptainId by viewModel.selectedViceCaptainId.collectAsStateWithLifecycle()
    val currentRole by viewModel.roleFilter.collectAsStateWithLifecycle()
    val lastEventMessage by viewModel.liveMatchEventMessage.collectAsStateWithLifecycle()
    val simulatedBonusPoints by viewModel.simulatedUserBonusPoints.collectAsStateWithLifecycle()
    val snackBarMessage by viewModel.snackBarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackBar()
        }
    }

    // Determine bottom nav active index
    val bottomNavIndex = when (currentScreen) {
        is Screen.Matches, is Screen.MatchDetail, is Screen.CreateTeam,
        is Screen.CaptainSelection, is Screen.PitchPreview, is Screen.ContestLeaderboard -> 0
        is Screen.MyMatches -> 1
        is Screen.GuruInsights -> 2
        is Screen.Wallet -> 3
        is Screen.PointsSystem -> 0
    }

    val showBottomNav = currentScreen is Screen.Matches ||
            currentScreen is Screen.MyMatches ||
            currentScreen is Screen.GuruInsights ||
            currentScreen is Screen.Wallet

    // System BackHandler
    BackHandler(enabled = currentScreen !is Screen.Matches) {
        when (currentScreen) {
            is Screen.CaptainSelection -> {
                val match = (currentScreen as Screen.CaptainSelection).match
                viewModel.navigateTo(Screen.CreateTeam(match))
            }
            is Screen.PitchPreview -> {
                val match = (currentScreen as Screen.PitchPreview).match
                viewModel.navigateTo(Screen.CreateTeam(match))
            }
            is Screen.CreateTeam -> {
                val match = (currentScreen as Screen.CreateTeam).match
                viewModel.navigateTo(Screen.MatchDetail(match))
            }
            is Screen.MatchDetail -> {
                viewModel.navigateTo(Screen.Matches)
            }
            is Screen.ContestLeaderboard -> {
                val match = (currentScreen as Screen.ContestLeaderboard).match
                viewModel.navigateTo(Screen.MatchDetail(match))
            }
            is Screen.PointsSystem -> {
                viewModel.navigateTo(Screen.Matches)
            }
            is Screen.MyMatches, is Screen.GuruInsights, is Screen.Wallet -> {
                viewModel.navigateTo(Screen.Matches)
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkNavyBg,
        topBar = {
            if (showBottomNav) {
                AppHeader(
                    wallet = wallet,
                    onWalletClick = { viewModel.navigateTo(Screen.Wallet) }
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    containerColor = DarkSurface,
                    tonalElevation = 6.dp,
                    modifier = Modifier.navigationBarsPadding().testTag("main_bottom_nav")
                ) {
                    NavigationBarItem(
                        selected = bottomNavIndex == 0,
                        onClick = { viewModel.navigateTo(Screen.Matches) },
                        icon = {
                            Icon(
                                imageVector = if (bottomNavIndex == 0) Icons.Filled.SportsCricket else Icons.Outlined.SportsCricket,
                                contentDescription = "Matches"
                            )
                        },
                        label = { Text("Matches", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryRed,
                            selectedTextColor = PrimaryRed,
                            indicatorColor = PrimaryRed.copy(alpha = 0.15f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = bottomNavIndex == 1,
                        onClick = { viewModel.navigateTo(Screen.MyMatches) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (joinedContests.isNotEmpty()) {
                                        Badge(containerColor = PrimaryRed) {
                                            Text("${joinedContests.size}")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (bottomNavIndex == 1) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents,
                                    contentDescription = "My Matches"
                                )
                            }
                        },
                        label = { Text("My Matches", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryRed,
                            selectedTextColor = PrimaryRed,
                            indicatorColor = PrimaryRed.copy(alpha = 0.15f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = bottomNavIndex == 2,
                        onClick = { viewModel.navigateTo(Screen.GuruInsights) },
                        icon = {
                            Icon(
                                imageVector = if (bottomNavIndex == 2) Icons.Filled.Psychology else Icons.Outlined.Psychology,
                                contentDescription = "Guru AI"
                            )
                        },
                        label = { Text("Guru", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SkyAccent,
                            selectedTextColor = SkyAccent,
                            indicatorColor = SkyAccent.copy(alpha = 0.15f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = bottomNavIndex == 3,
                        onClick = { viewModel.navigateTo(Screen.Wallet) },
                        icon = {
                            Icon(
                                imageVector = if (bottomNavIndex == 3) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                                contentDescription = "Wallet"
                            )
                        },
                        label = { Text("Wallet", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TrophyGold,
                            selectedTextColor = TrophyGold,
                            indicatorColor = TrophyGold.copy(alpha = 0.15f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = DarkSurfaceElevated,
                        contentColor = TextPrimary,
                        actionColor = TrophyGold
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val screen = currentScreen) {
                is Screen.Matches -> {
                    MatchesScreen(
                        matches = viewModel.matches,
                        selectedStatus = matchFilter,
                        onStatusSelected = { viewModel.setMatchFilter(it) },
                        onMatchClick = { match -> viewModel.selectMatch(match) },
                        onGuruClick = { viewModel.navigateTo(Screen.GuruInsights) },
                        onRulesClick = { viewModel.navigateTo(Screen.PointsSystem) },
                        onSimulateLiveBall = { match -> viewModel.simulateLiveBall(match) }
                    )
                }

                is Screen.MatchDetail -> {
                    val match = screen.match
                    val matchContests = viewModel.getContestsForMatch(match.id)
                    val matchUserTeams = allUserTeams.filter { it.matchId == match.id }
                    val matchJoinedContests = joinedContests.filter { it.matchId == match.id }
                    val guruInsight = viewModel.getGuruInsight(match.id)

                    MatchDetailScreen(
                        match = match,
                        contests = matchContests,
                        selectedCategory = selectedCategory,
                        userTeams = matchUserTeams,
                        joinedContests = matchJoinedContests,
                        guruInsight = guruInsight,
                        onBack = { viewModel.navigateTo(Screen.Matches) },
                        onCategorySelected = { viewModel.setContestCategory(it) },
                        onCreateTeamClick = { viewModel.startCreateTeam(match) },
                        onJoinContest = { contest, team -> viewModel.joinContest(match, contest, team) },
                        onLeaderboardClick = { contest ->
                            val userPoints = matchUserTeams.firstOrNull()?.totalPoints ?: 380
                            viewModel.navigateTo(Screen.ContestLeaderboard(match, contest, userPoints))
                        },
                        onSimulateLiveClick = { viewModel.simulateLiveBall(match) },
                        onTeamPitchClick = { team ->
                            val allMatchPlayers = viewModel.getMatchPlayers(match.id)
                            val teamPlayers = team.playerIds.mapNotNull { pid -> allMatchPlayers.find { it.id == pid } }
                            viewModel.navigateTo(Screen.PitchPreview(match, teamPlayers, team.captainId, team.viceCaptainId))
                        }
                    )
                }

                is Screen.CreateTeam -> {
                    val match = screen.match
                    val allPlayers = viewModel.getMatchPlayers(match.id)
                    val (canProceed, errorReason) = viewModel.canProceedToCaptainSelection(match)

                    CreateTeamScreen(
                        match = match,
                        allPlayers = allPlayers,
                        selectedPlayerIds = selectedPlayerIds,
                        currentRole = currentRole,
                        onRoleSelected = { viewModel.setRoleFilter(it) },
                        onTogglePlayer = { viewModel.togglePlayerSelection(it, match) },
                        onBack = { viewModel.navigateTo(Screen.MatchDetail(match)) },
                        onPreviewPitch = {
                            val selectedPlayersList = allPlayers.filter { selectedPlayerIds.contains(it.id) }
                            viewModel.navigateTo(
                                Screen.PitchPreview(
                                    match = match,
                                    players = selectedPlayersList,
                                    captainId = captainId ?: "",
                                    viceCaptainId = viceCaptainId ?: ""
                                )
                            )
                        },
                        onNext = {
                            if (canProceed) {
                                val selectedPlayersList = allPlayers.filter { selectedPlayerIds.contains(it.id) }
                                viewModel.navigateTo(Screen.CaptainSelection(match, selectedPlayersList))
                            }
                        },
                        validationError = if (!canProceed && selectedPlayerIds.size == 11) errorReason else null
                    )
                }

                is Screen.CaptainSelection -> {
                    val match = screen.match
                    val selectedPlayers = screen.selectedPlayers

                    CaptainSelectionScreen(
                        match = match,
                        players = selectedPlayers,
                        captainId = captainId,
                        viceCaptainId = viceCaptainId,
                        onSelectCaptain = { viewModel.setCaptain(it) },
                        onSelectViceCaptain = { viewModel.setViceCaptain(it) },
                        onBack = { viewModel.navigateTo(Screen.CreateTeam(match)) },
                        onPreviewPitch = {
                            viewModel.navigateTo(
                                Screen.PitchPreview(
                                    match = match,
                                    players = selectedPlayers,
                                    captainId = captainId ?: "",
                                    viceCaptainId = viceCaptainId ?: ""
                                )
                            )
                        },
                        onSaveTeam = {
                            viewModel.saveTeam(match) {
                                viewModel.navigateTo(Screen.MatchDetail(match))
                            }
                        }
                    )
                }

                is Screen.PitchPreview -> {
                    PitchPreviewScreen(
                        match = screen.match,
                        players = screen.players,
                        captainId = screen.captainId,
                        viceCaptainId = screen.viceCaptainId,
                        onBack = {
                            // Return to previous screen
                            if (screen.players.size == 11 && screen.captainId.isNotBlank()) {
                                viewModel.navigateTo(Screen.MatchDetail(screen.match))
                            } else {
                                viewModel.navigateTo(Screen.CreateTeam(screen.match))
                            }
                        }
                    )
                }

                is Screen.MyMatches -> {
                    MyMatchesScreen(
                        joinedContests = joinedContests,
                        matches = viewModel.matches,
                        simulatedBonusPoints = simulatedBonusPoints,
                        lastEventMessage = lastEventMessage,
                        onSimulateLiveBall = { match -> viewModel.simulateLiveBall(match) },
                        onViewLeaderboard = { match, contest ->
                            viewModel.navigateTo(Screen.ContestLeaderboard(match, contest, 390 + simulatedBonusPoints))
                        },
                        onExploreMatches = { viewModel.navigateTo(Screen.Matches) }
                    )
                }

                is Screen.ContestLeaderboard -> {
                    val leaderboardItems = viewModel.getLeaderboard(screen.contest.title)
                    LeaderboardScreen(
                        match = screen.match,
                        contest = screen.contest,
                        items = leaderboardItems,
                        onBack = { viewModel.navigateTo(Screen.MatchDetail(screen.match)) }
                    )
                }

                is Screen.GuruInsights -> {
                    val defaultMatch = viewModel.matches.first()
                    var selectedGuruMatch by remember { mutableStateOf(defaultMatch) }
                    val insight = viewModel.getGuruInsight(selectedGuruMatch.id)

                    GuruScreen(
                        matches = viewModel.matches,
                        selectedMatch = selectedGuruMatch,
                        insight = insight,
                        onSelectMatch = { selectedGuruMatch = it },
                        onCreateTeam = { match -> viewModel.startCreateTeam(match) },
                        onBack = { viewModel.navigateTo(Screen.Matches) }
                    )
                }

                is Screen.Wallet -> {
                    WalletScreen(
                        wallet = wallet,
                        transactions = transactions,
                        onAddCash = { viewModel.addCash(it) },
                        onWithdrawCash = { viewModel.withdrawCash(it) },
                        onBack = { viewModel.navigateTo(Screen.Matches) }
                    )
                }

                is Screen.PointsSystem -> {
                    PointsSystemScreen(
                        onBack = { viewModel.navigateTo(Screen.Matches) }
                    )
                }
            }
        }
    }
}
