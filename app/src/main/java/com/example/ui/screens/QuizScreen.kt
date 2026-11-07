package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppHeader
import com.example.ui.components.GlassCard
import com.example.ui.theme.BackgroundLavender
import com.example.ui.theme.BorderLight
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AiLearningViewModel
import com.example.viewmodel.MainViewModel

@Composable
fun QuizScreen(
    mainViewModel: MainViewModel,
    aiViewModel: AiLearningViewModel,
    onBack: () -> Unit
) {
    val questions by aiViewModel.quizQuestions.collectAsState()
    val currentIndex by aiViewModel.currentQuizIndex.collectAsState()
    val selectedOption by aiViewModel.selectedOption.collectAsState()
    val isConfirmed by aiViewModel.isOptionConfirmed.collectAsState()
    val score by aiViewModel.quizScore.collectAsState()
    val isFinished by aiViewModel.isQuizFinished.collectAsState()

    val currentQ = questions.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        AppHeader(
            title = "Quick Match Quiz",
            subtitle = "Score: $score pts",
            showBackButton = true,
            onBackClick = onBack
        )

        if (isFinished) {
            // Final Results Card
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Quiz Completed! 🎉",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You scored $score out of ${questions.size * 20} points",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                val feedback = when {
                    score >= 80 -> "शानदार प्रदर्शन! आपका व्याकरण और शब्दावली बहुत अच्छी है।"
                    score >= 40 -> "अच्छा प्रयास! रोज़ अभ्यास से आप और बेहतर होंगे।"
                    else -> "कोई बात नहीं! ग़लतियों से ही सीखते हैं। दोबारा कोशिश करें।"
                }

                Text(
                    text = feedback,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        mainViewModel.recordQuizScore(score)
                        aiViewModel.restartQuiz("Mixed English Practice")
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .testTag("retry_quiz_btn")
                ) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Play Again", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        } else if (currentQ != null) {
            // Progress Indicator
            LinearProgressIndicator(
                progress = { ((currentIndex + 1).toFloat() / questions.size.toFloat().coerceAtLeast(1f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = IndigoPrimary,
                trackColor = Color(0xFFE2E8F0)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Question Header Card
                item {
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
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = IndigoContainer
                                ) {
                                    Text(
                                        text = currentQ.questionType,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = IndigoPrimary
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Text(
                                    text = "Question ${currentIndex + 1}/${questions.size}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = currentQ.question,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }
                }

                // 4 Options
                items(currentQ.options.size) { optIndex ->
                    val optionText = currentQ.options[optIndex]
                    val isSelected = selectedOption == optIndex
                    val isCorrect = optIndex == currentQ.correctIndex

                    val cardBgColor = when {
                        !isConfirmed && isSelected -> IndigoContainer
                        isConfirmed && isCorrect -> Color(0xFFDCFCE7)
                        isConfirmed && isSelected && !isCorrect -> Color(0xFFFEE2E2)
                        else -> Color.White
                    }

                    val borderColor = when {
                        !isConfirmed && isSelected -> IndigoPrimary
                        isConfirmed && isCorrect -> SuccessGreen
                        isConfirmed && isSelected && !isCorrect -> Color(0xFFEF4444)
                        else -> BorderLight
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBgColor),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isConfirmed) {
                                aiViewModel.selectQuizOption(optIndex)
                            }
                            .testTag("quiz_option_$optIndex")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) IndigoPrimary else Color(0xFFF1F5F9),
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = ('A'.code + optIndex).toChar().toString(),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) Color.White else TextPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = TextPrimary,
                                        fontSize = 15.sp
                                    )
                                )
                            }

                            if (isConfirmed) {
                                if (isCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Correct",
                                        tint = SuccessGreen
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Incorrect",
                                        tint = Color(0xFFEF4444)
                                    )
                                }
                            }
                        }
                    }
                }

                // Hindi Explanation Card after answering
                if (isConfirmed) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFAF5FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "💡 Hindi Explanation / कारण:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6B21A8)
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentQ.explanationHindi,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF581C87),
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }

                // Action Buttons: Confirm or Next
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (!isConfirmed) {
                        Button(
                            onClick = { aiViewModel.confirmQuizAnswer() },
                            enabled = selectedOption != null,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("quiz_confirm_btn")
                        ) {
                            Text("Confirm Answer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    } else {
                        Button(
                            onClick = {
                                aiViewModel.nextQuizQuestion()
                                if (currentIndex == questions.size - 1) {
                                    mainViewModel.recordQuizScore(score)
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("quiz_next_btn")
                        ) {
                            Text(
                                if (currentIndex < questions.size - 1) "Next Question →" else "See Results 🏆",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
