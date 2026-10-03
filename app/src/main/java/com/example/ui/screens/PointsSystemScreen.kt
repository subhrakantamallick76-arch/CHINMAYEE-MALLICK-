package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*

@Composable
fun PointsSystemScreen(
    onBack: () -> Unit
) {
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
                            text = "Fantasy Points System",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Official T20 / ODI Point Calculations",
                            fontSize = 11.sp,
                            color = TrophyGold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("points_system_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Captaincy Rule
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryRed.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("MULTIPLIERS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = PrimaryRed, letterSpacing = 1.sp)
                        Text("• Captain (C): 2X Points on all actions", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        Text("• Vice-Captain (VC): 1.5X Points on all actions", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TrophyGold)
                        Text("• Starting 11 Announcement: +4 Points for each playing player", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }

            // Batting Points
            item {
                PointsCategoryCard(
                    title = "Batting Points",
                    icon = Icons.Default.SportsCricket,
                    iconColor = TrophyGold,
                    rules = listOf(
                        "Run" to "+1 pt",
                        "Boundary Bonus (4s)" to "+1 pt",
                        "Six Bonus (6s)" to "+2 pts",
                        "30 Run Bonus" to "+4 pts",
                        "Half-Century (50 Runs)" to "+8 pts",
                        "Century (100 Runs)" to "+16 pts",
                        "Dismissal for Duck (BAT/WK/AR)" to "-2 pts"
                    )
                )
            }

            // Bowling Points
            item {
                PointsCategoryCard(
                    title = "Bowling Points",
                    icon = Icons.Default.SportsBaseball,
                    iconColor = PrimaryRed,
                    rules = listOf(
                        "Wicket (Excl. Run Out)" to "+25 pts",
                        "Bowled / LBW Bonus" to "+8 pts",
                        "3-Wicket Haul Bonus" to "+4 pts",
                        "4-Wicket Haul Bonus" to "+8 pts",
                        "5-Wicket Haul Bonus" to "+16 pts",
                        "Maiden Over" to "+12 pts"
                    )
                )
            }

            // Fielding Points
            item {
                PointsCategoryCard(
                    title = "Fielding Points",
                    icon = Icons.Default.FrontHand,
                    iconColor = PitchGreen,
                    rules = listOf(
                        "Catch" to "+8 pts",
                        "3-Catch Bonus in a match" to "+4 pts",
                        "Stumping (WK)" to "+12 pts",
                        "Direct Hit Run-Out" to "+12 pts",
                        "Run-Out Thrower/Catcher" to "+6 pts each"
                    )
                )
            }

            // Economy Rate
            item {
                PointsCategoryCard(
                    title = "Economy Rate (Min 2 Overs)",
                    icon = Icons.Default.Speed,
                    iconColor = SkyAccent,
                    rules = listOf(
                        "Below 5.0 runs per over" to "+6 pts",
                        "Between 5.0 - 5.99 runs/over" to "+4 pts",
                        "Between 6.0 - 7.0 runs/over" to "+2 pts",
                        "Between 10.0 - 11.0 runs/over" to "-2 pts",
                        "Above 11.0 runs per over" to "-6 pts"
                    )
                )
            }
        }
    }
}

@Composable
private fun PointsCategoryCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    rules: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
            }

            rules.forEach { (action, pts) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = action, fontSize = 13.sp, color = TextSecondary)
                    Text(text = pts, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (pts.startsWith("-")) PrimaryRed else PitchGreen)
                }
            }
        }
    }
}
