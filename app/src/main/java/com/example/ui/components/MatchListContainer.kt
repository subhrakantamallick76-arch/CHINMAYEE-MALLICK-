package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.model.Match
import com.example.model.MatchStatus
import com.example.ui.theme.*

/**
 * Reusable vertical list container to display multiple cricket match cards,
 * allowing users to scroll through upcoming, live, and completed fixtures with
 * search and series filter support.
 */
@Composable
fun MatchListContainer(
    matches: List<Match>,
    onMatchClick: (Match) -> Unit,
    modifier: Modifier = Modifier,
    selectedStatus: MatchStatus? = null,
    onStatusSelected: ((MatchStatus?) -> Unit)? = null,
    headerContent: (@Composable () -> Unit)? = null,
    onSimulateLiveBall: ((Match) -> Unit)? = null
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSeries by remember { mutableStateOf<String?>(null) }

    // Extract unique series names
    val allSeries = remember(matches) {
        matches.map { it.seriesName }.distinct()
    }

    // Filter matches based on status, search query, and series
    val filteredMatches = remember(matches, selectedStatus, searchQuery, selectedSeries) {
        matches.filter { match ->
            val matchesStatus = selectedStatus == null || match.status == selectedStatus
            val matchesSearch = searchQuery.isBlank() ||
                    match.team1Name.contains(searchQuery, ignoreCase = true) ||
                    match.team1Code.contains(searchQuery, ignoreCase = true) ||
                    match.team2Name.contains(searchQuery, ignoreCase = true) ||
                    match.team2Code.contains(searchQuery, ignoreCase = true) ||
                    match.seriesName.contains(searchQuery, ignoreCase = true)
            val matchesSeries = selectedSeries == null || match.seriesName == selectedSeries

            matchesStatus && matchesSearch && matchesSeries
        }
    }

    val liveCount = remember(matches) { matches.count { it.status == MatchStatus.LIVE } }
    val upcomingCount = remember(matches) { matches.count { it.status == MatchStatus.UPCOMING } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("match_list_container"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Optional Custom Header Content (e.g. Hero Banners, Promos)
        if (headerContent != null) {
            item(key = "list_header_content") {
                headerContent()
            }
        }

        // Search Bar for Fixtures
        item(key = "fixture_search_bar") {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search teams (e.g. IND, AUS, MI, CSK)...",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface,
                    focusedBorderColor = PrimaryRed,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fixture_search_input")
            )
        }

        // Status Filter Tabs (All, Live, Upcoming, Completed)
        if (onStatusSelected != null) {
            item(key = "status_filter_tabs") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("status_filter_row"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // All Fixtures Tab
                    StatusTabPill(
                        label = "ALL",
                        count = matches.size,
                        isSelected = selectedStatus == null,
                        onClick = { onStatusSelected(null) },
                        modifier = Modifier.weight(1f)
                    )

                    // Live Tab with blinking indicator
                    StatusTabPill(
                        label = "LIVE",
                        count = liveCount,
                        isSelected = selectedStatus == MatchStatus.LIVE,
                        onClick = { onStatusSelected(MatchStatus.LIVE) },
                        activeColor = PrimaryRed,
                        modifier = Modifier.weight(1f)
                    )

                    // Upcoming Tab
                    StatusTabPill(
                        label = "UPCOMING",
                        count = upcomingCount,
                        isSelected = selectedStatus == MatchStatus.UPCOMING,
                        onClick = { onStatusSelected(MatchStatus.UPCOMING) },
                        modifier = Modifier.weight(1.2f)
                    )

                    // Completed Tab
                    StatusTabPill(
                        label = "RESULTS",
                        count = matches.count { it.status == MatchStatus.COMPLETED },
                        isSelected = selectedStatus == MatchStatus.COMPLETED,
                        onClick = { onStatusSelected(MatchStatus.COMPLETED) },
                        modifier = Modifier.weight(1.1f)
                    )
                }
            }
        }

        // Series Filter Chips
        if (allSeries.size > 1) {
            item(key = "series_chips_row") {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("series_filter_row")
                ) {
                    item {
                        FilterChip(
                            selected = selectedSeries == null,
                            onClick = { selectedSeries = null },
                            label = { Text("All Leagues", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryRed,
                                selectedLabelColor = Color.White,
                                containerColor = DarkSurfaceElevated,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedSeries == null,
                                borderColor = DarkSurfaceBorder,
                                selectedBorderColor = PrimaryRed
                            )
                        )
                    }

                    items(allSeries) { series ->
                        val isSelected = selectedSeries == series
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSeries = if (isSelected) null else series },
                            label = { Text(series, fontSize = 11.sp, maxLines = 1) },
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
        }

        // Section Title & Matches Count
        item(key = "fixtures_count_header") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        selectedStatus == MatchStatus.LIVE -> "Ongoing Live Fixtures"
                        selectedStatus == MatchStatus.UPCOMING -> "Upcoming Cricket Fixtures"
                        selectedStatus == MatchStatus.COMPLETED -> "Recent Match Results"
                        selectedSeries != null -> selectedSeries!!
                        else -> "Cricket Match Fixtures"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )

                Text(
                    text = "${filteredMatches.size} ${if (filteredMatches.size == 1) "match" else "matches"}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Matches List
        if (filteredMatches.isEmpty()) {
            item(key = "empty_fixtures_state") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsCricket,
                            contentDescription = "No Matches",
                            tint = TextMuted,
                            modifier = Modifier.size(54.dp)
                        )
                        Text(
                            text = "No matches found",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (searchQuery.isNotBlank()) "No fixture matches '$searchQuery'" else "Check back soon for new scheduled matches",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        if (searchQuery.isNotBlank() || selectedSeries != null) {
                            TextButton(
                                onClick = {
                                    searchQuery = ""
                                    selectedSeries = null
                                }
                            ) {
                                Text("Reset Filters", color = PrimaryRed, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            items(filteredMatches, key = { it.id }) { match ->
                if (match.status == MatchStatus.LIVE) {
                    // Ongoing / Live Match: Render rich live cricket score card with over & ball updates!
                    LiveCricketScoreCard(
                        match = match,
                        recentBalls = listOf("1", "4", "0", "6", "W", "1"),
                        onCardClick = { onMatchClick(match) },
                        onRefreshClick = if (onSimulateLiveBall != null) {
                            { onSimulateLiveBall(match) }
                        } else null,
                        modifier = Modifier.animateItem()
                    )
                } else {
                    // Upcoming or Completed Match: Render standard match card with prize pool
                    MatchCard(
                        match = match,
                        onClick = { onMatchClick(match) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusTabPill(
    label: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = PrimaryRed
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .testTag("status_pill_${label.lowercase()}"),
        color = if (isSelected) activeColor else DarkSurface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) activeColor else DarkSurfaceBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = if (isSelected) Color.White else TextSecondary
            )
            if (count > 0 && isSelected) {
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.25f)
                ) {
                    Text(
                        text = "$count",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }
    }
}
