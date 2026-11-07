package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AiConversationScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DailyQuestionsScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.GrammarScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PronunciationScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SpeakingPracticeScreen
import com.example.ui.screens.TranslationScreen
import com.example.ui.screens.VocabularyScreen
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AiLearningViewModel
import com.example.viewmodel.MainViewModel

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel,
    aiViewModel: AiLearningViewModel
) {
    val navStack = remember { mutableStateListOf("home") }
    val currentRoute = navStack.lastOrNull() ?: "home"

    fun navigateTo(route: String) {
        if (currentRoute != route) {
            navStack.add(route)
        }
    }

    fun popBack() {
        if (navStack.size > 1) {
            navStack.removeAt(navStack.size - 1)
        }
    }

    // Android back handler
    BackHandler(enabled = navStack.size > 1) {
        popBack()
    }

    val bottomNavItems = listOf(
        BottomNavItem(
            route = "home",
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            route = "speaking_practice",
            label = "Speaking",
            selectedIcon = Icons.Filled.RecordVoiceOver,
            unselectedIcon = Icons.Outlined.RecordVoiceOver
        ),
        BottomNavItem(
            route = "vocabulary",
            label = "Vocab",
            selectedIcon = Icons.Filled.MenuBook,
            unselectedIcon = Icons.Outlined.MenuBook
        ),
        BottomNavItem(
            route = "analytics",
            label = "Progress",
            selectedIcon = Icons.Filled.Analytics,
            unselectedIcon = Icons.Outlined.Analytics
        ),
        BottomNavItem(
            route = "settings",
            label = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )

    // Check if current route is one of the main 5 tabs
    val isMainTab = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("app_bottom_bar")
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = when (item.route) {
                        "home" -> currentRoute == "home"
                        "speaking_practice" -> currentRoute == "speaking_practice" || currentRoute == "conversation" || currentRoute == "daily_questions"
                        "vocabulary" -> currentRoute == "vocabulary" || currentRoute == "flashcards"
                        "analytics" -> currentRoute == "analytics"
                        "settings" -> currentRoute == "settings"
                        else -> currentRoute == item.route
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != item.route) {
                                navStack.clear()
                                navStack.add(item.route)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = IndigoPrimary,
                            selectedTextColor = IndigoPrimary,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color(0xFFEEF2FF)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRoute) {
                "home" -> HomeScreen(
                    mainViewModel = mainViewModel,
                    onNavigate = { navigateTo(it) }
                )
                "speaking_practice" -> SpeakingPracticeScreen(
                    mainViewModel = mainViewModel,
                    aiViewModel = aiViewModel,
                    onBack = { popBack() }
                )
                "translation" -> TranslationScreen(
                    mainViewModel = mainViewModel,
                    aiViewModel = aiViewModel,
                    onBack = { popBack() }
                )
                "flashcards" -> FlashcardsScreen(
                    mainViewModel = mainViewModel,
                    onBack = { popBack() }
                )
                "vocabulary" -> VocabularyScreen(
                    mainViewModel = mainViewModel,
                    onBack = { popBack() }
                )
                "daily_questions" -> DailyQuestionsScreen(
                    mainViewModel = mainViewModel,
                    aiViewModel = aiViewModel,
                    onBack = { popBack() }
                )
                "grammar" -> GrammarScreen(
                    mainViewModel = mainViewModel,
                    aiViewModel = aiViewModel,
                    onBack = { popBack() }
                )
                "conversation" -> AiConversationScreen(
                    mainViewModel = mainViewModel,
                    aiViewModel = aiViewModel,
                    onBack = { popBack() }
                )
                "pronunciation" -> PronunciationScreen(
                    mainViewModel = mainViewModel,
                    aiViewModel = aiViewModel,
                    onBack = { popBack() }
                )
                "quiz" -> QuizScreen(
                    mainViewModel = mainViewModel,
                    aiViewModel = aiViewModel,
                    onBack = { popBack() }
                )
                "analytics" -> AnalyticsScreen(
                    mainViewModel = mainViewModel,
                    onBack = { popBack() }
                )
                "settings" -> SettingsScreen(
                    mainViewModel = mainViewModel,
                    onBack = { popBack() }
                )
                else -> HomeScreen(
                    mainViewModel = mainViewModel,
                    onNavigate = { navigateTo(it) }
                )
            }
        }
    }
}
