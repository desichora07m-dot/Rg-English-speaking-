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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VocabularyWord
import com.example.ui.components.AppHeader
import com.example.ui.components.GlassCard
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.theme.BackgroundLavender
import com.example.ui.theme.BorderLight
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PinkContainer
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

@Composable
fun VocabularyScreen(
    mainViewModel: MainViewModel,
    onBack: () -> Unit
) {
    val speechManager = mainViewModel.speechManager
    val allWords by mainViewModel.vocabularyItems.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Daily Flash, 1: All Words, 2: Saved
    var difficultyFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var currentCardIndex by remember { mutableIntStateOf(0) }

    val filteredWords = allWords.filter { word ->
        val matchesDiff = difficultyFilter == "All" || word.difficulty.equals(difficultyFilter, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                word.word.contains(searchQuery, ignoreCase = true) ||
                word.hindiMeaning.contains(searchQuery, ignoreCase = true)
        val matchesTab = when (selectedTabIndex) {
            2 -> word.isFavorite
            else -> true
        }
        matchesDiff && matchesSearch && matchesTab
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        AppHeader(
            title = "Vocabulary Builder",
            subtitle = "10 Daily Words & Pronunciation",
            showBackButton = true,
            onBackClick = onBack
        )

        // Mode Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = IndigoPrimary
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Daily Practice", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Word List (${allWords.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 },
                text = { Text("Saved ⭐", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
        }

        if (selectedTabIndex == 0) {
            // Daily Practice Card Carousel Mode
            val dailyList = allWords.take(10)
            val currentWord = dailyList.getOrNull(currentCardIndex.coerceIn(0, dailyList.size - 1))

            if (currentWord != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Progress & Counter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = IndigoContainer
                        ) {
                            Text(
                                text = "Word ${currentCardIndex + 1} of ${dailyList.size}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoPrimary
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        IconButton(
                            onClick = { mainViewModel.toggleWordFavorite(currentWord) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (currentWord.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Favorite",
                                tint = if (currentWord.isFavorite) PinkAccent else TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Large Vocabulary Card
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag("vocabulary_focus_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = "${currentWord.difficulty} • ${currentWord.partOfSpeech}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = currentWord.word,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 32.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "/ ${currentWord.pronunciation} /",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = IndigoPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Play Audio Button
                            Surface(
                                shape = CircleShape,
                                color = IndigoContainer,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clickable { speechManager.speak(currentWord.word) }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Pronounce",
                                        tint = IndigoPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFFAF5FF),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "हिंदी अर्थ (Hindi Meaning):",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF7E22CE))
                                    )
                                    Text(
                                        text = currentWord.hindiMeaning,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF581C87),
                                            fontSize = 18.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "\"${currentWord.exampleSentence}\"",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentWord.exampleHindi,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom Navigation Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (currentCardIndex > 0) currentCardIndex--
                            },
                            enabled = currentCardIndex > 0,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Previous", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                mainViewModel.markWordLearned(currentWord)
                                if (currentCardIndex < dailyList.size - 1) {
                                    currentCardIndex++
                                }
                                mainViewModel.recordPractice("Vocabulary", "Learned: ${currentWord.word}", minutes = 1)
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentWord.isLearned) SuccessGreen else IndigoPrimary
                            ),
                            modifier = Modifier.weight(1.3f)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                if (currentWord.isLearned) "Learned ✓" else "Mark Learned",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                if (currentCardIndex < dailyList.size - 1) currentCardIndex++
                            },
                            enabled = currentCardIndex < dailyList.size - 1,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Next", color = TextPrimary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        } else {
            // List / Search Mode
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search words or Hindi meaning...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IndigoPrimary,
                        unfocusedBorderColor = BorderLight
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Difficulty Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Beginner", "Intermediate", "Advanced").forEach { diff ->
                        val selected = difficultyFilter == diff
                        FilterChip(
                            selected = selected,
                            onClick = { difficultyFilter = diff },
                            label = { Text(diff, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IndigoPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredWords) { item ->
                        WordListItemCard(
                            word = item,
                            onSpeak = { speechManager.speak(item.word) },
                            onToggleFavorite = { mainViewModel.toggleWordFavorite(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WordListItemCard(
    word: VocabularyWord,
    onSpeak: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "/ ${word.pronunciation} /",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = IndigoPrimary
                        )
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = word.hindiMeaning,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF0F766E),
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = word.exampleSentence,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                TtsSpeakerButton(onSpeak = onSpeak)
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onToggleFavorite, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (word.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Favorite",
                        tint = if (word.isFavorite) PinkAccent else TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
