package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    fun getUserStats(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    suspend fun getUserStatsOnce(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserStats(stats: UserStatsEntity)

    @Query("SELECT * FROM learned_words ORDER BY learnedTimestamp DESC")
    fun getAllLearnedWords(): Flow<List<LearnedWordEntity>>

    @Query("SELECT * FROM learned_words WHERE isFavorite = 1 ORDER BY learnedTimestamp DESC")
    fun getFavoriteWords(): Flow<List<LearnedWordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWord(word: LearnedWordEntity)

    @Query("DELETE FROM learned_words WHERE wordId = :wordId")
    suspend fun removeWord(wordId: String)

    @Query("SELECT * FROM practice_history ORDER BY timestamp DESC LIMIT 30")
    fun getPracticeHistory(): Flow<List<PracticeHistoryEntity>>

    @Insert
    suspend fun insertHistory(item: PracticeHistoryEntity)

    @Query("DELETE FROM user_stats")
    suspend fun clearStats()

    @Query("DELETE FROM learned_words")
    suspend fun clearWords()

    @Query("DELETE FROM practice_history")
    suspend fun clearHistory()
}

@Database(
    entities = [UserStatsEntity::class, LearnedWordEntity::class, PracticeHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "english_speaking_ai.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
