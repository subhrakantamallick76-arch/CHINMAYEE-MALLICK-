package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Match
import com.example.model.Player
import com.example.ui.components.CricketGroundView
import com.example.ui.theme.*

@Composable
fun PitchPreviewScreen(
    match: Match,
    players: List<Player>,
    captainId: String,
    viceCaptainId: String,
    onBack: () -> Unit
) {
    val team1Count = players.count { it.teamCode == match.team1Code }
    val team2Count = players.count { it.teamCode == match.team2Code }
    val totalCredits = players.sumOf { it.credits }

    Scaffold(
        topBar = {
            Surface(
                color = DarkSurface,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary
                            )
                        }
                        Column {
                            Text(
                                text = "Pitch Team Preview",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "${match.team1Code} $team1Count : $team2Count ${match.team2Code} • ${String.format("%.1f", totalCredits)} Cr",
                                fontSize = 11.sp,
                                color = TrophyGold
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
                .testTag("pitch_preview_screen")
        ) {
            CricketGroundView(
                players = players,
                captainId = captainId,
                viceCaptainId = viceCaptainId,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
