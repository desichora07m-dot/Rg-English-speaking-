package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PracticeHistoryEntity
import com.example.ui.components.AppHeader
import com.example.ui.components.GlassCard
import com.example.ui.theme.BackgroundLavender
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PinkContainer
import com.example.ui.theme.StreakFire
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

@Composable
fun AnalyticsScreen(
    mainViewModel: MainViewModel,
    onBack: () -> Unit
) {
    val stats by mainViewModel.userStats.collectAsState()
    val history by mainViewModel.practiceHistory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        AppHeader(
            title = "Learning Progress",
            subtitle = "Streak, Analytics & Activity History",
            showBackButton = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 4-Grid Big Metric Cards
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MetricCard(
                            title = "Daily Streak",
                            value = "${stats?.streakDays ?: 4} Days",
                            hindiSubtitle = "लगातार अभ्यास",
                            icon = Icons.Default.Whatshot,
                            iconColor = StreakFire,
                            iconBg = Color(0xFFFFF3E0),
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "Practice Time",
                            value = "${stats?.totalMinutes ?: 45} mins",
                            hindiSubtitle = "कुल अभ्यास समय",
                            icon = Icons.Default.Timer,
                            iconColor = IndigoPrimary,
                            iconBg = IndigoContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MetricCard(
                            title = "Words Learned",
                            value = "${stats?.wordsLearnedCount ?: 28} words",
                            hindiSubtitle = "सीखे गए शब्द",
                            icon = Icons.Default.MenuBook,
                            iconColor = PinkAccent,
                            iconBg = PinkContainer,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "Quiz Points",
                            value = "${stats?.totalQuizScore ?: 85} pts",
                            hindiSubtitle = "क्विज़ स्कोर",
                            icon = Icons.Default.EmojiEvents,
                            iconColor = Color(0xFFD97706),
                            iconBg = Color(0xFFFEF3C7),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Weekly Consistency Chart
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.White
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Weekly Consistency",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        val completedDays = setOf(0, 1, 2, 3) // e.g. Mon through Thu

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            days.forEachIndexed { idx, day ->
                                val isDone = completedDays.contains(idx)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (isDone) IndigoPrimary else Color(0xFFE2E8F0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (isDone) "✓" else "•",
                                            color = if (isDone) Color.White else TextSecondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isDone) IndigoPrimary else TextSecondary,
                                            fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Activity History Title
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.History, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Recent Practice Sessions",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
            }

            // History Log items
            if (history.isEmpty()) {
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color.White
                    ) {
                        Text(
                            text = "Start practicing speaking, flashcards, or quizzes to build your learning history!",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                    }
                }
            } else {
                items(history) { record ->
                    HistoryItemCard(record)
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    hindiSubtitle: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Surface(
                shape = CircleShape,
                color = iconBg,
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 18.sp
                )
            )

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            )

            Text(
                text = hindiSubtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = IndigoPrimary,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
fun HistoryItemCard(item: PracticeHistoryEntity) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = IndigoContainer
                    ) {
                        Text(
                            text = item.activityType,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IndigoPrimary,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "+${item.durationMinutes} mins",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.summary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2
                )
            }

            if (item.scoreOrDetail.isNotBlank()) {
                Text(
                    text = item.scoreOrDetail,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
