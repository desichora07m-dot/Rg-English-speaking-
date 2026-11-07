package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppHeader
import com.example.ui.components.GlassCard
import com.example.ui.components.TtsSpeakerButton
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
fun GrammarScreen(
    mainViewModel: MainViewModel,
    aiViewModel: AiLearningViewModel,
    onBack: () -> Unit
) {
    val input by aiViewModel.grammarInput.collectAsState()
    val isChecking by aiViewModel.isCheckingGrammar.collectAsState()
    val result by aiViewModel.grammarResult.collectAsState()
    val settings by mainViewModel.settings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        AppHeader(
            title = "Grammar Correction",
            subtitle = "AI Mistake Detection & Hindi Rules",
            showBackButton = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input Box
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
                            Text(
                                text = "Enter English Sentence to Check:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )

                            if (input.isNotEmpty()) {
                                IconButton(
                                    onClick = { aiViewModel.updateGrammarInput("") },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = input,
                            onValueChange = { aiViewModel.updateGrammarInput(it) },
                            placeholder = { Text("Example: She go to school every day.") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("grammar_input_field"),
                            minLines = 2,
                            maxLines = 4,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = BorderLight
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    aiViewModel.checkGrammar(settings.explanationLanguage)
                                    mainViewModel.recordPractice(
                                        activityType = "Grammar",
                                        summary = "Checked: $input",
                                        minutes = 2
                                    )
                                },
                                enabled = input.isNotBlank() && !isChecking,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("check_grammar_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                            ) {
                                if (isChecking) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Icon(Icons.Default.Spellcheck, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Analyze with AI", fontWeight = FontWeight.Bold)
                                }
                            }

                            if (input.isEmpty()) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = IndigoContainer,
                                    modifier = Modifier.testTag("grammar_sample_btn")
                                ) {
                                    IconButton(
                                        onClick = {
                                            aiViewModel.updateGrammarInput("She go to school every day.")
                                            aiViewModel.checkGrammar(settings.explanationLanguage)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = "Sample",
                                            tint = IndigoPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Results Card
            if (result != null) {
                val res = result!!
                item {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("grammar_result_card"),
                        backgroundColor = Color.White
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SuccessContainer
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = SuccessGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Grammar Analysis",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = SuccessGreen
                                            )
                                        )
                                    }
                                }

                                TtsSpeakerButton(
                                    onSpeak = { mainViewModel.speechManager.speak(res.correctedSentence) }
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Comparison: Original vs Correct
                            Text(
                                text = "Original Sentence:",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                            Text(
                                text = res.originalSentence,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFFEF4444),
                                    fontWeight = FontWeight.Medium
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Correct Sentence:",
                                style = MaterialTheme.typography.labelSmall.copy(color = SuccessGreen, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = res.correctedSentence,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Hindi Explanation
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFAF5FF),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = Color(0xFF7E22CE),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "नियम (Rule): ${res.grammarRule}",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF581C87)
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = res.hindiExplanation,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF6B21A8),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }

                            // Examples
                            if (res.examples.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "More Examples:",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                res.examples.forEach { ex ->
                                    Text(
                                        text = "• $ex",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary
                                        ),
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }

                            // Practice Question
                            if (res.practiceQuestion.isNotBlank()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFFFBEB),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.HelpOutline,
                                            contentDescription = null,
                                            tint = Color(0xFFD97706),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "Try it yourself (अभ्यास प्रश्न):",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF92400E)
                                                )
                                            )
                                            Text(
                                                text = res.practiceQuestion,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF78350F)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
