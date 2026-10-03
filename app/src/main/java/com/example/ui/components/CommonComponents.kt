package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun AppHeader(
    wallet: Wallet,
    onWalletClick: () -> Unit,
    onNotificationClick: () -> Unit = {}
) {
    Surface(
        color = DarkSurface,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Branding
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(PrimaryRed, TrophyGold)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SK",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SK COTRAGE",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 1.sp,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "FANTASY CRICKET",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TrophyGold,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            // Wallet Balance Chip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onWalletClick() }
                        .testTag("wallet_balance_chip")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Wallet",
                            tint = TrophyGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "₹${wallet.totalBalance.toInt()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Cash",
                            tint = PitchGreen,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MatchCard(
    match: Match,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("match_card_${match.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top Series & Status Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.seriesName,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (match.status == MatchStatus.LIVE) {
                    Surface(
                        color = PrimaryRed.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryRed)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryRed)
                            )
                            Text(
                                text = "LIVE",
                                color = PrimaryRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else if (match.status == MatchStatus.COMPLETED) {
                    Surface(
                        color = Color.Gray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "COMPLETED",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Match Time",
                            tint = TrophyGold,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = match.timeString,
                            color = TrophyGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Teams & Score Center
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Team 1
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(match.team1Color)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = match.team1Code,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                    Column {
                        Text(
                            text = match.team1Code,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = match.team1Name,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (match.team1Score.isNotBlank()) {
                            Text(
                                text = match.team1Score,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }

                // VS badge / Live text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "VS",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }

                // Team 2
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = match.team2Code,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = match.team2Name,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (match.team2Score.isNotBlank()) {
                            Text(
                                text = match.team2Score,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(match.team2Color)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = match.team2Code,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Bottom Mega Contest Banner inside card
            HorizontalDivider(color = DarkSurfaceBorder, thickness = 0.8.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated.copy(alpha = 0.5f))
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Mega Contest",
                        tint = TrophyGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Mega Prize ${match.megaPrizePoolText}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryRed
                ) {
                    Text(
                        text = "Play ${match.entryFeeStarting}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ContestCard(
    contest: Contest,
    onJoinClick: () -> Unit,
    onLeaderboardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("contest_card_${contest.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Contest Header: Prize Pool and Entry Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Prize Pool",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = if (contest.prizePool > 0) "₹${formatCurrency(contest.prizePool)}" else "Practice Free",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TrophyGold
                    )
                }

                Button(
                    onClick = onJoinClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (contest.entryFee == 0) PitchGreen else PrimaryRed
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("join_contest_btn_${contest.id}")
                ) {
                    Text(
                        text = if (contest.entryFee == 0) "FREE" else "₹${contest.entryFee}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }

            // Progress Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            ) {
                LinearProgressIndicator(
                    progress = { contest.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PrimaryRed,
                    trackColor = DarkSurfaceBorder
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${formatCompactNumber(contest.spotsLeft)} spots left",
                        fontSize = 11.sp,
                        color = PrimaryRed,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${formatCompactNumber(contest.totalSpots)} spots",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = DarkSurfaceBorder, thickness = 0.7.dp)

            // Contest Footer: 1st Prize, Winners %, Guaranteed tag & Leaderboard Link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated.copy(alpha = 0.6f))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "First Prize",
                            tint = TrophyGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "1st: ${contest.firstPrize}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "• ${contest.maxWinnersPercent}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    if (contest.isGuaranteed) {
                        Surface(
                            shape = RoundedCornerShape(3.dp),
                            color = PitchGreen.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Guaranteed",
                                fontSize = 9.sp,
                                color = PitchGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Leaderboard",
                    color = SkyAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onLeaderboardClick() }
                )
            }
        }
    }
}

@Composable
fun PlayerItemRow(
    player: Player,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .testTag("player_row_${player.id}"),
        color = if (isSelected) PrimaryRed.copy(alpha = 0.12f) else DarkSurface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) PrimaryRed.copy(alpha = 0.4f) else DarkSurfaceBorder
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Player info with avatar & team
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceElevated)
                        .border(1.5.dp, if (isSelected) PrimaryRed else DarkSurfaceBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = player.name.take(2).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = player.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = DarkSurfaceElevated
                        ) {
                            Text(
                                text = player.teamCode,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TrophyGold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "Sel by ${player.selectionPercentage}%",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Points & Credits
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${player.points}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "pts",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${player.credits}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = TrophyGold
                    )
                    Text(
                        text = "cr",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                // Add / Remove action button
                IconButton(
                    onClick = onToggle,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.RemoveCircle else Icons.Default.AddCircle,
                        contentDescription = if (isSelected) "Remove Player" else "Add Player",
                        tint = if (isSelected) PrimaryRed else PitchGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CricketGroundView(
    players: List<Player>,
    captainId: String,
    viceCaptainId: String,
    modifier: Modifier = Modifier
) {
    val wkPlayers = players.filter { it.role == PlayerRole.WK }
    val batPlayers = players.filter { it.role == PlayerRole.BAT }
    val arPlayers = players.filter { it.role == PlayerRole.AR }
    val bowlPlayers = players.filter { it.role == PlayerRole.BOWL }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F5132),
                        Color(0xFF198754),
                        Color(0xFF0F5132)
                    )
                )
            )
            .border(2.dp, Color(0xFF20C997).copy(alpha = 0.5f), RoundedCornerShape(18.dp))
    ) {
        // Turf & Pitch Background Graphics
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Outer Field Oval
            drawOval(
                color = Color.White.copy(alpha = 0.15f),
                topLeft = Offset(w * 0.05f, h * 0.04f),
                size = androidx.compose.ui.geometry.Size(w * 0.90f, h * 0.92f),
                style = Stroke(width = 2f)
            )

            // Inner 30-Yard Circle
            drawOval(
                color = Color.White.copy(alpha = 0.12f),
                topLeft = Offset(w * 0.18f, h * 0.15f),
                size = androidx.compose.ui.geometry.Size(w * 0.64f, h * 0.70f),
                style = Stroke(width = 1.5f)
            )

            // Center Pitch Strip (Clay / Sand)
            drawRoundRect(
                color = Color(0xFFD4A373).copy(alpha = 0.45f),
                topLeft = Offset(w * 0.40f, h * 0.35f),
                size = androidx.compose.ui.geometry.Size(w * 0.20f, h * 0.30f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )

            // Bowling Crease Lines
            drawLine(
                color = Color.White.copy(alpha = 0.4f),
                start = Offset(w * 0.38f, h * 0.38f),
                end = Offset(w * 0.62f, h * 0.38f),
                strokeWidth = 2f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.4f),
                start = Offset(w * 0.38f, h * 0.62f),
                end = Offset(w * 0.62f, h * 0.62f),
                strokeWidth = 2f
            )
        }

        // Realistic Cricket Formation
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Wicket Keepers Row (Top)
            PitchRoleSection(title = "WICKET-KEEPERS", players = wkPlayers, captainId, viceCaptainId)

            // Batters Row
            PitchRoleSection(title = "BATTERS", players = batPlayers, captainId, viceCaptainId)

            // All-Rounders Row
            PitchRoleSection(title = "ALL-ROUNDERS", players = arPlayers, captainId, viceCaptainId)

            // Bowlers Row (Bottom)
            PitchRoleSection(title = "BOWLERS", players = bowlPlayers, captainId, viceCaptainId)
        }
    }
}

@Composable
private fun PitchRoleSection(
    title: String,
    players: List<Player>,
    captainId: String,
    viceCaptainId: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = Color.Black.copy(alpha = 0.35f),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            players.forEach { p ->
                PitchPlayerToken(
                    player = p,
                    isCaptain = p.id == captainId,
                    isViceCaptain = p.id == viceCaptainId
                )
            }
        }
    }
}

@Composable
fun PitchPlayerToken(
    player: Player,
    isCaptain: Boolean,
    isViceCaptain: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(68.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F172A))
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = player.name.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
            }

            if (isCaptain) {
                Box(
                    modifier = Modifier
                        .offset(x = 6.dp, y = (-4).dp)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(PrimaryRed)
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "2x",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            } else if (isViceCaptain) {
                Box(
                    modifier = Modifier
                        .offset(x = 6.dp, y = (-4).dp)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(TrophyGold)
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1.5",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = DarkNavyBg
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(4.dp),
            color = Color.Black.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Text(
                text = player.name.split(" ").lastOrNull() ?: player.name,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
            )
        }

        Text(
            text = "${player.credits} Cr",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// Helpers
private fun formatCurrency(amount: Long): String {
    return when {
        amount >= 10000000 -> "${amount / 10000000.0} Crores".replace(".0", "")
        amount >= 100000 -> "${amount / 100000.0} Lakhs".replace(".0", "")
        amount >= 1000 -> "${amount / 1000.0}k".replace(".0", "")
        else -> "$amount"
    }
}

private fun formatCompactNumber(count: Int): String {
    return when {
        count >= 100000 -> "${(count / 100000.0 * 10).toInt() / 10.0}L".replace(".0", "")
        count >= 1000 -> "${(count / 1000.0 * 10).toInt() / 10.0}k".replace(".0", "")
        else -> "$count"
    }
}
