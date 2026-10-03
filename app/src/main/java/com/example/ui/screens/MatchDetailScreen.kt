package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.ContestCard
import com.example.ui.components.LiveCricketScoreCard
import com.example.ui.theme.*

@Composable
fun MatchDetailScreen(
    match: Match,
    contests: List<Contest>,
    selectedCategory: ContestCategory,
    userTeams: List<UserTeam>,
    joinedContests: List<JoinedContest>,
    guruInsight: GuruInsight,
    onBack: () -> Unit,
    onCategorySelected: (ContestCategory) -> Unit,
    onCreateTeamClick: () -> Unit,
    onJoinContest: (Contest, UserTeam) -> Unit,
    onLeaderboardClick: (Contest) -> Unit,
    onSimulateLiveClick: () -> Unit,
    onTeamPitchClick: (UserTeam) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var contestToJoin by remember { mutableStateOf<Contest?>(null) }

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

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${match.team1Code} vs ${match.team2Code}",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = match.timeString,
                                fontSize = 11.sp,
                                color = if (match.status == MatchStatus.LIVE) PrimaryRed else TrophyGold,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (match.status == MatchStatus.LIVE) {
                            Button(
                                onClick = onSimulateLiveClick,
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("simulate_ball_header_btn")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = "Simulate",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("Simulate Ball", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Secondary detail bar (Venue & Live commentary)
                    if (match.liveCommentary.isNotBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkSurfaceElevated)
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = "Commentary",
                                tint = TrophyGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = match.liveCommentary,
                                fontSize = 11.sp,
                                color = TextPrimary,
                                maxLines = 1
                            )
                        }
                    }

                    // Main Match Tabs
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = DarkSurface,
                        contentColor = PrimaryRed,
                        divider = {}
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Contests", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("My Contests (${joinedContests.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("My Teams (${userTeams.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            text = { Text("Guru Report", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = DarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (userTeams.isNotEmpty()) {
                        OutlinedButton(
                            onClick = { selectedTab = 2 },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryRed)
                        ) {
                            Text(
                                text = "My Teams (${userTeams.size})",
                                color = PrimaryRed,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onCreateTeamClick,
                        modifier = Modifier.weight(1f).testTag("create_team_bottom_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CREATE TEAM",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> {
                    // Contests Tab
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Live Score Card (if LIVE match)
                        if (match.status == MatchStatus.LIVE) {
                            item {
                                LiveCricketScoreCard(
                                    match = match,
                                    recentBalls = listOf("1", "4", "0", "6", "W", "1"),
                                    onRefreshClick = onSimulateLiveClick
                                )
                            }
                        }

                        // Category Filter Chips
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(ContestCategory.values()) { category ->
                                    val isSelected = selectedCategory == category
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { onCategorySelected(category) },
                                        label = {
                                            Text(
                                                text = category.displayName,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = PrimaryRed,
                                            selectedLabelColor = Color.White,
                                            containerColor = DarkSurfaceElevated,
                                            labelColor = TextSecondary
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = isSelected,
                                            borderColor = DarkSurfaceBorder,
                                            selectedBorderColor = PrimaryRed
                                        )
                                    )
                                }
                            }
                        }

                        items(contests, key = { it.id }) { contest ->
                            ContestCard(
                                contest = contest,
                                onJoinClick = {
                                    contestToJoin = contest
                                },
                                onLeaderboardClick = {
                                    onLeaderboardClick(contest)
                                }
                            )
                        }
                    }
                }
                1 -> {
                    // My Contests Tab
                    if (joinedContests.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Empty",
                                    tint = TextMuted,
                                    modifier = Modifier.size(56.dp)
                                )
                                Text(
                                    text = "You haven't joined any contests for this match yet",
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                                Button(
                                    onClick = { selectedTab = 0 },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Explore Contests")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(joinedContests) { jc ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = jc.contestTitle,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = TextPrimary
                                            )
                                            Surface(
                                                color = PitchGreen.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "Joined",
                                                    color = PitchGreen,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text("Team", fontSize = 11.sp, color = TextSecondary)
                                                Text(jc.teamName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                            }
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("Fantasy Points", fontSize = 11.sp, color = TextSecondary)
                                                Text("${jc.fantasyPoints} pts", fontWeight = FontWeight.Black, fontSize = 15.sp, color = TrophyGold)
                                            }
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text("Rank", fontSize = 11.sp, color = TextSecondary)
                                                Text("#${jc.currentRank}", fontWeight = FontWeight.Black, fontSize = 15.sp, color = PrimaryRed)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // My Teams Tab
                    if (userTeams.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = "No Teams",
                                    tint = TextMuted,
                                    modifier = Modifier.size(56.dp)
                                )
                                Text(
                                    text = "No teams created yet for this match.",
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                                Button(
                                    onClick = onCreateTeamClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Create Your First Team")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(userTeams) { team ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onTeamPitchClick(team) },
                                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .clip(CircleShape)
                                                        .background(PrimaryRed),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.SportsCricket,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                                Text(
                                                    text = team.teamName,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp,
                                                    color = TextPrimary
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = DarkSurfaceElevated
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Visibility,
                                                        contentDescription = "Preview",
                                                        tint = SkyAccent,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Text(
                                                        text = "Pitch View",
                                                        fontSize = 11.sp,
                                                        color = SkyAccent,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))
                                        HorizontalDivider(color = DarkSurfaceBorder, thickness = 0.6.dp)
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "11 Players Selected",
                                                fontSize = 12.sp,
                                                color = TextSecondary
                                            )
                                            Text(
                                                text = "Points: ${team.totalPoints}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TrophyGold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // Guru Report Tab
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(imageVector = Icons.Default.Stadium, contentDescription = null, tint = PitchGreen)
                                        Text("Pitch & Weather Condition", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                    }
                                    Text(text = guruInsight.pitchType, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TrophyGold)
                                    Text(text = guruInsight.avgScore, fontSize = 12.sp, color = TextSecondary)
                                    Text(text = guruInsight.paceVsSpin, fontSize = 12.sp, color = TextSecondary)
                                    Text(text = guruInsight.weather, fontSize = 12.sp, color = TextSecondary)
                                }
                            }
                        }

                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(imageVector = Icons.Default.Stars, contentDescription = null, tint = TrophyGold)
                                        Text("Top Captaincy Recommendations", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                    }
                                    guruInsight.captainRecommendations.forEach { name ->
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = PitchGreen, modifier = Modifier.size(16.dp))
                                            Text(text = "$name (2x Points multiplier)", fontSize = 13.sp, color = TextPrimary)
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = SkyAccent)
                                        Text("SK Cotrage Expert Summary", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                    }
                                    Text(text = guruInsight.expertAnalysis, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Join Contest Dialog
            if (contestToJoin != null) {
                val contest = contestToJoin!!
                AlertDialog(
                    onDismissRequest = { contestToJoin = null },
                    containerColor = DarkSurface,
                    title = {
                        Text(
                            text = "Join Contest",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = contest.title,
                                fontWeight = FontWeight.SemiBold,
                                color = TrophyGold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Entry Fee: ₹${contest.entryFee}",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (userTeams.isEmpty()) {
                                Text(
                                    text = "You don't have a team created yet for this match. Please create a team first!",
                                    color = PrimaryRed,
                                    fontSize = 12.sp
                                )
                            } else {
                                Text(
                                    text = "Select Team to Join with:",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                userTeams.forEach { team ->
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                onJoinContest(contest, team)
                                                contestToJoin = null
                                            },
                                        color = DarkSurfaceElevated,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(team.teamName, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            Text("Join ->", color = PrimaryRed, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        if (userTeams.isEmpty()) {
                            Button(
                                onClick = {
                                    contestToJoin = null
                                    onCreateTeamClick()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                            ) {
                                Text("Create Team Now")
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { contestToJoin = null }) {
                            Text("Cancel", color = TextSecondary)
                        }
                    }
                )
            }
        }
    }
}
