package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.LiveCricketScoreCard
import com.example.ui.theme.*

@Composable
fun MyMatchesScreen(
    joinedContests: List<JoinedContest>,
    matches: List<Match>,
    simulatedBonusPoints: Int,
    lastEventMessage: String?,
    onSimulateLiveBall: (Match) -> Unit,
    onViewLeaderboard: (Match, Contest) -> Unit,
    onExploreMatches: () -> Unit
) {
    val liveMatch = matches.find { it.status == MatchStatus.LIVE }

    // Derive dynamic current over balls based on simulation bonus
    val currentOverBalls = remember(simulatedBonusPoints) {
        val pool = listOf("0", "1", "4", "2", "6", "W", "1")
        val count = (simulatedBonusPoints / 10).coerceIn(1, 6)
        pool.take(count)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("my_matches_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Reusable Live Cricket Score Card with Ball-by-Ball updates
        if (liveMatch != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LiveCricketScoreCard(
                        match = liveMatch,
                        recentBalls = currentOverBalls,
                        onRefreshClick = { onSimulateLiveBall(liveMatch) }
                    )

                    // Simulation Action Bar
                    Surface(
                        color = DarkSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Simulate Match Action",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = lastEventMessage ?: "Simulate balls to update score & fantasy pts",
                                    fontSize = 11.sp,
                                    color = if (lastEventMessage != null) TrophyGold else TextSecondary,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }

                            Button(
                                onClick = { onSimulateLiveBall(liveMatch) },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("simulate_live_ball_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = "Simulate",
                                        tint = TrophyGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("Next Ball ⚡", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Joined Contests
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Active Contests",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                Text(
                    text = "${joinedContests.size} Joined",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        if (joinedContests.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsScore,
                            contentDescription = "No Contests",
                            tint = TextMuted,
                            modifier = Modifier.size(60.dp)
                        )
                        Text(
                            text = "You haven't entered any contests yet!",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Pick a match, build your dream 11 with 100 credits, and join Mega leagues to win big cash!",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onExploreMatches,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Find Matches & Play", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(joinedContests) { jc ->
                val match = matches.find { it.id == jc.matchId } ?: matches.first()
                val totalPoints = jc.fantasyPoints + simulatedBonusPoints
                val adjustedRank = if (simulatedBonusPoints > 30) (jc.currentRank - 3).coerceAtLeast(1) else jc.currentRank

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = jc.contestTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${match.team1Code} vs ${match.team2Code} • Entry ₹${jc.entryFee}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PrimaryRed.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryRed)
                            ) {
                                Text(
                                    text = "LIVE",
                                    color = PrimaryRed,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats Grid: Rank, Fantasy Points, Winnings
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkSurfaceElevated, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Team", fontSize = 10.sp, color = TextSecondary)
                                Text(jc.teamName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Fantasy Points", fontSize = 10.sp, color = TextSecondary)
                                Text("$totalPoints pts", fontWeight = FontWeight.Black, fontSize = 15.sp, color = TrophyGold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Current Rank", fontSize = 10.sp, color = TextSecondary)
                                Text("#$adjustedRank", fontWeight = FontWeight.Black, fontSize = 15.sp, color = PitchGreen)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Projected Winning: ₹${if (adjustedRank <= 5) "50,000" else "49"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TrophyGold
                            )

                            Button(
                                onClick = {
                                    val dummyContest = Contest(
                                        id = jc.contestId,
                                        matchId = jc.matchId,
                                        title = jc.contestTitle,
                                        category = ContestCategory.MEGA,
                                        prizePool = 15000000,
                                        entryFee = jc.entryFee,
                                        totalSpots = 400000,
                                        spotsFilled = 350000,
                                        firstPrize = "₹15 Lakhs",
                                        maxWinnersPercent = "65%"
                                    )
                                    onViewLeaderboard(match, dummyContest)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("Leaderboard ->", color = SkyAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
