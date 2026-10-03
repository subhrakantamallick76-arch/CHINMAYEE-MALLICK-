package com.example.model

enum class PlayerRole(val displayName: String, val shortName: String, val minCount: Int, val maxCount: Int) {
    WK("Wicket-Keepers", "WK", 1, 4),
    BAT("Batters", "BAT", 3, 6),
    AR("All-Rounders", "AR", 1, 4),
    BOWL("Bowlers", "BOWL", 3, 6)
}

data class Player(
    val id: String,
    val name: String,
    val teamCode: String,
    val role: PlayerRole,
    val credits: Double,
    val points: Int,
    val selectionPercentage: Float,
    val jerseyNumber: Int,
    val isPlaying: Boolean = true,
    var runs: Int = 0,
    var wickets: Int = 0,
    var catches: Int = 0,
    var ballsFaced: Int = 0,
    var oversBowled: Float = 0.0f
)

enum class MatchStatus {
    UPCOMING, LIVE, COMPLETED
}

data class Match(
    val id: String,
    val team1Code: String,
    val team1Name: String,
    val team1Color: Long,
    val team2Code: String,
    val team2Name: String,
    val team2Color: Long,
    val seriesName: String,
    val status: MatchStatus,
    val timeString: String,
    val venue: String,
    val pitchReport: String,
    val megaPrizePoolText: String,
    val firstPrizeText: String,
    val entryFeeStarting: String,
    val team1Score: String = "",
    val team2Score: String = "",
    val liveCommentary: String = ""
)

enum class ContestCategory(val displayName: String) {
    ALL("All Contests"),
    MEGA("Mega Contests"),
    HEAD_TO_HEAD("Head to Head"),
    WINNER_TAKES_ALL("Winner Takes All"),
    PRACTICE("Practice & Free")
}

data class Contest(
    val id: String,
    val matchId: String,
    val title: String,
    val category: ContestCategory,
    val prizePool: Long,
    val entryFee: Int,
    val totalSpots: Int,
    var spotsFilled: Int,
    val firstPrize: String,
    val maxWinnersPercent: String,
    val isGuaranteed: Boolean = true,
    val maxTeams: Int = 20
) {
    val spotsLeft: Int get() = (totalSpots - spotsFilled).coerceAtLeast(0)
    val progress: Float get() = if (totalSpots > 0) spotsFilled.toFloat() / totalSpots else 0f
}

data class UserTeam(
    val id: String,
    val matchId: String,
    val teamName: String,
    val captainId: String,
    val viceCaptainId: String,
    val playerIds: List<String>,
    var totalPoints: Int = 0
)

data class JoinedContest(
    val id: String,
    val contestId: String,
    val matchId: String,
    val teamId: String,
    val teamName: String,
    val contestTitle: String,
    val entryFee: Int,
    val currentRank: Int,
    val totalParticipants: Int,
    val fantasyPoints: Int,
    val prizeWon: Int
)

data class Wallet(
    val depositBalance: Double,
    val winningBalance: Double,
    val bonusBalance: Double
) {
    val totalBalance: Double get() = depositBalance + winningBalance + bonusBalance
}

data class TransactionRecord(
    val id: String,
    val title: String,
    val amount: Double,
    val isCredit: Boolean,
    val dateText: String
)

data class LeaderboardItem(
    val rank: Int,
    val userName: String,
    val teamName: String,
    val points: Int,
    val prizeText: String,
    val isCurrentUser: Boolean = false
)

data class GuruInsight(
    val matchId: String,
    val pitchType: String,
    val avgScore: String,
    val paceVsSpin: String,
    val weather: String,
    val captainRecommendations: List<String>,
    val viceCaptainRecommendations: List<String>,
    val differentialPicks: List<String>,
    val expertAnalysis: String
)
