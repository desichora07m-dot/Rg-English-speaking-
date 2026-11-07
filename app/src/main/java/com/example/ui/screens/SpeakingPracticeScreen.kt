package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
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
import com.example.model.ChatMessage
import com.example.ui.components.AppHeader
import com.example.ui.components.AudioPulseButton
import com.example.ui.components.GlassCard
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.theme.BackgroundLavender
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AiLearningViewModel
import com.example.viewmodel.MainViewModel

@Composable
fun SpeakingPracticeScreen(
    mainViewModel: MainViewModel,
    aiViewModel: AiLearningViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val speechManager = mainViewModel.speechManager
    val isListening by speechManager.isListening.collectAsState()
    val recognizedText by speechManager.recognizedText.collectAsState()
    val micError by speechManager.errorMessage.collectAsState()
    val isEvaluating by aiViewModel.isEvaluatingSpeaking.collectAsState()
    val messages by aiViewModel.chatMessages.collectAsState()
    val latestEval by aiViewModel.latestSpeakingEval.collectAsState()
    val settings by mainViewModel.settings.collectAsState()

    var manualInputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Permission launcher for microphone
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            speechManager.startListening()
        }
    }

    fun handleMicClick() {
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

    // Auto-send when speech completes
    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            aiViewModel.selectScenario(aiViewModel.currentScenario.value, speechManager)
        }
        speechManager.onSpeechComplete = { text ->
            if (text.isNotBlank()) {
                aiViewModel.sendSpeechMessage(
                    userText = text,
                    speechManager = speechManager,
                    settings = settings,
                    onSessionComplete = {
                        mainViewModel.recordPractice(
                            activityType = "Speaking Practice",
                            summary = "Spoke: $text",
                            minutes = 3
                        )
                    }
                )
            }
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        AppHeader(
            title = "AI Speaking Teacher",
            subtitle = "Priya Ma'am - Voice & Hindi Support",
            showBackButton = true,
            onBackClick = onBack,
            actions = {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = IndigoContainer,
                    modifier = Modifier.clickable {
                        aiViewModel.selectScenario(aiViewModel.currentScenario.value, speechManager)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Restart",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Restart",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IndigoPrimary
                            )
                        )
                    }
                }
            }
        )

        // Main Conversation Chat List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                ConversationBubble(
                    message = msg,
                    onSpeak = { speechManager.speak(it) }
                )
            }

            if (isEvaluating) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = IndigoPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Priya Ma'am is listening and preparing response...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        )
                    }
                }
            }

            // Latest correction guidance banner
            if (latestEval?.userMistake != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFEFCE8),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEF08A)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFFCA8A04),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "💡 Gentle Speaking Correction / सुधार:",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF854D0E)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Natural Sentence: \"${latestEval?.correctedSentence}\"",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF713F12)
                                )
                            )

                            if (!latestEval?.hindiExplanation.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "हिंदी में समझें: ${latestEval?.hindiExplanation}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF854D0E)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Now try saying the corrected sentence!",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IndigoPrimary
                                    )
                                )

                                Button(
                                    onClick = { speechManager.speak(latestEval?.correctedSentence.orEmpty()) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Listen",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Live Speech / Mic State Banner
        if (isListening || recognizedText.isNotEmpty()) {
            Surface(
                color = if (isListening) Color(0xFFEFF6FF) else Color(0xFFF0FDF4),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        tint = if (isListening) Color(0xFFEF4444) else SuccessGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isListening) "Listening: ${recognizedText.ifEmpty { "Speak in English..." }}" else "Recognized: $recognizedText",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (micError != null) {
            Surface(
                color = Color(0xFFFEF2F2),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "⚠️ $micError",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFB91C1C)),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // Bottom Controls: Microphone & Manual Text Fallback
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
                // Large Microphone Button
                AudioPulseButton(
                    isListening = isListening,
                    onToggle = { handleMicClick() }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isListening) "Tap to Stop Speaking" else "Tap Mic to Speak English",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isListening) Color(0xFFEF4444) else TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Manual text fallback field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = manualInputText,
                        onValueChange = { manualInputText = it },
                        placeholder = { Text("Or type your answer here...", fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("speaking_manual_input"),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IndigoPrimary,
                            unfocusedBorderColor = BorderLight,
                            focusedContainerColor = Color(0xFFF8FAFC),
                            unfocusedContainerColor = Color(0xFFF8FAFC)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (manualInputText.isNotBlank()) {
                                val text = manualInputText.trim()
                                manualInputText = ""
                                aiViewModel.sendSpeechMessage(
                                    userText = text,
                                    speechManager = speechManager,
                                    settings = settings,
                                    onSessionComplete = {
                                        mainViewModel.recordPractice(
                                            activityType = "Speaking Practice",
                                            summary = "Typed: $text",
                                            minutes = 2
                                        )
                                    }
                                )
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(IndigoPrimary)
                            .testTag("speaking_send_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationBubble(
    message: ChatMessage,
    onSpeak: (String) -> Unit
) {
    val isUser = message.sender == "user"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            if (!isUser) {
                Surface(
                    shape = CircleShape,
                    color = IndigoPrimary,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(bottom = 4.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "AI",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
            }

            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
                color = if (isUser) IndigoPrimary else Color.White,
                shadowElevation = 1.dp,
                border = if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, BorderLight) else null,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isUser) Color.White else TextPrimary,
                            fontSize = 14.sp
                        )
                    )

                    if (!isUser) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TtsSpeakerButton(
                                onSpeak = { onSpeak(message.text) }
                            )
                        }
                    }
                }
            }
        }
    }
}
