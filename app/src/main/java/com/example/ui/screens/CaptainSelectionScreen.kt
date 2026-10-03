package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Match
import com.example.model.Player
import com.example.ui.theme.*

@Composable
fun CaptainSelectionScreen(
    match: Match,
    players: List<Player>,
    captainId: String?,
    viceCaptainId: String?,
    onSelectCaptain: (String) -> Unit,
    onSelectViceCaptain: (String) -> Unit,
    onBack: () -> Unit,
    onPreviewPitch: () -> Unit,
    onSaveTeam: () -> Unit
) {
    val canSave = captainId != null && viceCaptainId != null

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
                                text = "Choose Captain & Vice Captain",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "C gets 2X points • VC gets 1.5X points",
                                fontSize = 11.sp,
                                color = TrophyGold
                            )
                        }
                    }

                    // Multiplier Banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurfaceElevated)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("C", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Text(
                                text = "Captain (2X Pts)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (captainId != null) PitchGreen else TextSecondary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(TrophyGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("VC", color = DarkNavyBg, fontWeight = FontWeight.Black, fontSize = 11.sp)
                            }
                            Text(
                                text = "Vice-Captain (1.5X Pts)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (viceCaptainId != null) PitchGreen else TextSecondary
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = DarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onPreviewPitch,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SkyAccent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Pitch View",
                            tint = SkyAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("PITCH VIEW", color = SkyAccent, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onSaveTeam,
                        modifier = Modifier.weight(1f).testTag("save_team_final_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (canSave) PrimaryRed else DarkSurfaceBorder
                        ),
                        enabled = canSave
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SAVE TEAM",
                            fontWeight = FontWeight.Bold,
                            color = if (canSave) Color.White else TextMuted
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(players, key = { it.id }) { player ->
                val isC = player.id == captainId
                val isVC = player.id == viceCaptainId

                Surface(
                    color = DarkSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isC) PrimaryRed else if (isVC) TrophyGold else DarkSurfaceBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Player Info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = player.role.shortName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TrophyGold
                                )
                            }

                            Column {
                                Text(
                                    text = player.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${player.teamCode} • ${player.points} pts",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        // C and VC Selection Buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Captain Button
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isC) PrimaryRed else DarkSurfaceElevated)
                                    .border(1.dp, if (isC) PrimaryRed else DarkSurfaceBorder, CircleShape)
                                    .clickable { onSelectCaptain(player.id) }
                                    .testTag("captain_btn_${player.id}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "2X",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp,
                                    color = if (isC) Color.White else TextSecondary
                                )
                            }

                            // Vice Captain Button
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isVC) TrophyGold else DarkSurfaceElevated)
                                    .border(1.dp, if (isVC) TrophyGold else DarkSurfaceBorder, CircleShape)
                                    .clickable { onSelectViceCaptain(player.id) }
                                    .testTag("vice_captain_btn_${player.id}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "1.5X",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    color = if (isVC) DarkNavyBg else TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
