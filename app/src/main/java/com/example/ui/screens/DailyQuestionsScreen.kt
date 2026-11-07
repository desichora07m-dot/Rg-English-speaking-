package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.AppHeader
import com.example.ui.components.AudioPulseButton
import com.example.ui.components.GlassCard
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.theme.BackgroundLavender
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PinkContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AiLearningViewModel
import com.example.viewmodel.MainViewModel

@Composable
fun DailyQuestionsScreen(
    mainViewModel: MainViewModel,
    aiViewModel: AiLearningViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val speechManager = mainViewModel.speechManager
    val isListening by speechManager.isListening.collectAsState()
    val recognizedText by speechManager.recognizedText.collectAsState()
    val micError by speechManager.errorMessage.collectAsState()

    val categories = aiViewModel.dailyCategories
    val selectedCat by aiViewModel.selectedCategory.collectAsState()
    val questions by aiViewModel.dailyQuestions.collectAsState()
    val currentIndex by aiViewModel.currentQuestionIndex.collectAsState()
    val isLoading by aiViewModel.isLoadingQuestions.collectAsState()
    val isEvaluating by aiViewModel.isEvaluatingSpeaking.collectAsState()
    val currentEval by aiViewModel.questionEval.collectAsState()
    val settings by mainViewModel.settings.collectAsState()

    var manualText by remember { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) speechManager.startListening()
    }

    LaunchedEffect(Unit) {
        speechManager.onSpeechComplete = { text ->
            if (text.isNotBlank()) {
                aiViewModel.evaluateQuestionAnswer(text, settings)
                mainViewModel.recordPractice("Daily Question", "Answered: $text", minutes = 2)
            }
        }
    }

    val currentQ = questions.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        AppHeader(
            title = "Daily Speaking Questions",
            subtitle = "5 Questions to Speak Confidently",
            showBackButton = true,
            onBackClick = onBack
        )

        // Category Horizontal Scroll
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = cat == selectedCat
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) IndigoPrimary else Color.White,
                    shadowElevation = 1.dp,
                    border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, BorderLight) else null,
                    modifier = Modifier.clickable { aiViewModel.selectCategory(cat) }
                ) {
                    Text(
                        text = cat,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextPrimary
                        ),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // Progress bar for the 5 questions
        LinearProgressIndicator(
            progress = { ((currentIndex + 1).toFloat() / questions.size.toFloat().coerceAtLeast(1f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = IndigoPrimary,
            trackColor = Color(0xFFE2E8F0)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Question Card
            if (currentQ != null) {
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
                                    shape = RoundedCornerShape(10.dp),
                                    color = IndigoContainer
                                ) {
                                    Text(
                                        text = "Question ${currentIndex + 1} of ${questions.size}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = IndigoPrimary,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                TtsSpeakerButton(
                                    onSpeak = { speechManager.speak(currentQ.questionEnglish) }
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = currentQ.questionEnglish,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 18.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "हिंदी अर्थ: ${currentQ.questionHindi}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF0FDF4),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "💡 Tip: ${currentQ.tipForAnswering}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF166534),
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
            }

            // AI Evaluation Result
            if (currentEval != null) {
                val eval = currentEval!!
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color(0xFFFEFCE8),
                        borderColor = Color(0xFFFEF08A)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFFCA8A04),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AI Teacher Feedback",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF854D0E)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = eval.aiReply,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF713F12)
                                )
                            )

                            if (!eval.correctedSentence.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "More Natural Phrasing:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF854D0E)
                                    )
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "\"${eval.correctedSentence}\"",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = IndigoPrimary
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                    TtsSpeakerButton(
                                        onSpeak = { speechManager.speak(eval.correctedSentence) }
                                    )
                                }
                            }

                            if (!eval.hindiExplanation.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "हिंदी में सीख: ${eval.hindiExplanation}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF854D0E)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (currentIndex < questions.size - 1) {
                                Button(
                                    onClick = { aiViewModel.nextDailyQuestion() },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Next Question", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            if (isEvaluating) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = IndigoPrimary, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Evaluating your spoken answer...", fontSize = 13.sp, color = TextSecondary)
                    }
                }
            }
        }

        // Live Mic & Input Bar
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AudioPulseButton(
                    isListening = isListening,
                    onToggle = {
                        if (isListening) {
                            speechManager.stopListening()
                        } else {
                            val permission = Manifest.permission.RECORD_AUDIO
                            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                speechManager.startListening()
                            } else {
                                permissionLauncher.launch(permission)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isListening) "Listening... Speak your answer" else "Tap Mic to Answer",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isListening) Color(0xFFEF4444) else TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = manualText,
                        onValueChange = { manualText = it },
                        placeholder = { Text("Or type answer...", fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (manualText.isNotBlank()) {
                                val t = manualText.trim()
                                manualText = ""
                                aiViewModel.evaluateQuestionAnswer(t, settings)
                                mainViewModel.recordPractice("Daily Question", "Typed answer: $t", minutes = 2)
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(IndigoPrimary)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}
