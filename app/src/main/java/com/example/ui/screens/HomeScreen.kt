package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewCarousel
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.ui.components.GlassCard
import com.example.ui.components.HeroGradientCard
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

data class FeatureCardItem(
    val id: String,
    val title: String,
    val hindiTitle: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTint: Color,
    val route: String
)

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val stats by mainViewModel.userStats.collectAsState()
    val settings by mainViewModel.settings.collectAsState()

    val featureCards = listOf(
        FeatureCardItem(
            id = "speaking",
            title = "Speaking Practice",
            hindiTitle = "बोलने का अभ्यास",
            subtitle = "Practice talking with AI Teacher",
            icon = Icons.Default.RecordVoiceOver,
            iconBgColor = IndigoContainer,
            iconTint = IndigoPrimary,
            route = "speaking_practice"
        ),
        FeatureCardItem(
            id = "translation",
            title = "Hindi to English",
            hindiTitle = "हिंदी से अंग्रेज़ी अनुवाद",
            subtitle = "Natural translation with grammar",
            icon = Icons.Default.Translate,
            iconBgColor = CyanContainer,
            iconTint = CyanAccent,
            route = "translation"
        ),
        FeatureCardItem(
            id = "flashcards",
            title = "Flash Cards",
            hindiTitle = "फ्लैश कार्ड्स",
            subtitle = "3D flip cards with sentence tips",
            icon = Icons.Default.ViewCarousel,
            iconBgColor = PinkContainer,
            iconTint = PinkAccent,
            route = "flashcards"
        ),
        FeatureCardItem(
            id = "vocabulary",
            title = "Vocabulary Builder",
            hindiTitle = "शब्दावली बिल्डर",
            subtitle = "10 daily words with Hindi audio",
            icon = Icons.Default.MenuBook,
            iconBgColor = Color(0xFFFEF3C7),
            iconTint = Color(0xFFD97706),
            route = "vocabulary"
        ),
        FeatureCardItem(
            id = "daily_questions",
            title = "Daily Questions",
            hindiTitle = "दैनिक प्रश्न",
            subtitle = "5 speaking questions daily",
            icon = Icons.Default.FitnessCenter,
            iconBgColor = Color(0xFFE0E7FF),
            iconTint = Color(0xFF4338CA),
            route = "daily_questions"
        ),
        FeatureCardItem(
            id = "grammar",
            title = "Grammar Practice",
            hindiTitle = "व्याकरण सुधार",
            subtitle = "Instant correction & Hindi rules",
            icon = Icons.Default.Spellcheck,
            iconBgColor = Color(0xFFDCFCE7),
            iconTint = SuccessGreen,
            route = "grammar"
        ),
        FeatureCardItem(
            id = "conversation",
            title = "AI Conversation",
            hindiTitle = "वास्तविक बातचीत",
            subtitle = "Restaurant, Interview & Friends",
            icon = Icons.Default.ChatBubble,
            iconBgColor = Color(0xFFEDE9FE),
            iconTint = Color(0xFF7C3AED),
            route = "conversation"
        ),
        FeatureCardItem(
            id = "pronunciation",
            title = "Pronunciation",
            hindiTitle = "उच्चारण अभ्यास",
            subtitle = "Listen, record & fix tough words",
            icon = Icons.Default.Mic,
            iconBgColor = Color(0xFFFFE4E6),
            iconTint = Color(0xFFE11D48),
            route = "pronunciation"
        ),
        FeatureCardItem(
            id = "quiz",
            title = "Quick Match Quiz",
            hindiTitle = "क्विज़ खेलें",
            subtitle = "Interactive multiple choice quiz",
            icon = Icons.Default.Quiz,
            iconBgColor = Color(0xFFF3E8FF),
            iconTint = Color(0xFF9333EA),
            route = "quiz"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
            .testTag("home_screen_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Personalized Greeting
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "नमस्ते, ${settings.userName} 👋",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 22.sp
                        )
                    )
                    Text(
                        text = "Speak English with Confidence.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.clickable { onNavigate("analytics") }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = "Streak",
                            tint = StreakFire,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${stats?.streakDays ?: 4} Days",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }
                }
            }
        }

        // 2. Daily Goal Progress Card
        item {
            val totalMinutes = stats?.totalMinutes ?: 45
            val goal = settings.dailyGoalMinutes
            val progressPercent = ((totalMinutes.toFloat() / goal.toFloat()) * 100).toInt().coerceAtMost(100)

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = IndigoContainer,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = IndigoPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Today's Learning Goal",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "$totalMinutes / $goal mins practiced",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        Text(
                            text = "$progressPercent%",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (progressPercent >= 100) SuccessGreen else IndigoPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { (progressPercent / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (progressPercent >= 100) SuccessGreen else IndigoPrimary,
                        trackColor = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        QuickStatItem(
                            value = "${stats?.wordsLearnedCount ?: 28}",
                            label = "Words Learned"
                        )
                        QuickStatItem(
                            value = "${stats?.totalSessions ?: 6}",
                            label = "Sessions"
                        )
                        QuickStatItem(
                            value = "${stats?.totalQuizScore ?: 85}",
                            label = "Quiz Score"
                        )
                    }
                }
            }
        }

        // 3. Hero Card: AI Speaking Practice
        item {
            HeroGradientCard(
                title = "AI Speaking Teacher",
                subtitle = "Practice conversational English with voice. Speak freely and get gentle Hindi explanations.",
                badgeText = "AI GEMINI POWERED",
                buttonText = "Start Speaking Now",
                onButtonClick = { onNavigate("speaking_practice") }
            )
        }

        // 4. Motivational Message
        item {
            val quote = SampleData.dailyMotivationalTips.first()
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFAF5FF),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF9333EA),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = quote.first,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF581C87),
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = quote.second,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF7E22CE),
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // 5. Practice Cards Title
        item {
            Text(
                text = "Core Learning Modules",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
        }

        // Grid of items: Pair them up 2 by 2
        items(count = (featureCards.size + 1) / 2) { rowIndex ->
            val first = featureCards[rowIndex * 2]
            val second = featureCards.getOrNull(rowIndex * 2 + 1)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureCard(
                    item = first,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(first.route) }
                )
                if (second != null) {
                    FeatureCard(
                        item = second,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(second.route) }
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun QuickStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun FeatureCard(
    item: FeatureCardItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .testTag("feature_card_${item.id}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = item.iconBgColor,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = item.iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 14.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.hindiTitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = IndigoPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
