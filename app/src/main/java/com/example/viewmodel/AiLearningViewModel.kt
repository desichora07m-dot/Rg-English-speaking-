package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiAiService
import com.example.audio.SpeechManager
import com.example.data.SampleData
import com.example.model.ChatMessage
import com.example.model.ConversationScenario
import com.example.model.DailyQuestion
import com.example.model.GrammarAnalysis
import com.example.model.PronunciationChallenge
import com.example.model.QuizQuestion
import com.example.model.SpeakingEvaluation
import com.example.model.TranslationResult
import com.example.model.UserSettings
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AiLearningViewModel : ViewModel() {

    private val aiService = GeminiAiService()

    // 1. Translation
    private val _translationInput = MutableStateFlow("")
    val translationInput: StateFlow<String> = _translationInput.asStateFlow()

    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()

    private val _translationResult = MutableStateFlow<TranslationResult?>(null)
    val translationResult: StateFlow<TranslationResult?> = _translationResult.asStateFlow()

    private val _translationError = MutableStateFlow<String?>(null)
    val translationError: StateFlow<String?> = _translationError.asStateFlow()

    fun updateTranslationInput(text: String) {
        _translationInput.value = text
    }

    fun translate(explanationLanguage: String = "Hinglish") {
        val query = _translationInput.value.trim()
        if (query.isBlank()) return

        viewModelScope.launch {
            _isTranslating.value = true
            _translationError.value = null
            try {
                val res = aiService.translateHindiToEnglish(query, explanationLanguage)
                _translationResult.value = res
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _translationError.value = e.localizedMessage ?: "Translation failed."
            } finally {
                _isTranslating.value = false
            }
        }
    }

    fun clearTranslation() {
        _translationInput.value = ""
        _translationResult.value = null
        _translationError.value = null
    }

    // 2. Speaking Practice & AI Conversation
    private val _currentScenario = MutableStateFlow(SampleData.scenariosList.first())
    val currentScenario: StateFlow<ConversationScenario> = _currentScenario.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isEvaluatingSpeaking = MutableStateFlow(false)
    val isEvaluatingSpeaking: StateFlow<Boolean> = _isEvaluatingSpeaking.asStateFlow()

    private val _latestSpeakingEval = MutableStateFlow<SpeakingEvaluation?>(null)
    val latestSpeakingEval: StateFlow<SpeakingEvaluation?> = _latestSpeakingEval.asStateFlow()

    fun selectScenario(scenario: ConversationScenario, speechManager: SpeechManager? = null) {
        _currentScenario.value = scenario
        val initialGreeting = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = "ai",
            text = scenario.initialAiGreeting
        )
        _chatMessages.value = listOf(initialGreeting)
        _latestSpeakingEval.value = null
        speechManager?.speak(scenario.initialAiGreeting)
    }

    fun sendSpeechMessage(
        userText: String,
        speechManager: SpeechManager,
        settings: UserSettings,
        onSessionComplete: (() -> Unit)? = null
    ) {
        if (userText.isBlank()) return

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = "user",
            text = userText
        )
        _chatMessages.value = _chatMessages.value + userMsg

        val lastAiPrompt = _chatMessages.value
            .filter { it.sender == "ai" }
            .lastOrNull()?.text ?: _currentScenario.value.initialAiGreeting

        viewModelScope.launch {
            _isEvaluatingSpeaking.value = true
            try {
                val eval = aiService.evaluateSpeaking(
                    userSpeech = userText,
                    previousPrompt = lastAiPrompt,
                    scenario = _currentScenario.value.contextPrompt,
                    explanationLanguage = settings.explanationLanguage
                )
                _latestSpeakingEval.value = eval

                val aiMsg = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = "ai",
                    text = eval.aiReply,
                    correction = eval.correctedSentence,
                    hindiTip = eval.hindiExplanation
                )
                _chatMessages.value = _chatMessages.value + aiMsg

                if (settings.autoPlayTts) {
                    speechManager.speak(eval.aiReply)
                }
                onSessionComplete?.invoke()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val fallbackReply = "That's great! Speaking English every day builds confidence. What else would you like to share?"
                _chatMessages.value = _chatMessages.value + ChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = "ai",
                    text = fallbackReply
                )
                if (settings.autoPlayTts) {
                    speechManager.speak(fallbackReply)
                }
            } finally {
                _isEvaluatingSpeaking.value = false
            }
        }
    }

    // 3. Grammar Correction
    private val _grammarInput = MutableStateFlow("")
    val grammarInput: StateFlow<String> = _grammarInput.asStateFlow()

    private val _isCheckingGrammar = MutableStateFlow(false)
    val isCheckingGrammar: StateFlow<Boolean> = _isCheckingGrammar.asStateFlow()

    private val _grammarResult = MutableStateFlow<GrammarAnalysis?>(null)
    val grammarResult: StateFlow<GrammarAnalysis?> = _grammarResult.asStateFlow()

    fun updateGrammarInput(text: String) {
        _grammarInput.value = text
    }

    fun checkGrammar(explanationLanguage: String = "Hinglish") {
        val sentence = _grammarInput.value.trim()
        if (sentence.isBlank()) return

        viewModelScope.launch {
            _isCheckingGrammar.value = true
            try {
                val res = aiService.correctGrammar(sentence, explanationLanguage)
                _grammarResult.value = res
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Return clear error state
            } finally {
                _isCheckingGrammar.value = false
            }
        }
    }

    // 4. Daily Speaking Questions
    private val _dailyCategories = listOf(
        "Daily Life", "School & College", "Friends", "Family",
        "Shopping", "Food", "Hobbies", "Travel", "Technology", "Job Interviews", "Self Introduction"
    )
    val dailyCategories: List<String> = _dailyCategories

    private val _selectedCategory = MutableStateFlow("Daily Life")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _dailyQuestions = MutableStateFlow<List<DailyQuestion>>(aiService.fallbackQuestions("Daily Life"))
    val dailyQuestions: StateFlow<List<DailyQuestion>> = _dailyQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _isLoadingQuestions = MutableStateFlow(false)
    val isLoadingQuestions: StateFlow<Boolean> = _isLoadingQuestions.asStateFlow()

    private val _questionEval = MutableStateFlow<SpeakingEvaluation?>(null)
    val questionEval: StateFlow<SpeakingEvaluation?> = _questionEval.asStateFlow()

    fun selectCategory(cat: String) {
        _selectedCategory.value = cat
        _currentQuestionIndex.value = 0
        _questionEval.value = null
        loadCategoryQuestions(cat)
    }

    fun loadCategoryQuestions(cat: String) {
        viewModelScope.launch {
            _isLoadingQuestions.value = true
            try {
                val questions = aiService.generateDailyQuestions(cat)
                _dailyQuestions.value = questions
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _dailyQuestions.value = aiService.fallbackQuestions(cat)
            } finally {
                _isLoadingQuestions.value = false
            }
        }
    }

    fun evaluateQuestionAnswer(answerText: String, settings: UserSettings) {
        val currentQ = _dailyQuestions.value.getOrNull(_currentQuestionIndex.value) ?: return
        viewModelScope.launch {
            _isEvaluatingSpeaking.value = true
            try {
                val eval = aiService.evaluateSpeaking(
                    userSpeech = answerText,
                    previousPrompt = currentQ.questionEnglish,
                    scenario = "Daily practice question answering",
                    explanationLanguage = settings.explanationLanguage
                )
                _questionEval.value = eval
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _questionEval.value = SpeakingEvaluation(
                    aiReply = "Good answer! You expressed your thoughts clearly.",
                    userMistake = null,
                    correctedSentence = answerText,
                    hindiExplanation = "Bahut badiya prayas! Keep answering in complete sentences.",
                    encouragementTip = "Awesome effort!"
                )
            } finally {
                _isEvaluatingSpeaking.value = false
            }
        }
    }

    fun nextDailyQuestion() {
        if (_currentQuestionIndex.value < _dailyQuestions.value.size - 1) {
            _currentQuestionIndex.value += 1
            _questionEval.value = null
        }
    }

    // 5. Quick Match Quiz
    private val _quizQuestions = MutableStateFlow<List<QuizQuestion>>(aiService.fallbackQuiz())
    val quizQuestions: StateFlow<List<QuizQuestion>> = _quizQuestions.asStateFlow()

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _selectedOption = MutableStateFlow<Int?>(null)
    val selectedOption: StateFlow<Int?> = _selectedOption.asStateFlow()

    private val _isOptionConfirmed = MutableStateFlow(false)
    val isOptionConfirmed: StateFlow<Boolean> = _isOptionConfirmed.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished.asStateFlow()

    fun selectQuizOption(index: Int) {
        if (!_isOptionConfirmed.value) {
            _selectedOption.value = index
        }
    }

    fun confirmQuizAnswer() {
        val selected = _selectedOption.value ?: return
        val current = _quizQuestions.value.getOrNull(_currentQuizIndex.value) ?: return
        _isOptionConfirmed.value = true
        if (selected == current.correctIndex) {
            _quizScore.value += 20
        }
    }

    fun nextQuizQuestion() {
        if (_currentQuizIndex.value < _quizQuestions.value.size - 1) {
            _currentQuizIndex.value += 1
            _selectedOption.value = null
            _isOptionConfirmed.value = false
        } else {
            _isQuizFinished.value = true
        }
    }

    fun restartQuiz(topic: String = "English Essentials") {
        viewModelScope.launch {
            _currentQuizIndex.value = 0
            _selectedOption.value = null
            _isOptionConfirmed.value = false
            _quizScore.value = 0
            _isQuizFinished.value = false
            try {
                val generated = aiService.generateQuizQuestions(topic)
                _quizQuestions.value = generated
            } catch (e: Exception) {
                _quizQuestions.value = aiService.fallbackQuiz()
            }
        }
    }

    // 6. Pronunciation Practice
    private val _pronunciationList = MutableStateFlow(SampleData.pronunciationList)
    val pronunciationList: StateFlow<List<PronunciationChallenge>> = _pronunciationList.asStateFlow()

    private val _currentPronIndex = MutableStateFlow(0)
    val currentPronIndex: StateFlow<Int> = _currentPronIndex.asStateFlow()

    private val _pronEvaluation = MutableStateFlow<Pair<Int, String>?>(null)
    val pronEvaluation: StateFlow<Pair<Int, String>?> = _pronEvaluation.asStateFlow()

    fun evaluatePronunciation(spokenText: String) {
        val current = _pronunciationList.value.getOrNull(_currentPronIndex.value) ?: return
        val target = current.wordOrPhrase.trim().lowercase()
        val recognized = spokenText.trim().lowercase()

        val matchPercent: Int = when {
            recognized.contains(target) || target.contains(recognized) -> 95
            levenshteinSimilarity(target, recognized) > 0.7 -> 80
            levenshteinSimilarity(target, recognized) > 0.4 -> 60
            else -> 40
        }

        val feedback = when {
            matchPercent >= 90 -> "Excellent! Your pronunciation was clear and accurate."
            matchPercent >= 75 -> "Good job! A little more practice with word stress will make it native."
            else -> "Keep trying! Remember the tip: ${current.hindiTip}"
        }

        _pronEvaluation.value = Pair(matchPercent, feedback)
    }

    fun nextPronunciationChallenge() {
        if (_currentPronIndex.value < _pronunciationList.value.size - 1) {
            _currentPronIndex.value += 1
            _pronEvaluation.value = null
        }
    }

    fun prevPronunciationChallenge() {
        if (_currentPronIndex.value > 0) {
            _currentPronIndex.value -= 1
            _pronEvaluation.value = null
        }
    }

    private fun levenshteinSimilarity(s1: String, s2: String): Double {
        if (s1 == s2) return 1.0
        if (s1.isEmpty() || s2.isEmpty()) return 0.0
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j
        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost)
            }
        }
        val dist = dp[s1.length][s2.length]
        val maxLen = maxOf(s1.length, s2.length)
        return (maxLen - dist).toDouble() / maxLen.toDouble()
    }
}
