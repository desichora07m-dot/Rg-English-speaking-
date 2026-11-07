package com.example.ai

import android.util.Log
import com.example.model.DailyQuestion
import com.example.model.GrammarAnalysis
import com.example.model.QuizQuestion
import com.example.model.SpeakingEvaluation
import com.example.model.TranslationResult
import com.example.model.VocabularyWord
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

class GeminiAiService {
    private val TAG = "GeminiAiService"

    // Initialized lazily via by lazy as mandated by firebase-ai-logic-android
    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
            }
        )
    }

    private val textOnlyModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash"
        )
    }

    suspend fun translateHindiToEnglish(
        input: String,
        explanationLanguage: String = "Hinglish"
    ): TranslationResult {
        val prompt = """
            You are an expert English-Hindi language teacher for Indian beginners.
            Translate the following text (which can be in Devanagari Hindi, Roman Hinglish, or English) into natural conversational English.
            Input: "$input"
            
            Return a JSON object strictly matching this schema:
            {
              "englishText": "The natural modern spoken English translation",
              "hindiMeaning": "Accurate Hindi meaning in simple Devanagari",
              "grammarPattern": "Simple formula explanation like 'Subject + have/has + Noun' with 1 sentence Hindi explanation",
              "alternatives": ["Alternative natural way 1", "Alternative natural way 2"],
              "examples": ["Example beginner sentence 1 with Hindi meaning", "Example beginner sentence 2 with Hindi meaning"]
            }
            Do not include markdown fences. Output raw JSON only.
        """.trimIndent()

        return try {
            val responseText = withTimeout(45_000L) {
                model.generateContent(prompt).text.orEmpty()
            }
            parseTranslationResult(responseText, input)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "translateHindiToEnglish failed", e)
            fallbackTranslation(input)
        }
    }

    suspend fun evaluateSpeaking(
        userSpeech: String,
        previousPrompt: String,
        scenario: String,
        explanationLanguage: String = "Hinglish"
    ): SpeakingEvaluation {
        val prompt = """
            You are Priya, a friendly, encouraging English teacher speaking with an Indian student who understands Hindi but struggles with spoken English.
            Context / Scenario: $scenario
            Previous Teacher Question: "$previousPrompt"
            Student Spoke: "$userSpeech"
            
            Evaluate the student's spoken response.
            Be extremely encouraging and positive.
            If the student made any grammar or pronunciation/word choice error, gently correct it and explain in simple $explanationLanguage why.
            If their sentence was already good, praise them and provide an even more natural conversational idiom/phrasing.
            
            Return a JSON object strictly matching:
            {
              "aiReply": "Conversational continuation in warm, friendly simple English (2-3 sentences)",
              "userMistake": "Brief note on error if any, or null if perfect",
              "correctedSentence": "The ideal natural spoken sentence",
              "hindiExplanation": "Clear, gentle 1-2 line explanation in simple Hindi/Hinglish (e.g. 'Yahan par past tense use hoga...')",
              "nextQuestionPrompt": "A follow-up question to keep the student practicing speaking",
              "encouragementTip": "Short cheering message (e.g. 'Bahut badhiya koshish! Try saying it once more.')"
            }
            Do not include markdown code fences. Raw JSON only.
        """.trimIndent()

        return try {
            val responseText = withTimeout(45_000L) {
                model.generateContent(prompt).text.orEmpty()
            }
            parseSpeakingEvaluation(responseText, userSpeech)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "evaluateSpeaking failed", e)
            fallbackSpeakingEvaluation(userSpeech)
        }
    }

    suspend fun correctGrammar(
        sentence: String,
        explanationLanguage: String = "Hinglish"
    ): GrammarAnalysis {
        val prompt = """
            You are an expert English grammar coach for Hindi speakers.
            Analyze this sentence: "$sentence"
            
            Identify any grammatical errors (tense, subject-verb agreement, prepositions, articles, word order).
            Explain in simple, beginner-friendly $explanationLanguage.
            
            Return a JSON object strictly matching:
            {
              "originalSentence": "$sentence",
              "correctedSentence": "Corrected natural English sentence",
              "hindiExplanation": "Explanation of the rule in simple Hindi/Hinglish",
              "grammarRule": "Core rule title (e.g. Simple Present with He/She/It)",
              "examples": ["Example 1 with Hindi meaning", "Example 2 with Hindi meaning"],
              "practiceQuestion": "A quick fill-in-the-blank practice question based on this rule with answer in parenthesis"
            }
            No markdown fences. Raw JSON only.
        """.trimIndent()

        return try {
            val responseText = withTimeout(45_000L) {
                model.generateContent(prompt).text.orEmpty()
            }
            parseGrammarAnalysis(responseText, sentence)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "correctGrammar failed", e)
            fallbackGrammarAnalysis(sentence)
        }
    }

    suspend fun generateDailyQuestions(category: String): List<DailyQuestion> {
        val prompt = """
            Generate 5 practical English speaking questions for an Indian student practicing daily English.
            Category: $category
            Level: Beginner to Intermediate.
            
            Return a JSON array of 5 items strictly matching:
            [
              {
                "id": "q1",
                "category": "$category",
                "questionEnglish": "What time do you usually wake up in the morning?",
                "questionHindi": "आप आमतौर पर सुबह कितने बजे उठते हैं?",
                "tipForAnswering": "Answer using: 'I usually wake up at ...'"
              }
            ]
            No markdown fences. Raw JSON only.
        """.trimIndent()

        return try {
            val responseText = withTimeout(45_000L) {
                model.generateContent(prompt).text.orEmpty()
            }
            parseDailyQuestions(responseText, category)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "generateDailyQuestions failed", e)
            fallbackQuestions(category)
        }
    }

    suspend fun generateQuizQuestions(topic: String): List<QuizQuestion> {
        val prompt = """
            Generate 5 multiple choice English quiz questions for Hindi-speaking learners.
            Topic/Focus: $topic
            Include question types: Hindi to English translation, Fill in the blanks, Grammar correction, Vocabulary matching.
            
            Return a JSON array strictly matching:
            [
              {
                "id": "quiz_1",
                "question": "Choose the correct sentence:",
                "questionType": "Grammar Check",
                "options": ["She go to school", "She goes to school", "She going school", "She gone to school"],
                "correctIndex": 1,
                "explanationHindi": "He/She/It ke saath simple present mein verb me 's/es' judta hai."
              }
            ]
            No markdown fences. Raw JSON only.
        """.trimIndent()

        return try {
            val responseText = withTimeout(45_000L) {
                model.generateContent(prompt).text.orEmpty()
            }
            parseQuizQuestions(responseText)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "generateQuizQuestions failed", e)
            fallbackQuiz()
        }
    }

    // Parsing helpers
    private fun cleanJson(raw: String): String {
        var clean = raw.trim()
        if (clean.startsWith("```json")) {
            clean = clean.removePrefix("```json")
        } else if (clean.startsWith("```")) {
            clean = clean.removePrefix("```")
        }
        if (clean.endsWith("```")) {
            clean = clean.removeSuffix("```")
        }
        return clean.trim()
    }

    private fun parseTranslationResult(raw: String, input: String): TranslationResult {
        val json = JSONObject(cleanJson(raw))
        val englishText = json.optString("englishText", input)
        val hindiMeaning = json.optString("hindiMeaning", input)
        val grammarPattern = json.optString("grammarPattern", "Subject + Verb + Object")
        val altArray = json.optJSONArray("alternatives") ?: JSONArray()
        val alternatives = (0 until altArray.length()).map { altArray.getString(it) }
        val exArray = json.optJSONArray("examples") ?: JSONArray()
        val examples = (0 until exArray.length()).map { exArray.getString(it) }

        return TranslationResult(
            englishText = englishText,
            hindiMeaning = hindiMeaning,
            grammarPattern = grammarPattern,
            alternatives = if (alternatives.isNotEmpty()) alternatives else listOf("I have a phone.", "There is a phone with me."),
            examples = if (examples.isNotEmpty()) examples else listOf("I have a pen. (मेरे पास एक कलम है)", "She has a book. (उसके पास एक किताब है)")
        )
    }

    private fun parseSpeakingEvaluation(raw: String, speech: String): SpeakingEvaluation {
        val json = JSONObject(cleanJson(raw))
        return SpeakingEvaluation(
            aiReply = json.optString("aiReply", "Great effort! Speaking every day is the key to fluency."),
            userMistake = json.optString("userMistake").takeIf { it.isNotBlank() && it != "null" },
            correctedSentence = json.optString("correctedSentence", speech),
            hindiExplanation = json.optString("hindiExplanation", "Dheere dheere practice karne se sentence structure sudhar jayega."),
            nextQuestionPrompt = json.optString("nextQuestionPrompt", "What else did you do today?"),
            encouragementTip = json.optString("encouragementTip", "Aapne bahut achha koshish kiya! Keep going!")
        )
    }

    private fun parseGrammarAnalysis(raw: String, original: String): GrammarAnalysis {
        val json = JSONObject(cleanJson(raw))
        val exArray = json.optJSONArray("examples") ?: JSONArray()
        val examples = (0 until exArray.length()).map { exArray.getString(it) }

        return GrammarAnalysis(
            originalSentence = json.optString("originalSentence", original),
            correctedSentence = json.optString("correctedSentence", original),
            hindiExplanation = json.optString("hindiExplanation", "Sentence structure sahi karne ke liye verb aur subject ka dhyan dein."),
            grammarRule = json.optString("grammarRule", "Subject-Verb Agreement"),
            examples = if (examples.isNotEmpty()) examples else listOf("He works hard.", "They work hard."),
            practiceQuestion = json.optString("practiceQuestion", "He ___ (play/plays) cricket every evening.")
        )
    }

    private fun parseDailyQuestions(raw: String, category: String): List<DailyQuestion> {
        val arr = JSONArray(cleanJson(raw))
        val list = mutableListOf<DailyQuestion>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            list.add(
                DailyQuestion(
                    id = obj.optString("id", "q_$i"),
                    category = obj.optString("category", category),
                    questionEnglish = obj.optString("questionEnglish", "Tell me about your day."),
                    questionHindi = obj.optString("questionHindi", "अपने दिन के बारे में बताएं।"),
                    tipForAnswering = obj.optString("tipForAnswering", "Start with: 'Today I...'")
                )
            )
        }
        return if (list.isNotEmpty()) list else fallbackQuestions(category)
    }

    private fun parseQuizQuestions(raw: String): List<QuizQuestion> {
        val arr = JSONArray(cleanJson(raw))
        val list = mutableListOf<QuizQuestion>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            val optArr = obj.optJSONArray("options") ?: JSONArray()
            val options = (0 until optArr.length()).map { optArr.getString(it) }
            list.add(
                QuizQuestion(
                    id = obj.optString("id", "quiz_$i"),
                    question = obj.optString("question", "Choose the right option:"),
                    questionType = obj.optString("questionType", "General Grammar"),
                    options = if (options.size == 4) options else listOf("Option A", "Option B", "Option C", "Option D"),
                    correctIndex = obj.optInt("correctIndex", 0).coerceIn(0, 3),
                    explanationHindi = obj.optString("explanationHindi", "Is niyam ko yaad rakhein.")
                )
            )
        }
        return if (list.isNotEmpty()) list else fallbackQuiz()
    }

    // Fallbacks for offline / instant experience
    private fun fallbackTranslation(input: String): TranslationResult {
        return TranslationResult(
            englishText = "I have a phone.",
            hindiMeaning = if (input.isNotBlank()) input else "मेरे पास एक फ़ोन है।",
            grammarPattern = "Subject (I/You/We/They) + have + Object",
            alternatives = listOf("I own a smartphone.", "I've got a phone with me."),
            examples = listOf(
                "I have two cars. (मेरे पास दो कारें हैं।)",
                "She has a new bag. (उसके पास एक नया बैग है।)"
            )
        )
    }

    private fun fallbackSpeakingEvaluation(speech: String): SpeakingEvaluation {
        return SpeakingEvaluation(
            aiReply = "That's wonderful! You communicated your idea clearly.",
            userMistake = if (speech.contains("at 7 clock", ignoreCase = true)) "Instead of '7 clock', say '7 o'clock'" else null,
            correctedSentence = if (speech.contains("at 7 clock", ignoreCase = true)) "I wake up at 7 o'clock." else speech,
            hindiExplanation = "Time batate waqt exact hour ke saath 'o'clock' use karte hain.",
            nextQuestionPrompt = "What do you like to have for breakfast?",
            encouragementTip = "Bahut badiya prayas! Practice makes perfect."
        )
    }

    private fun fallbackGrammarAnalysis(sentence: String): GrammarAnalysis {
        return GrammarAnalysis(
            originalSentence = sentence,
            correctedSentence = if (sentence.contains("She go", ignoreCase = true)) "She goes to school." else sentence,
            hindiExplanation = "He, She, It ke saath simple present tense mein verb ke aage 's' ya 'es' lagta hai.",
            grammarRule = "Simple Present Tense (Third Person Singular)",
            examples = listOf(
                "He plays cricket every Sunday. (वह हर रविवार क्रिकेट खेलता है।)",
                "She teaches English at school. (वह स्कूल में अंग्रेज़ी पढ़ाती है।)"
            ),
            practiceQuestion = "Rahul ___ (watch / watches) movies on weekends. (Answer: watches)"
        )
    }

    fun fallbackQuestions(category: String): List<DailyQuestion> {
        return listOf(
            DailyQuestion(
                id = "dq1",
                category = category,
                questionEnglish = "What time do you usually wake up in the morning?",
                questionHindi = "आप आमतौर पर सुबह कितने बजे उठते हैं?",
                tipForAnswering = "Begin with: 'I usually wake up at 6:30 AM...'"
            ),
            DailyQuestion(
                id = "dq2",
                category = category,
                questionEnglish = "What is your favorite food and why do you like it?",
                questionHindi = "आपका पसंदीदा खाना क्या है और आप इसे क्यों पसंद करते हैं?",
                tipForAnswering = "Say: 'My favorite food is ... because it is tasty and healthy.'"
            ),
            DailyQuestion(
                id = "dq3",
                category = category,
                questionEnglish = "How do you spend your free time on weekends?",
                questionHindi = "आप सप्ताहांत पर अपना खाली समय कैसे बिताते हैं?",
                tipForAnswering = "Answer: 'On weekends, I like to relax, read books or meet friends.'"
            ),
            DailyQuestion(
                id = "dq4",
                category = category,
                questionEnglish = "Can you describe your best friend in two sentences?",
                questionHindi = "क्या आप अपने सबसे अच्छे दोस्त का वर्णन दो वाक्यों में कर सकते हैं?",
                tipForAnswering = "Say: 'My best friend's name is ... He is very supportive and kind.'"
            ),
            DailyQuestion(
                id = "dq5",
                category = category,
                questionEnglish = "Why is learning English important for your future?",
                questionHindi = "आपके भविष्य के लिए अंग्रेज़ी सीखना क्यों महत्वपूर्ण है?",
                tipForAnswering = "Say: 'English is important because it opens up great career opportunities.'"
            )
        )
    }

    fun fallbackQuiz(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = "q_1",
                question = "Fill in the blank: 'She ___ to the market yesterday.'",
                questionType = "Fill in Blanks",
                options = listOf("go", "goes", "went", "gone"),
                correctIndex = 2,
                explanationHindi = "Yesterday beete hue kal ki baat hai, isliye past tense 'went' use hoga."
            ),
            QuizQuestion(
                id = "q_2",
                question = "Translate into English: 'मेरे पास एक किताब है।' ",
                questionType = "Hindi to English",
                options = listOf("I have a book.", "I having a book.", "Me got book.", "I am have book."),
                correctIndex = 0,
                explanationHindi = "Pass hone ke liye 'have' ka prayog hota hai: I + have + a book."
            ),
            QuizQuestion(
                id = "q_3",
                question = "Which sentence is grammatically correct?",
                questionType = "Grammar Check",
                options = listOf("He don't know the answer.", "He doesn't knows the answer.", "He doesn't know the answer.", "He not know answer."),
                correctIndex = 2,
                explanationHindi = "He ke saath 'doesn't' lagta hai aur uske baad verb ki base form (know) aati hai."
            ),
            QuizQuestion(
                id = "q_4",
                question = "What is the Hindi meaning of 'Confidence'?",
                questionType = "Vocab Match",
                options = listOf("डर (Fear)", "आत्मविश्वास (Self-belief)", "गुस्सा (Anger)", "आलस (Laziness)"),
                correctIndex = 1,
                explanationHindi = "Confidence ka matlab hota hai 'आत्मविश्वास' ya khud par vishwas."
            ),
            QuizQuestion(
                id = "q_5",
                question = "Choose the right option: 'They ___ playing football now.'",
                questionType = "Fill in Blanks",
                options = listOf("is", "are", "was", "has"),
                correctIndex = 1,
                explanationHindi = "Plural subject 'They' ke saath present continuous mein 'are' lagta hai."
            )
        )
    }
}
