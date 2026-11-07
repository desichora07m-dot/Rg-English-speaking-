package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 1,
    val totalMinutes: Int = 45,
    val totalSessions: Int = 6,
    val streakDays: Int = 4,
    val lastActiveDate: String = "",
    val wordsLearnedCount: Int = 28,
    val totalQuizScore: Int = 85
)

@Entity(tableName = "learned_words")
data class LearnedWordEntity(
    @PrimaryKey val wordId: String,
    val word: String,
    val hindiMeaning: String,
    val difficulty: String,
    val isFavorite: Boolean = false,
    val isLearned: Boolean = true,
    val learnedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "practice_history")
data class PracticeHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val activityType: String,
    val summary: String,
    val scoreOrDetail: String = "",
    val durationMinutes: Int = 5,
    val timestamp: Long = System.currentTimeMillis()
)
