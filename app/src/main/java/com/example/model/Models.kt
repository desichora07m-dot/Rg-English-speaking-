package com.example.model

data class TranslationResult(
    val englishText: String,
    val hindiMeaning: String,
    val grammarPattern: String,
    val alternatives: List<String>,
    val examples: List<String>
)

data class SpeakingEvaluation(
    val aiReply: String,
    val userMistake: String? = null,
    val correctedSentence: String? = null,
    val hindiExplanation: String? = null,
    val nextQuestionPrompt: String? = null,
    val encouragementTip: String? = null
)

data class GrammarAnalysis(
    val originalSentence: String,
    val correctedSentence: String,
    val hindiExplanation: String,
    val grammarRule: String,
    val examples: List<String>,
    val practiceQuestion: String
)

data class VocabularyWord(
    val id: String,
    val word: String,
    val hindiMeaning: String,
    val pronunciation: String,
    val partOfSpeech: String,
    val exampleSentence: String,
    val exampleHindi: String,
    val difficulty: String, // "Beginner", "Intermediate", "Advanced"
    val isFavorite: Boolean = false,
    val isLearned: Boolean = false
)

data class FlashcardItem(
    val id: String,
    val frontEnglish: String,
    val backHindi: String,
    val pronunciation: String,
    val grammarTip: String,
    val category: String,
    val isDifficult: Boolean = false,
    val isMastered: Boolean = false
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val questionType: String, // "Hindi to English", "Fill in Blanks", "Grammar Check", "Vocab Match"
    val options: List<String>,
    val correctIndex: Int,
    val explanationHindi: String
)

data class ConversationScenario(
    val id: String,
    val title: String,
    val hindiTitle: String,
    val iconName: String,
    val description: String,
    val initialAiGreeting: String,
    val contextPrompt: String,
    val samplePrompts: List<String>
)

data class ChatMessage(
    val id: String,
    val sender: String, // "user", "ai"
    val text: String,
    val correction: String? = null,
    val hindiTip: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class PronunciationChallenge(
    val id: String,
    val wordOrPhrase: String,
    val phoneticGuide: String,
    val hindiTip: String,
    val commonMistake: String,
    val difficulty: String // "Easy", "Medium", "Challenging"
)

data class DailyQuestion(
    val id: String,
    val category: String,
    val questionEnglish: String,
    val questionHindi: String,
    val tipForAnswering: String
)

data class UserSettings(
    val userName: String = "Rahul",
    val difficultyLevel: String = "Beginner",
    val explanationLanguage: String = "Hinglish", // "Hindi", "Hinglish", "English"
    val autoPlayTts: Boolean = true,
    val speechRate: Float = 0.9f,
    val dailyGoalMinutes: Int = 15
)
