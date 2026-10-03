package com.example.data

import com.example.data.local.*
import com.example.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class FantasyRepository(private val dao: FantasyDao) {

    // Initial Matches
    val matches: List<Match> = listOf(
        Match(
            id = "m1",
            team1Code = "IND",
            team1Name = "India",
            team1Color = 0xFF1E40AF,
            team2Code = "AUS",
            team2Name = "Australia",
            team2Color = 0xFFEAB308,
            seriesName = "T20 Championship Super 8",
            status = MatchStatus.LIVE,
            timeString = "LIVE - 14.2 Overs",
            venue = "Kensington Oval, Bridgetown",
            pitchReport = "Balanced pitch with true bounce. Spinners get good grip in 2nd innings.",
            megaPrizePoolText = "₹1.5 Crores",
            firstPrizeText = "₹15 Lakhs",
            entryFeeStarting = "₹49",
            team1Score = "IND 138/3 (14.2)",
            team2Score = "Yet to bat",
            liveCommentary = "Suryakumar Yadav smashes a magnificent six over deep fine leg! Current Run Rate: 9.62"
        ),
        Match(
            id = "m2",
            team1Code = "MI",
            team1Name = "Mumbai",
            team1Color = 0xFF0284C7,
            team2Code = "CSK",
            team2Name = "Chennai",
            team2Color = 0xFFFACC15,
            seriesName = "Indian T20 League 2026",
            status = MatchStatus.UPCOMING,
            timeString = "Today, 7:30 PM",
            venue = "Wankhede Stadium, Mumbai",
            pitchReport = "Red soil deck, high scoring batting paradise with short boundaries. Dew factor in 2nd half.",
            megaPrizePoolText = "₹2.0 Crores",
            firstPrizeText = "₹25 Lakhs",
            entryFeeStarting = "₹39"
        ),
        Match(
            id = "m3",
            team1Code = "RCB",
            team1Name = "Bangalore",
            team1Color = 0xFFDC2626,
            team2Code = "KKR",
            team2Name = "Kolkata",
            team2Color = 0xFF6D28D9,
            seriesName = "Indian T20 League 2026",
            status = MatchStatus.UPCOMING,
            timeString = "Tomorrow, 3:30 PM",
            venue = "M. Chinnaswamy Stadium, Bengaluru",
            pitchReport = "Flat batting wicket with good pace, expects 200+ first innings total.",
            megaPrizePoolText = "₹1.0 Crore",
            firstPrizeText = "₹10 Lakhs",
            entryFeeStarting = "₹29"
        ),
        Match(
            id = "m4",
            team1Code = "ENG",
            team1Name = "England",
            team1Color = 0xFF0369A1,
            team2Code = "SA",
            team2Name = "South Africa",
            team2Color = 0xFF15803D,
            seriesName = "T20 World Series",
            status = MatchStatus.UPCOMING,
            timeString = "Tomorrow, 7:30 PM",
            venue = "Lord's, London",
            pitchReport = "Green tinge on day 1 offering early seam swing to fast bowlers.",
            megaPrizePoolText = "₹75 Lakhs",
            firstPrizeText = "₹8 Lakhs",
            entryFeeStarting = "₹19"
        ),
        Match(
            id = "m5",
            team1Code = "IND",
            team1Name = "India",
            team1Color = 0xFF1E40AF,
            team2Code = "ENG",
            team2Name = "England",
            team2Color = 0xFF0369A1,
            seriesName = "T20 Semi Final",
            status = MatchStatus.COMPLETED,
            timeString = "Yesterday",
            venue = "Guyana National Stadium",
            pitchReport = "Dry spin friendly wicket.",
            megaPrizePoolText = "₹1.2 Crores",
            firstPrizeText = "₹12 Lakhs",
            entryFeeStarting = "₹49",
            team1Score = "IND 171/7 (20)",
            team2Score = "ENG 103/10 (16.4)",
            liveCommentary = "India won by 68 runs. Axar Patel named Player of the Match!"
        )
    )

    // Match Players Map (22 players per match)
    val matchPlayers: Map<String, List<Player>> = mapOf(
        "m1" to listOf(
            // Wicket Keepers (WK)
            Player("p101", "Rishabh Pant", "IND", PlayerRole.WK, 9.0, 68, 86.4f, 17, runs = 36, catches = 1),
            Player("p102", "Josh Inglis", "AUS", PlayerRole.WK, 8.5, 42, 62.1f, 48),
            Player("p103", "Sanju Samson", "IND", PlayerRole.WK, 8.0, 31, 38.5f, 9),

            // Batters (BAT)
            Player("p104", "Virat Kohli", "IND", PlayerRole.BAT, 9.5, 92, 91.2f, 18, runs = 48),
            Player("p105", "Travis Head", "AUS", PlayerRole.BAT, 9.5, 88, 89.8f, 62),
            Player("p106", "Suryakumar Yadav", "IND", PlayerRole.BAT, 9.0, 78, 84.0f, 63, runs = 41),
            Player("p107", "Rohit Sharma", "IND", PlayerRole.BAT, 9.0, 74, 82.5f, 45, runs = 23),
            Player("p108", "Mitchell Marsh", "AUS", PlayerRole.BAT, 8.5, 54, 69.3f, 8),
            Player("p109", "David Warner", "AUS", PlayerRole.BAT, 8.5, 49, 58.7f, 31),
            Player("p110", "Yashasvi Jaiswal", "IND", PlayerRole.BAT, 8.0, 35, 41.2f, 64),
            Player("p111", "Tim David", "AUS", PlayerRole.BAT, 8.0, 38, 44.5f, 85),

            // All-Rounders (AR)
            Player("p112", "Hardik Pandya", "IND", PlayerRole.AR, 9.0, 85, 87.6f, 33, runs = 24, wickets = 1),
            Player("p113", "Marcus Stoinis", "AUS", PlayerRole.AR, 9.0, 76, 78.4f, 17, wickets = 1),
            Player("p114", "Glenn Maxwell", "AUS", PlayerRole.AR, 8.5, 62, 71.9f, 32, oversBowled = 2.0f),
            Player("p115", "Axar Patel", "IND", PlayerRole.AR, 8.5, 67, 75.3f, 20, oversBowled = 3.0f, wickets = 1),
            Player("p116", "Ravindra Jadeja", "IND", PlayerRole.AR, 8.0, 48, 55.0f, 8),

            // Bowlers (BOWL)
            Player("p117", "Jasprit Bumrah", "IND", PlayerRole.BOWL, 9.5, 96, 94.8f, 93, oversBowled = 3.0f, wickets = 2),
            Player("p118", "Pat Cummins", "AUS", PlayerRole.BOWL, 9.0, 71, 79.5f, 30, oversBowled = 3.0f, wickets = 1),
            Player("p119", "Mitchell Starc", "AUS", PlayerRole.BOWL, 9.0, 68, 77.2f, 56, oversBowled = 3.0f),
            Player("p120", "Kuldeep Yadav", "IND", PlayerRole.BOWL, 8.5, 64, 73.1f, 23, oversBowled = 2.0f, wickets = 1),
            Player("p121", "Adam Zampa", "AUS", PlayerRole.BOWL, 8.5, 60, 68.4f, 88, oversBowled = 3.0f, wickets = 1),
            Player("p122", "Arshdeep Singh", "IND", PlayerRole.BOWL, 8.5, 72, 76.5f, 2, oversBowled = 2.0f, wickets = 1)
        ),
        "m2" to listOf(
            // MI vs CSK
            Player("p201", "MS Dhoni", "CSK", PlayerRole.WK, 8.5, 52, 78.2f, 7),
            Player("p202", "Ishan Kishan", "MI", PlayerRole.WK, 8.5, 61, 72.4f, 23),
            Player("p203", "Rohit Sharma", "MI", PlayerRole.BAT, 9.5, 84, 91.5f, 45),
            Player("p204", "Ruturaj Gaikwad", "CSK", PlayerRole.BAT, 9.5, 89, 93.1f, 31),
            Player("p205", "Suryakumar Yadav", "MI", PlayerRole.BAT, 9.0, 88, 89.4f, 63),
            Player("p206", "Shivam Dube", "CSK", PlayerRole.BAT, 9.0, 74, 80.2f, 25),
            Player("p207", "Tilak Varma", "MI", PlayerRole.BAT, 8.5, 66, 70.8f, 9),
            Player("p208", "Ajinkya Rahane", "CSK", PlayerRole.BAT, 8.0, 48, 52.3f, 21),
            Player("p209", "Hardik Pandya", "MI", PlayerRole.AR, 9.0, 79, 85.0f, 33),
            Player("p210", "Ravindra Jadeja", "CSK", PlayerRole.AR, 9.0, 81, 86.7f, 8),
            Player("p211", "Moeen Ali", "CSK", PlayerRole.AR, 8.5, 58, 64.1f, 18),
            Player("p212", "Rachin Ravindra", "CSK", PlayerRole.AR, 8.5, 63, 69.5f, 5),
            Player("p213", "Tim David", "MI", PlayerRole.AR, 8.0, 49, 56.4f, 85),
            Player("p214", "Jasprit Bumrah", "MI", PlayerRole.BOWL, 9.5, 95, 96.0f, 93),
            Player("p215", "Matheesha Pathirana", "CSK", PlayerRole.BOWL, 9.0, 82, 84.5f, 99),
            Player("p216", "Gerald Coetzee", "MI", PlayerRole.BOWL, 8.5, 68, 71.2f, 62),
            Player("p217", "Deepak Chahar", "CSK", PlayerRole.BOWL, 8.5, 60, 65.3f, 90),
            Player("p218", "Piyush Chawla", "MI", PlayerRole.BOWL, 8.0, 52, 58.7f, 11),
            Player("p219", "Tushar Deshpande", "CSK", PlayerRole.BOWL, 8.0, 56, 61.4f, 24),
            Player("p220", "Nuwan Thushara", "MI", PlayerRole.BOWL, 8.0, 47, 50.1f, 5),
            Player("p221", "Nehal Wadhera", "MI", PlayerRole.BAT, 7.5, 38, 41.2f, 28),
            Player("p222", "Mukesh Choudhary", "CSK", PlayerRole.BOWL, 7.5, 34, 38.6f, 46)
        ),
        "m3" to listOf(
            // RCB vs KKR
            Player("p301", "Dinesh Karthik", "RCB", PlayerRole.WK, 8.5, 59, 73.2f, 19),
            Player("p302", "Phil Salt", "KKR", PlayerRole.WK, 9.0, 77, 82.5f, 28),
            Player("p303", "Virat Kohli", "RCB", PlayerRole.BAT, 9.5, 94, 95.8f, 18),
            Player("p304", "Shreyas Iyer", "KKR", PlayerRole.BAT, 9.0, 75, 80.1f, 41),
            Player("p305", "Faf du Plessis", "RCB", PlayerRole.BAT, 9.0, 78, 83.4f, 13),
            Player("p306", "Rinku Singh", "KKR", PlayerRole.BAT, 8.5, 65, 71.0f, 35),
            Player("p307", "Rajat Patidar", "RCB", PlayerRole.BAT, 8.5, 69, 74.2f, 87),
            Player("p308", "Venkatesh Iyer", "KKR", PlayerRole.BAT, 8.5, 67, 72.0f, 25),
            Player("p309", "Andre Russell", "KKR", PlayerRole.AR, 9.5, 91, 92.4f, 12),
            Player("p310", "Glenn Maxwell", "RCB", PlayerRole.AR, 8.5, 60, 68.3f, 32),
            Player("p311", "Sunil Narine", "KKR", PlayerRole.AR, 9.5, 93, 94.0f, 74),
            Player("p312", "Cameron Green", "RCB", PlayerRole.AR, 8.5, 64, 70.5f, 42),
            Player("p313", "Mitchell Starc", "KKR", PlayerRole.BOWL, 9.0, 72, 79.1f, 56),
            Player("p314", "Mohammed Siraj", "RCB", PlayerRole.BOWL, 9.0, 70, 77.4f, 73),
            Player("p315", "Varun Chakaravarthy", "KKR", PlayerRole.BOWL, 8.5, 76, 81.3f, 29),
            Player("p316", "Harshit Rana", "KKR", PlayerRole.BOWL, 8.5, 68, 72.8f, 22),
            Player("p317", "Yash Dayal", "RCB", PlayerRole.BOWL, 8.0, 58, 63.5f, 10),
            Player("p318", "Lockie Ferguson", "RCB", PlayerRole.BOWL, 8.0, 54, 59.2f, 69),
            Player("p319", "Anuj Rawat", "RCB", PlayerRole.BAT, 7.5, 36, 40.0f, 2),
            Player("p320", "Ramandeep Singh", "KKR", PlayerRole.AR, 7.5, 42, 45.1f, 17),
            Player("p321", "Vaibhav Arora", "KKR", PlayerRole.BOWL, 7.5, 45, 48.0f, 99),
            Player("p322", "Karn Sharma", "RCB", PlayerRole.BOWL, 7.5, 40, 43.2f, 33)
        )
    )

    // Contests for each match
    fun getContestsForMatch(matchId: String): List<Contest> {
        return listOf(
            Contest(
                id = "${matchId}_c1",
                matchId = matchId,
                title = "Mega Contest ₹1.5 Crores",
                category = ContestCategory.MEGA,
                prizePool = 15000000,
                entryFee = 49,
                totalSpots = 400000,
                spotsFilled = 328450,
                firstPrize = "₹15 Lakhs",
                maxWinnersPercent = "65% Winners",
                isGuaranteed = true,
                maxTeams = 20
            ),
            Contest(
                id = "${matchId}_c2",
                matchId = matchId,
                title = "Mini Mega ₹25 Lakhs",
                category = ContestCategory.MEGA,
                prizePool = 2500000,
                entryFee = 29,
                totalSpots = 100000,
                spotsFilled = 71200,
                firstPrize = "₹2 Lakhs",
                maxWinnersPercent = "60% Winners",
                isGuaranteed = true,
                maxTeams = 10
            ),
            Contest(
                id = "${matchId}_c3",
                matchId = matchId,
                title = "Head 2 Head (2 Spots)",
                category = ContestCategory.HEAD_TO_HEAD,
                prizePool = 1000,
                entryFee = 575,
                totalSpots = 2,
                spotsFilled = 1,
                firstPrize = "₹1,000",
                maxWinnersPercent = "1 Winner (50%)",
                isGuaranteed = false,
                maxTeams = 1
            ),
            Contest(
                id = "${matchId}_c4",
                matchId = matchId,
                title = "Winner Takes All (4 Spots)",
                category = ContestCategory.WINNER_TAKES_ALL,
                prizePool = 2000,
                entryFee = 590,
                totalSpots = 4,
                spotsFilled = 3,
                firstPrize = "₹2,000",
                maxWinnersPercent = "1 Winner",
                isGuaranteed = true,
                maxTeams = 1
            ),
            Contest(
                id = "${matchId}_c5",
                matchId = matchId,
                title = "Beginners Champion Cup",
                category = ContestCategory.WINNER_TAKES_ALL,
                prizePool = 50000,
                entryFee = 19,
                totalSpots = 3000,
                spotsFilled = 2410,
                firstPrize = "₹5,000",
                maxWinnersPercent = "55% Winners",
                isGuaranteed = true,
                maxTeams = 3
            ),
            Contest(
                id = "${matchId}_c6",
                matchId = matchId,
                title = "Free Practice League",
                category = ContestCategory.PRACTICE,
                prizePool = 0,
                entryFee = 0,
                totalSpots = 10000,
                spotsFilled = 6890,
                firstPrize = "Glory & Badges",
                maxWinnersPercent = "Top 100",
                isGuaranteed = true,
                maxTeams = 5
            )
        )
    }

    // Expert Insights
    fun getGuruInsight(matchId: String): GuruInsight {
        return when (matchId) {
            "m1" -> GuruInsight(
                matchId = "m1",
                pitchType = "Balanced (Batting First Avg: 168)",
                avgScore = "168 - 185 Runs",
                paceVsSpin = "Pacers: 62% Wickets, Spinners: 38% Wickets",
                weather = "28°C, Clear skies, 14 km/h ocean breeze",
                captainRecommendations = listOf("Virat Kohli", "Jasprit Bumrah", "Travis Head"),
                viceCaptainRecommendations = listOf("Hardik Pandya", "Marcus Stoinis", "Suryakumar Yadav"),
                differentialPicks = listOf("Axar Patel", "Josh Inglis", "Kuldeep Yadav"),
                expertAnalysis = "The surface at Kensington Oval provides even bounce. New ball bowlers get zip, while spinners dominate during the middle overs. Picking all-rounders like Hardik Pandya and Marcus Stoinis provides dual scoring opportunities."
            )
            "m2" -> GuruInsight(
                matchId = "m2",
                pitchType = "Batting Paradise (Avg Score: 195)",
                avgScore = "190 - 210 Runs",
                paceVsSpin = "Pacers: 70% Wickets, Spinners: 30% Wickets",
                weather = "31°C, Humid, Significant evening dew expected",
                captainRecommendations = listOf("Ruturaj Gaikwad", "Rohit Sharma", "Suryakumar Yadav"),
                viceCaptainRecommendations = listOf("Ravindra Jadeja", "Matheesha Pathirana", "Hardik Pandya"),
                differentialPicks = listOf("Shivam Dube", "Gerald Coetzee", "Tilak Varma"),
                expertAnalysis = "Wankhede Stadium is historically high-scoring with short square boundaries. The team chasing has won 68% of evening games due to slippery dew on the outfield. Load up on top 4 batsmen from both sides."
            )
            else -> GuruInsight(
                matchId = matchId,
                pitchType = "Standard T20 Track",
                avgScore = "175 Runs",
                paceVsSpin = "Pacers: 55%, Spinners: 45%",
                weather = "Pleasant, 26°C",
                captainRecommendations = listOf("Star Batsman", "Ace All-Rounder"),
                viceCaptainRecommendations = listOf("Strike Bowler", "Top Wicket-Keeper"),
                differentialPicks = listOf("No. 5 Finisher", "Death Bowler"),
                expertAnalysis = "Opt for in-form top order batsmen and death overs specialist bowlers who bowl the 18th and 20th overs."
            )
        }
    }

    // Leaderboard sample data
    fun getLeaderboard(contestTitle: String, userPoints: Int): List<LeaderboardItem> {
        val list = mutableListOf(
            LeaderboardItem(1, "CotrageKing_77", "Cotrage XI", 482, "₹15,00,000"),
            LeaderboardItem(2, "MasterBlaster99", "Blasters 1", 468, "₹5,00,000"),
            LeaderboardItem(3, "StrikeForce_IN", "Strike 11", 455, "₹2,50,000"),
            LeaderboardItem(4, "CricketWizard", "Wizard Team", 441, "₹1,00,000"),
            LeaderboardItem(5, "ThunderStrike", "Thunder 1", 432, "₹50,000"),
            LeaderboardItem(6, "SuperKingsFan", "WhistlePodu", 420, "₹25,000"),
            LeaderboardItem(7, "YorkerSpecialist", "Speedsters", 412, "₹10,000"),
            LeaderboardItem(8, "SixerKing_Sam", "Sixers XI", 405, "₹5,000")
        )
        // Add User rank
        val userRank = if (userPoints >= 480) 1 else if (userPoints >= 450) 3 else if (userPoints >= 400) 8 else 14
        list.add(
            LeaderboardItem(
                rank = userRank,
                userName = "You (SK Cotrage User)",
                teamName = "My Dream Team",
                points = userPoints,
                prizeText = if (userRank <= 10) "₹5,000" else "₹49 (Entry Back)",
                isCurrentUser = true
            )
        )
        return list.sortedBy { it.rank }
    }

    // Room Database Operations
    fun getTeamsForMatch(matchId: String): Flow<List<UserTeam>> {
        return dao.getTeamsForMatch(matchId).map { entities ->
            entities.map { it.toUserTeam() }
        }
    }

    fun getAllTeams(): Flow<List<UserTeam>> {
        return dao.getAllTeams().map { entities ->
            entities.map { it.toUserTeam() }
        }
    }

    suspend fun saveTeam(
        matchId: String,
        teamName: String,
        captainId: String,
        viceCaptainId: String,
        playerIds: List<String>
    ): String {
        val teamId = "team_${System.currentTimeMillis()}"
        val entity = TeamEntity(
            id = teamId,
            matchId = matchId,
            teamName = teamName,
            captainId = captainId,
            viceCaptainId = viceCaptainId,
            playerIdsJson = playerIds.joinToString(","),
            totalPoints = calculateInitialPoints(playerIds, captainId, viceCaptainId, matchId)
        )
        dao.insertTeam(entity)
        return teamId
    }

    fun getJoinedContests(): Flow<List<JoinedContest>> {
        return dao.getAllJoinedContests().map { entities ->
            entities.map { it.toJoinedContest() }
        }
    }

    fun getJoinedContestsForMatch(matchId: String): Flow<List<JoinedContest>> {
        return dao.getJoinedContestsForMatch(matchId).map { entities ->
            entities.map { it.toJoinedContest() }
        }
    }

    suspend fun joinContest(
        matchId: String,
        contest: Contest,
        teamId: String,
        teamName: String
    ): Boolean {
        // Check wallet balance
        // If entryFee > 0, deduct from wallet
        val entity = JoinedContestEntity(
            id = "jc_${System.currentTimeMillis()}",
            contestId = contest.id,
            matchId = matchId,
            teamId = teamId,
            teamName = teamName,
            contestTitle = contest.title,
            entryFee = contest.entryFee,
            currentRank = (4..25).random(),
            totalParticipants = contest.spotsFilled + 1,
            fantasyPoints = (280..420).random(),
            prizeWon = 0
        )
        dao.insertJoinedContest(entity)

        if (contest.entryFee > 0) {
            deductEntryFee(contest.entryFee, "Joined Contest: ${contest.title}")
        }
        return true
    }

    fun getWallet(): Flow<Wallet> {
        return dao.getWallet().map { entity ->
            if (entity == null) {
                // Initialize default wallet with ₹500 welcome bonus for the user!
                val defaultWallet = WalletEntity(
                    id = 1,
                    depositBalance = 200.0,
                    winningBalance = 150.0,
                    bonusBalance = 150.0
                )
                dao.insertOrUpdateWallet(defaultWallet)
                dao.insertTransaction(
                    TransactionEntity(
                        id = "tx_welcome",
                        title = "SK Cotrage Welcome Sign-up Bonus",
                        amount = 500.0,
                        isCredit = true,
                        dateText = "Today, Welcome Gift"
                    )
                )
                Wallet(200.0, 150.0, 150.0)
            } else {
                Wallet(entity.depositBalance, entity.winningBalance, entity.bonusBalance)
            }
        }
    }

    suspend fun addCash(amount: Double) {
        val currentWallet = dao.getWallet()
        // We will insert or update
        val deposit = 200.0 + amount
        val wallet = WalletEntity(
            id = 1,
            depositBalance = deposit,
            winningBalance = 150.0,
            bonusBalance = 150.0
        )
        dao.insertOrUpdateWallet(wallet)
        val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        dao.insertTransaction(
            TransactionEntity(
                id = "tx_${System.currentTimeMillis()}",
                title = "Added Cash (UPI / Card)",
                amount = amount,
                isCredit = true,
                dateText = dateFormat.format(Date())
            )
        )
    }

    suspend fun withdrawWinnings(amount: Double) {
        val wallet = WalletEntity(
            id = 1,
            depositBalance = 200.0,
            winningBalance = (150.0 - amount).coerceAtLeast(0.0),
            bonusBalance = 150.0
        )
        dao.insertOrUpdateWallet(wallet)
        val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        dao.insertTransaction(
            TransactionEntity(
                id = "tx_${System.currentTimeMillis()}",
                title = "Instant UPI Bank Withdrawal",
                amount = amount,
                isCredit = false,
                dateText = dateFormat.format(Date())
            )
        )
    }

    private suspend fun deductEntryFee(amount: Int, title: String) {
        val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        dao.insertTransaction(
            TransactionEntity(
                id = "tx_${System.currentTimeMillis()}",
                title = title,
                amount = amount.toDouble(),
                isCredit = false,
                dateText = dateFormat.format(Date())
            )
        )
    }

    fun getTransactions(): Flow<List<TransactionRecord>> {
        return dao.getAllTransactions().map { entities ->
            entities.map {
                TransactionRecord(
                    id = it.id,
                    title = it.title,
                    amount = it.amount,
                    isCredit = it.isCredit,
                    dateText = it.dateText
                )
            }
        }
    }

    // Fantasy Point Calculation helper
    private fun calculateInitialPoints(
        playerIds: List<String>,
        captainId: String,
        viceCaptainId: String,
        matchId: String
    ): Int {
        val players = matchPlayers[matchId] ?: return 0
        var total = 0
        for (pid in playerIds) {
            val p = players.find { it.id == pid } ?: continue
            val multiplier = when (pid) {
                captainId -> 2.0
                viceCaptainId -> 1.5
                else -> 1.0
            }
            total += (p.points * multiplier).toInt()
        }
        return total
    }

    private fun TeamEntity.toUserTeam(): UserTeam {
        return UserTeam(
            id = id,
            matchId = matchId,
            teamName = teamName,
            captainId = captainId,
            viceCaptainId = viceCaptainId,
            playerIds = playerIdsJson.split(",").filter { it.isNotBlank() },
            totalPoints = totalPoints
        )
    }

    private fun JoinedContestEntity.toJoinedContest(): JoinedContest {
        return JoinedContest(
            id = id,
            contestId = contestId,
            matchId = matchId,
            teamId = teamId,
            teamName = teamName,
            contestTitle = contestTitle,
            entryFee = entryFee,
            currentRank = currentRank,
            totalParticipants = totalParticipants,
            fantasyPoints = fantasyPoints,
            prizeWon = prizeWon
        )
    }
}
