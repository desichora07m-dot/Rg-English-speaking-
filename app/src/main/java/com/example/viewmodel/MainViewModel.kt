package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SpeechManager
import com.example.data.SampleData
import com.example.data.local.AppDatabase
import com.example.data.local.LearnedWordEntity
import com.example.data.local.PracticeHistoryEntity
import com.example.data.local.PreferencesManager
import com.example.data.local.UserStatsEntity
import com.example.model.UserSettings
import com.example.model.VocabularyWord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val userDao = db.userDao()
    val preferencesManager = PreferencesManager(application)
    val speechManager = SpeechManager(application)

    val settings: StateFlow<UserSettings> = preferencesManager.settingsFlow

    val userStats: StateFlow<UserStatsEntity?> = userDao.getUserStats().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserStatsEntity(
            totalMinutes = 45,
            totalSessions = 6,
            streakDays = 4,
            wordsLearnedCount = 28,
            totalQuizScore = 85
        )
    )

    val learnedWordsList: StateFlow<List<LearnedWordEntity>> = userDao.getAllLearnedWords().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val practiceHistory: StateFlow<List<PracticeHistoryEntity>> = userDao.getPracticeHistory().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _vocabularyItems = MutableStateFlow<List<VocabularyWord>>(SampleData.vocabularyList)
    val vocabularyItems: StateFlow<List<VocabularyWord>> = _vocabularyItems.asStateFlow()

    init {
        initializeStatsAndSeedData()
    }

    private fun initializeStatsAndSeedData() {
        viewModelScope.launch {
            val current = userDao.getUserStatsOnce()
            val today = preferencesManager.getTodayDateString()
            if (current == null) {
                userDao.saveUserStats(
                    UserStatsEntity(
                        id = 1,
                        totalMinutes = 45,
                        totalSessions = 6,
                        streakDays = 4,
                        lastActiveDate = today,
                        wordsLearnedCount = 28,
                        totalQuizScore = 85
                    )
                )
            } else if (current.lastActiveDate != today) {
                // User active on a new day -> increment streak or keep active
                userDao.saveUserStats(
                    current.copy(
                        lastActiveDate = today,
                        streakDays = current.streakDays + 1
                    )
                )
            }
        }
    }

    fun recordPractice(activityType: String, summary: String, minutes: Int = 5, score: String = "") {
        viewModelScope.launch {
            userDao.insertHistory(
                PracticeHistoryEntity(
                    activityType = activityType,
                    summary = summary,
                    scoreOrDetail = score,
                    durationMinutes = minutes
                )
            )

            val current = userDao.getUserStatsOnce() ?: UserStatsEntity()
            userDao.saveUserStats(
                current.copy(
                    totalMinutes = current.totalMinutes + minutes,
                    totalSessions = current.totalSessions + 1
                )
            )
        }
    }

    fun recordQuizScore(score: Int) {
        viewModelScope.launch {
            val current = userDao.getUserStatsOnce() ?: UserStatsEntity()
            userDao.saveUserStats(
                current.copy(
                    totalQuizScore = current.totalQuizScore + score,
                    totalMinutes = current.totalMinutes + 3,
                    totalSessions = current.totalSessions + 1
                )
            )
            userDao.insertHistory(
                PracticeHistoryEntity(
                    activityType = "Quick Quiz",
                    summary = "Scored $score pts in Match Quiz",
                    scoreOrDetail = "$score pts",
                    durationMinutes = 3
                )
            )
        }
    }

    fun toggleWordFavorite(word: VocabularyWord) {
        val updated = _vocabularyItems.value.map {
            if (it.id == word.id) it.copy(isFavorite = !it.isFavorite) else it
        }
        _vocabularyItems.value = updated
        viewModelScope.launch {
            val target = updated.firstOrNull { it.id == word.id }
            if (target != null) {
                userDao.saveWord(
                    LearnedWordEntity(
                        wordId = target.id,
                        word = target.word,
                        hindiMeaning = target.hindiMeaning,
                        difficulty = target.difficulty,
                        isFavorite = target.isFavorite,
                        isLearned = target.isLearned
                    )
                )
            }
        }
    }

    fun markWordLearned(word: VocabularyWord) {
        val updated = _vocabularyItems.value.map {
            if (it.id == word.id) it.copy(isLearned = !it.isLearned) else it
        }
        _vocabularyItems.value = updated
        viewModelScope.launch {
            val target = updated.firstOrNull { it.id == word.id }
            if (target != null) {
                userDao.saveWord(
                    LearnedWordEntity(
                        wordId = target.id,
                        word = target.word,
                        hindiMeaning = target.hindiMeaning,
                        difficulty = target.difficulty,
                        isFavorite = target.isFavorite,
                        isLearned = target.isLearned
                    )
                )
                val current = userDao.getUserStatsOnce() ?: UserStatsEntity()
                userDao.saveUserStats(
                    current.copy(
                        wordsLearnedCount = current.wordsLearnedCount + if (target.isLearned) 1 else -1
                    )
                )
            }
        }
    }

    fun updateUserSettings(newSettings: UserSettings) {
        preferencesManager.updateSettings(newSettings)
        speechManager.setSpeechRate(newSettings.speechRate)
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            userDao.clearStats()
            userDao.clearWords()
            userDao.clearHistory()
            userDao.saveUserStats(
                UserStatsEntity(
                    id = 1,
                    totalMinutes = 0,
                    totalSessions = 0,
                    streakDays = 1,
                    lastActiveDate = preferencesManager.getTodayDateString(),
                    wordsLearnedCount = 0,
                    totalQuizScore = 0
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.destroy()
    }
}
