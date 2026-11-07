package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.model.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_settings_prefs", Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<UserSettings> = _settingsFlow.asStateFlow()

    private fun loadSettings(): UserSettings {
        return UserSettings(
            userName = prefs.getString("user_name", "Rahul") ?: "Rahul",
            difficultyLevel = prefs.getString("difficulty_level", "Beginner") ?: "Beginner",
            explanationLanguage = prefs.getString("explanation_lang", "Hinglish") ?: "Hinglish",
            autoPlayTts = prefs.getBoolean("auto_play_tts", true),
            speechRate = prefs.getFloat("speech_rate", 0.9f),
            dailyGoalMinutes = prefs.getInt("daily_goal_minutes", 15)
        )
    }

    fun updateSettings(newSettings: UserSettings) {
        prefs.edit()
            .putString("user_name", newSettings.userName)
            .putString("difficulty_level", newSettings.difficultyLevel)
            .putString("explanation_lang", newSettings.explanationLanguage)
            .putBoolean("auto_play_tts", newSettings.autoPlayTts)
            .putFloat("speech_rate", newSettings.speechRate)
            .putInt("daily_goal_minutes", newSettings.dailyGoalMinutes)
            .apply()
        _settingsFlow.value = newSettings
    }

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun exportSettingsJson(): String {
        val s = _settingsFlow.value
        return """
            {
              "userName": "${s.userName}",
              "difficultyLevel": "${s.difficultyLevel}",
              "explanationLanguage": "${s.explanationLanguage}",
              "autoPlayTts": ${s.autoPlayTts},
              "speechRate": ${s.speechRate},
              "dailyGoalMinutes": ${s.dailyGoalMinutes}
            }
        """.trimIndent()
    }

    fun importSettingsFromJson(json: String): Boolean {
        try {
            val obj = org.json.JSONObject(json)
            val settings = UserSettings(
                userName = obj.optString("userName", "Rahul"),
                difficultyLevel = obj.optString("difficultyLevel", "Beginner"),
                explanationLanguage = obj.optString("explanationLanguage", "Hinglish"),
                autoPlayTts = obj.optBoolean("autoPlayTts", true),
                speechRate = obj.optDouble("speechRate", 0.9).toFloat(),
                dailyGoalMinutes = obj.optInt("dailyGoalMinutes", 15)
            )
            updateSettings(settings)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}
