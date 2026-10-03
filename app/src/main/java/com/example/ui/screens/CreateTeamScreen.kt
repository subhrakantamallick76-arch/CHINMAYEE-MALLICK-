package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.PlayerItemRow
import com.example.ui.theme.*

@Composable
fun CreateTeamScreen(
    match: Match,
    allPlayers: List<Player>,
    selectedPlayerIds: Set<String>,
    currentRole: PlayerRole,
    onRoleSelected: (PlayerRole) -> Unit,
    onTogglePlayer: (Player) -> Unit,
    onBack: () -> Unit,
    onPreviewPitch: () -> Unit,
    onNext: () -> Unit,
    validationError: String?
) {
    val selectedPlayers = allPlayers.filter { selectedPlayerIds.contains(it.id) }
    val creditsUsed = selectedPlayers.sumOf { it.credits }
    val creditsRemaining = (100.0 - creditsUsed).coerceAtLeast(0.0)

    val team1Count = selectedPlayers.count { it.teamCode == match.team1Code }
    val team2Count = selectedPlayers.count { it.teamCode == match.team2Code }

    val wkCount = selectedPlayers.count { it.role == PlayerRole.WK }
    val batCount = selectedPlayers.count { it.role == PlayerRole.BAT }
    val arCount = selectedPlayers.count { it.role == PlayerRole.AR }
    val bowlCount = selectedPlayers.count { it.role == PlayerRole.BOWL }

    val roleFilteredPlayers = allPlayers.filter { it.role == currentRole }

    Scaffold(
        topBar = {
            Surface(
                color = DarkSurface,
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                    // Header Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
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
                                    text = "Create Fantasy Team",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${match.team1Code} vs ${match.team2Code}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Team Count Chips
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = DarkSurfaceElevated
                            ) {
                                Text(
                                    text = "${match.team1Code} : $team1Count",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = DarkSurfaceElevated
                            ) {
                                Text(
                                    text = "${match.team2Code} : $team2Count",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Progress Overview Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurfaceElevated)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Players",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${selectedPlayerIds.size}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (selectedPlayerIds.size == 11) PitchGreen else TextPrimary
                                )
                                Text(
                                    text = "/11",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }

                        // 11-step visual indicator dots
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 0 until 11) {
                                val isFilled = i < selectedPlayerIds.size
                                Box(
                                    modifier = Modifier
                                        .size(14.dp, 8.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (isFilled) PrimaryRed else DarkSurfaceBorder)
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Credits Left",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = String.format("%.1f", creditsRemaining),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TrophyGold
                            )
                        }
                    }

                    // Role Selector Tabs (WK, BAT, AR, BOWL)
                    TabRow(
                        selectedTabIndex = currentRole.ordinal,
                        containerColor = DarkSurface,
                        contentColor = PrimaryRed,
                        divider = {}
                    ) {
                        PlayerRole.values().forEach { role ->
                            val count = when (role) {
                                PlayerRole.WK -> wkCount
                                PlayerRole.BAT -> batCount
                                PlayerRole.AR -> arCount
                                PlayerRole.BOWL -> bowlCount
                            }
                            Tab(
                                selected = currentRole == role,
                                onClick = { onRoleSelected(role) },
                                text = {
                                    Text(
                                        text = "${role.shortName} ($count)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            )
                        }
                    }

                    // Role rule guidance
                    Surface(
                        color = DarkSurfaceElevated.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Pick ${currentRole.minCount}-${currentRole.maxCount} ${currentRole.displayName}",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
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
                Column(modifier = Modifier.padding(14.dp)) {
                    if (validationError != null) {
                        Text(
                            text = validationError,
                            color = PrimaryRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onPreviewPitch,
                            modifier = Modifier.weight(1f).testTag("preview_pitch_button"),
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
                            onClick = onNext,
                            modifier = Modifier.weight(1f).testTag("continue_to_captain_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedPlayerIds.size == 11) PrimaryRed else DarkSurfaceBorder
                            ),
                            enabled = selectedPlayerIds.size == 11
                        ) {
                            Text(
                                text = "NEXT (C / VC)",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedPlayerIds.size == 11) Color.White else TextMuted
                            )
                        }
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(roleFilteredPlayers, key = { it.id }) { player ->
                val isSelected = selectedPlayerIds.contains(player.id)
                PlayerItemRow(
                    player = player,
                    isSelected = isSelected,
                    onToggle = { onTogglePlayer(player) }
                )
            }
        }
    }
}
