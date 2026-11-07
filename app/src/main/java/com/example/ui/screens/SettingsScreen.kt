package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppHeader
import com.example.ui.components.GlassCard
import com.example.ui.theme.BackgroundLavender
import com.example.ui.theme.BorderLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    mainViewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val settings by mainViewModel.settings.collectAsState()

    var userName by remember(settings.userName) { mutableStateOf(settings.userName) }
    var difficulty by remember(settings.difficultyLevel) { mutableStateOf(settings.difficultyLevel) }
    var explanationLang by remember(settings.explanationLanguage) { mutableStateOf(settings.explanationLanguage) }
    var autoPlayTts by remember(settings.autoPlayTts) { mutableStateOf(settings.autoPlayTts) }
    var speechRate by remember(settings.speechRate) { mutableFloatStateOf(settings.speechRate) }
    var dailyGoal by remember(settings.dailyGoalMinutes) { mutableIntStateOf(settings.dailyGoalMinutes) }

    var showResetDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var importJsonText by remember { mutableStateOf("") }

    fun saveChanges() {
        mainViewModel.updateUserSettings(
            settings.copy(
                userName = userName.ifBlank { "Rahul" },
                difficultyLevel = difficulty,
                explanationLanguage = explanationLang,
                autoPlayTts = autoPlayTts,
                speechRate = speechRate,
                dailyGoalMinutes = dailyGoal
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        AppHeader(
            title = "Settings",
            subtitle = "Personalize Your Learning Experience",
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
            // Profile & Name Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.White
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = IndigoPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Learner Profile",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Your Name (Shown in Greetings):",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = userName,
                            onValueChange = {
                                userName = it
                                saveChanges()
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("settings_username_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = BorderLight
                            )
                        )
                    }
                }
            }

            // Learning Difficulty & Language
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.White
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, contentDescription = null, tint = IndigoPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Difficulty & Language",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Proficiency Level:",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Beginner", "Intermediate", "Advanced").forEach { lvl ->
                                FilterChip(
                                    selected = difficulty == lvl,
                                    onClick = {
                                        difficulty = lvl
                                        saveChanges()
                                    },
                                    label = { Text(lvl, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IndigoPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "AI Explanation Language:",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Hinglish", "Simple Hindi", "English").forEach { lang ->
                                FilterChip(
                                    selected = explanationLang == lang,
                                    onClick = {
                                        explanationLang = lang
                                        saveChanges()
                                    },
                                    label = { Text(lang, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IndigoPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Speech & TTS Settings
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.White
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = IndigoPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Voice & Speech Settings",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Auto-play Voice Responses",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, color = TextPrimary)
                                )
                                Text(
                                    text = "AI teacher will automatically speak responses aloud",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                                )
                            }
                            Switch(
                                checked = autoPlayTts,
                                onCheckedChange = {
                                    autoPlayTts = it
                                    saveChanges()
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = IndigoPrimary)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Teacher Speech Speed: ${(speechRate * 10).toInt() / 10f}x",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )

                        Slider(
                            value = speechRate,
                            onValueChange = {
                                speechRate = it
                                saveChanges()
                            },
                            valueRange = 0.6f..1.3f,
                            steps = 6,
                            colors = SliderDefaults.colors(thumbColor = IndigoPrimary, activeTrackColor = IndigoPrimary)
                        )
                    }
                }
            }

            // Daily Goal Setting
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.White
                ) {
                    Column {
                        Text(
                            text = "Daily Practice Goal: $dailyGoal Minutes",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Slider(
                            value = dailyGoal.toFloat(),
                            onValueChange = {
                                dailyGoal = it.toInt()
                                saveChanges()
                            },
                            valueRange = 5f..60f,
                            steps = 10,
                            colors = SliderDefaults.colors(thumbColor = IndigoPrimary, activeTrackColor = IndigoPrimary)
                        )
                    }
                }
            }

            // Backup & Data Sync
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.White
                ) {
                    Column {
                        Text(
                            text = "Data & Backup",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val json = mainViewModel.preferencesManager.exportSettingsJson()
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Backup", json))
                                    Toast.makeText(context, "Progress settings copied to clipboard!", Toast.LENGTH_LONG).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Upload, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Export Backup", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { showImportDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Import Backup", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { showResetDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reset_progress_btn")
                        ) {
                            Icon(Icons.Default.RestartAlt, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reset Learning Progress", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog for Reset Progress
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Progress?") },
            text = { Text("Are you sure you want to reset all streak days, practice minutes, learned words, and quiz scores? This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        mainViewModel.resetAllProgress()
                        showResetDialog = false
                        Toast.makeText(context, "All progress has been reset.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Yes, Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Import JSON Dialog
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("Import Settings Backup") },
            text = {
                Column {
                    Text("Paste JSON settings text here:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = importJsonText,
                        onValueChange = { importJsonText = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 6
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = mainViewModel.preferencesManager.importSettingsFromJson(importJsonText)
                        if (success) {
                            Toast.makeText(context, "Settings imported successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Invalid JSON format.", Toast.LENGTH_SHORT).show()
                        }
                        showImportDialog = false
                    }
                ) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
