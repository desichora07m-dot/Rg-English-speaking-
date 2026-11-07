package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.AppHeader
import com.example.ui.components.AudioPulseButton
import com.example.ui.components.GlassCard
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.theme.BackgroundLavender
import com.example.ui.theme.BorderLight
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AiLearningViewModel
import com.example.viewmodel.MainViewModel

@Composable
fun PronunciationScreen(
    mainViewModel: MainViewModel,
    aiViewModel: AiLearningViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val speechManager = mainViewModel.speechManager
    val isListening by speechManager.isListening.collectAsState()
    val recognizedText by speechManager.recognizedText.collectAsState()

    val pronList by aiViewModel.pronunciationList.collectAsState()
    val currentIndex by aiViewModel.currentPronIndex.collectAsState()
    val evaluation by aiViewModel.pronEvaluation.collectAsState()

    val currentChallenge = pronList.getOrNull(currentIndex)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) speechManager.startListening()
    }

    LaunchedEffect(Unit) {
        speechManager.onSpeechComplete = { text ->
            if (text.isNotBlank()) {
                aiViewModel.evaluatePronunciation(text)
                mainViewModel.recordPractice(
                    activityType = "Pronunciation",
                    summary = "Practiced: ${currentChallenge?.wordOrPhrase}",
                    minutes = 2
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        AppHeader(
            title = "Pronunciation Practice",
            subtitle = "Master Tough Words with Native Accent",
            showBackButton = true,
            onBackClick = onBack
        )

        // Progress indicator
        LinearProgressIndicator(
            progress = { ((currentIndex + 1).toFloat() / pronList.size.toFloat().coerceAtLeast(1f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = IndigoPrimary,
            trackColor = Color(0xFFE2E8F0)
        )

        if (currentChallenge != null) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Challenge Card
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pronunciation_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFEFF6FF)
                            ) {
                                Text(
                                    text = "Challenge ${currentIndex + 1} of ${pronList.size} • ${currentChallenge.difficulty}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = IndigoPrimary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = currentChallenge.wordOrPhrase,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 28.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = currentChallenge.phoneticGuide,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = IndigoPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Normal & Slow audio listen buttons
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        speechManager.setSpeechRate(0.9f)
                                        speechManager.speak(currentChallenge.wordOrPhrase)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Listen Normal", fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        speechManager.setSpeechRate(0.65f)
                                        speechManager.speak(currentChallenge.wordOrPhrase)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                                ) {
                                    Icon(Icons.Default.Speed, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Listen Slow", color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Common Mistake Alert
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFEF2F2),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "❌ Common Mispronunciation: \"${currentChallenge.commonMistake}\"",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF991B1B),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Hindi Pronunciation Tip
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFFAF5FF),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Hearing,
                                            contentDescription = null,
                                            tint = Color(0xFF7E22CE),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "💡 Hindi Speaking Tip:",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF6B21A8)
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentChallenge.hindiTip,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF581C87),
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Evaluation / Match Result
                if (evaluation != null) {
                    val (scorePercent, feedback) = evaluation!!
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = if (scorePercent >= 75) SuccessContainer else Color(0xFFFFFBEB),
                            borderColor = if (scorePercent >= 75) Color(0xFF86EFAC) else Color(0xFFFDE68A)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Accuracy: $scorePercent%",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (scorePercent >= 75) SuccessGreen else Color(0xFFD97706)
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = feedback,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        textAlign = TextAlign.Center,
                                        color = TextPrimary
                                    )
                                )

                                if (recognizedText.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "We heard: \"$recognizedText\"",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Mic & Nav
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AudioPulseButton(
                        isListening = isListening,
                        onToggle = {
                            if (isListening) {
                                speechManager.stopListening()
                            } else {
                                val perm = Manifest.permission.RECORD_AUDIO
                                if (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED) {
                                    speechManager.startListening()
                                } else {
                                    permissionLauncher.launch(perm)
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isListening) "Listening... Pronounce now" else "Tap Mic to Test Pronunciation",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isListening) Color(0xFFEF4444) else TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { aiViewModel.prevPronunciationChallenge() },
                            enabled = currentIndex > 0,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = TextPrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Prev Word", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { aiViewModel.nextPronunciationChallenge() },
                            enabled = currentIndex < pronList.size - 1,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Next Word", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}
