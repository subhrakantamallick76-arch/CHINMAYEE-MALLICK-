package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Match
import com.example.model.MatchStatus
import com.example.ui.theme.*

/**
 * Reusable Composable card component to display live cricket score updates,
 * including team names, scores, and current over/ball status.
 */
@Composable
fun LiveCricketScoreCard(
    team1Name: String,
    team1Code: String,
    team1Score: String,
    team1Overs: String,
    team1Color: Color = TeamIndiaBlue,
    team2Name: String,
    team2Code: String,
    team2Score: String,
    team2Overs: String = "",
    team2Color: Color = TeamAusYellow,
    currentOverStatus: String = "Over 14.2",
    recentBalls: List<String> = listOf("1", "4", "0", "6", "W", "1"),
    currentRunRate: String? = "9.62",
    requiredRunRate: String? = null,
    seriesName: String? = "T20 Championship Super 8",
    venue: String? = "Kensington Oval, Bridgetown",
    statusNote: String? = "India need 38 runs in 26 balls to win",
    batterOnStrike: String? = "Suryakumar Yadav* 41 (22)",
    nonStriker: String? = "Hardik Pandya 24 (14)",
    currentBowler: String? = "Pat Cummins 3.2-0-32-1",
    isLive: Boolean = true,
    modifier: Modifier = Modifier,
    onCardClick: (() -> Unit)? = null,
    onRefreshClick: (() -> Unit)? = null
) {
    // Pulsating animation for the LIVE badge
    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onCardClick != null) Modifier.clickable { onCardClick() } else Modifier
            )
            .testTag("live_cricket_score_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.2.dp,
            brush = if (isLive) {
                Brush.linearGradient(
                    listOf(PrimaryRed.copy(alpha = 0.8f), DarkSurfaceBorder, TrophyGold.copy(alpha = 0.6f))
                )
            } else {
                Brush.linearGradient(listOf(DarkSurfaceBorder, DarkSurfaceBorder))
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            if (isLive) Color(0xFF1E112A) else DarkSurfaceElevated,
                            DarkSurface
                        )
                    )
                )
        ) {
            // Header: Series, Live Pill & Over info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated.copy(alpha = 0.65f))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (isLive) {
                        Surface(
                            color = PrimaryRed.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryRed)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryRed.copy(alpha = pulseAlpha))
                                )
                                Text(
                                    text = "LIVE",
                                    color = PrimaryRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    if (!seriesName.isNullOrBlank()) {
                        Text(
                            text = seriesName,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = currentOverStatus,
                        color = TrophyGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (onRefreshClick != null) {
                        IconButton(
                            onClick = onRefreshClick,
                            modifier = Modifier.size(24.dp).testTag("live_score_refresh_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Score",
                                tint = SkyAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Teams and Live Score Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Team 1 Row
                TeamScoreRow(
                    teamName = team1Name,
                    teamCode = team1Code,
                    score = team1Score,
                    overs = team1Overs,
                    teamColor = team1Color,
                    isBattingNow = isLive && (team2Score.isBlank() || team2Score.contains("yet", ignoreCase = true)),
                    testTagPrefix = "team1"
                )

                // Team 2 Row
                TeamScoreRow(
                    teamName = team2Name,
                    teamCode = team2Code,
                    score = team2Score.ifBlank { "Yet to bat" },
                    overs = team2Overs,
                    teamColor = team2Color,
                    isBattingNow = isLive && team2Score.isNotBlank() && !team2Score.contains("yet", ignoreCase = true),
                    testTagPrefix = "team2"
                )
            }

            // Current Over Ball-by-Ball Tracker
            if (recentBalls.isNotEmpty()) {
                Surface(
                    color = DarkSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "THIS OVER:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 0.5.sp
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.testTag("current_over_balls_row")
                            ) {
                                items(recentBalls) { ball ->
                                    BallOutcomeChip(ballOutcome = ball)
                                }
                            }
                        }

                        // Run Rates
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!currentRunRate.isNullOrBlank()) {
                                Text(
                                    text = "CRR: $currentRunRate",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                            }
                            if (!requiredRunRate.isNullOrBlank()) {
                                Text(
                                    text = "RRR: $requiredRunRate",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryRed
                                )
                            }
                        }
                    }
                }
            }

            // Live Players On Field (Batters & Bowler)
            if (!batterOnStrike.isNullOrBlank() || !currentBowler.isNullOrBlank()) {
                HorizontalDivider(color = DarkSurfaceBorder, thickness = 0.6.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!batterOnStrike.isNullOrBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SportsCricket,
                                    contentDescription = "Batter",
                                    tint = TrophyGold,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = batterOnStrike,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        if (!currentBowler.isNullOrBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SportsBaseball,
                                    contentDescription = "Bowler",
                                    tint = SkyAccent,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = currentBowler,
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    if (!nonStriker.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Non-striker:",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                            Text(
                                text = nonStriker,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Bottom Status Note or Match Venue
            if (!statusNote.isNullOrBlank() || !venue.isNullOrBlank()) {
                Surface(
                    color = DarkSurfaceElevated.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = statusNote ?: venue.orEmpty(),
                            fontSize = 11.sp,
                            color = TrophyGoldLight,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        if (!venue.isNullOrBlank() && !statusNote.isNullOrBlank()) {
                            Text(
                                text = venue,
                                fontSize = 10.sp,
                                color = TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Convenience overload that binds directly to a [Match] model.
 */
@Composable
fun LiveCricketScoreCard(
    match: Match,
    modifier: Modifier = Modifier,
    recentBalls: List<String> = listOf("1", "4", "0", "6", "W", "1"),
    onCardClick: (() -> Unit)? = null,
    onRefreshClick: (() -> Unit)? = null
) {
    LiveCricketScoreCard(
        team1Name = match.team1Name,
        team1Code = match.team1Code,
        team1Score = match.team1Score.ifBlank { "138/3" },
        team1Overs = "(14.2 ov)",
        team1Color = Color(match.team1Color),
        team2Name = match.team2Name,
        team2Code = match.team2Code,
        team2Score = match.team2Score.ifBlank { "Yet to bat" },
        team2Overs = "",
        team2Color = Color(match.team2Color),
        currentOverStatus = match.timeString,
        recentBalls = recentBalls,
        seriesName = match.seriesName,
        venue = match.venue,
        statusNote = match.liveCommentary.ifBlank { "Match in progress" },
        isLive = match.status == MatchStatus.LIVE,
        modifier = modifier,
        onCardClick = onCardClick,
        onRefreshClick = onRefreshClick
    )
}

@Composable
private fun TeamScoreRow(
    teamName: String,
    teamCode: String,
    score: String,
    overs: String,
    teamColor: Color,
    isBattingNow: Boolean,
    testTagPrefix: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Team Emblem and Names
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(teamColor)
                    .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = teamCode.take(3),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = teamName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    if (isBattingNow) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(PitchGreen)
                        )
                    }
                }
                Text(
                    text = teamCode,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        // Score and Overs
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = score,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isBattingNow) TrophyGold else TextPrimary,
                modifier = Modifier.testTag("${testTagPrefix}_score_text")
            )
            if (overs.isNotBlank()) {
                Text(
                    text = overs,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Chip representing individual ball outcome in current over.
 * 4 -> Green boundary
 * 6 -> Purple/Gold maximum
 * W -> Crimson Red wicket
 * 0 -> Muted gray dot ball
 * 1, 2, 3 -> Clean white single/double
 */
@Composable
fun BallOutcomeChip(
    ballOutcome: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, isBold) = when (ballOutcome.uppercase().trim()) {
        "4" -> Triple(PitchGreen, Color.White, true)
        "6" -> Triple(Color(0xFF9333EA), Color.White, true)
        "W", "OUT" -> Triple(PrimaryRed, Color.White, true)
        "0", "." -> Triple(DarkSurfaceBorder, TextMuted, false)
        "WD", "NB" -> Triple(TrophyGold, DarkNavyBg, true)
        else -> Triple(DarkSurfaceElevated, TextPrimary, false)
    }

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(0.8.dp, Color.White.copy(alpha = 0.2f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = ballOutcome,
            fontSize = 10.sp,
            fontWeight = if (isBold) FontWeight.Black else FontWeight.Bold,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LiveCricketScoreCardPreview() {
    SKCotrageTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LiveCricketScoreCard(
                team1Name = "India",
                team1Code = "IND",
                team1Score = "148/3",
                team1Overs = "15.3 ov",
                team2Name = "Australia",
                team2Code = "AUS",
                team2Score = "Yet to bat",
                currentOverStatus = "Over 15.3 (LIVE)",
                recentBalls = listOf("1", "4", "0", "6", "W", "1"),
                currentRunRate = "9.55",
                batterOnStrike = "V. Kohli* 54 (36)",
                currentBowler = "P. Cummins 3.3-0-28-1",
                statusNote = "India 1st Innings • Kensington Oval"
            )
        }
    }
}
