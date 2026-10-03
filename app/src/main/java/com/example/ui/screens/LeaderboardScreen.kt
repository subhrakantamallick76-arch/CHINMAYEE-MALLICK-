package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Contest
import com.example.model.LeaderboardItem
import com.example.model.Match
import com.example.ui.theme.*

@Composable
fun LeaderboardScreen(
    match: Match,
    contest: Contest,
    items: List<LeaderboardItem>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            Surface(
                color = DarkSurface,
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary
                            )
                        }

                        Column {
                            Text(
                                text = "Leaderboard",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = contest.title,
                                fontSize = 11.sp,
                                color = TrophyGold
                            )
                        }
                    }

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurfaceElevated)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("RANK & USER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.weight(1.5f))
                        Text("POINTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.weight(0.8f))
                        Text("PRIZE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.weight(0.8f))
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("leaderboard_list"),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(items, key = { "${it.rank}_${it.userName}" }) { item ->
                val isCurrent = item.isCurrentUser

                Surface(
                    color = if (isCurrent) PrimaryRed.copy(alpha = 0.18f) else DarkSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCurrent) PrimaryRed else DarkSurfaceBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Rank and User info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            if (item.rank == 1) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(TrophyGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = "1st",
                                        tint = DarkNavyBg,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = "#${item.rank}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = if (isCurrent) PrimaryRed else TextSecondary,
                                    modifier = Modifier.width(28.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = item.userName,
                                    fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isCurrent) Color.White else TextPrimary
                                )
                                Text(
                                    text = item.teamName,
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Points
                        Text(
                            text = "${item.points}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = TrophyGold,
                            modifier = Modifier.weight(0.8f)
                        )

                        // Prize
                        Text(
                            text = item.prizeText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = PitchGreen,
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                }
            }
        }
    }
}
