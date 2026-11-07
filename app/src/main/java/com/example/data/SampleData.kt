package com.example.data

import com.example.model.ConversationScenario
import com.example.model.FlashcardItem
import com.example.model.PronunciationChallenge
import com.example.model.VocabularyWord

object SampleData {

    val vocabularyList = listOf(
        // Beginner
        VocabularyWord(
            id = "v1",
            word = "Achieve",
            hindiMeaning = "हासिल करना / प्राप्त करना",
            pronunciation = "uh-CHEEV",
            partOfSpeech = "Verb",
            exampleSentence = "Work hard to achieve your dreams.",
            exampleHindi = "अपने सपनों को हासिल करने के लिए कड़ी मेहनत करें।",
            difficulty = "Beginner"
        ),
        VocabularyWord(
            id = "v2",
            word = "Confidence",
            hindiMeaning = "आत्मविश्वास",
            pronunciation = "KON-fuh-dunss",
            partOfSpeech = "Noun",
            exampleSentence = "Speaking English daily will boost your confidence.",
            exampleHindi = "रोज़ अंग्रेज़ी बोलने से आपका आत्मविश्वास बढ़ेगा।",
            difficulty = "Beginner"
        ),
        VocabularyWord(
            id = "v3",
            word = "Fluent",
            hindiMeaning = "धाराप्रवाह / बिना अटके बोलना",
            pronunciation = "FLOO-unt",
            partOfSpeech = "Adjective",
            exampleSentence = "She is fluent in both Hindi and English.",
            exampleHindi = "वह हिंदी और अंग्रेज़ी दोनों धाराप्रवाह बोलती है।",
            difficulty = "Beginner"
        ),
        VocabularyWord(
            id = "v4",
            word = "Opportunity",
            hindiMeaning = "अवसर / मौक़ा",
            pronunciation = "op-er-TOO-nuh-tee",
            partOfSpeech = "Noun",
            exampleSentence = "This job is a great opportunity for my career.",
            exampleHindi = "यह नौकरी मेरे करियर के लिए एक शानदार मौक़ा है।",
            difficulty = "Beginner"
        ),
        VocabularyWord(
            id = "v5",
            word = "Improve",
            hindiMeaning = "सुधारना / बेहतर बनाना",
            pronunciation = "im-PROOV",
            partOfSpeech = "Verb",
            exampleSentence = "Practice 15 minutes daily to improve your accent.",
            exampleHindi = "अपने उच्चारण को सुधारने के लिए रोज़ 15 मिनट अभ्यास करें।",
            difficulty = "Beginner"
        ),
        VocabularyWord(
            id = "v6",
            word = "Comfortable",
            hindiMeaning = "आरामदायक / सहज",
            pronunciation = "KUMF-ter-bul",
            partOfSpeech = "Adjective",
            exampleSentence = "I feel comfortable speaking with my teacher.",
            exampleHindi = "मैं अपने शिक्षक से बात करने में सहज महसूस करता हूँ।",
            difficulty = "Beginner"
        ),
        VocabularyWord(
            id = "v7",
            word = "Describe",
            hindiMeaning = "वर्णन करना / समझाना",
            pronunciation = "dih-SKRYB",
            partOfSpeech = "Verb",
            exampleSentence = "Can you describe your hometown?",
            exampleHindi = "क्या आप अपने गृहनगर का वर्णन कर सकते हैं?",
            difficulty = "Beginner"
        ),
        VocabularyWord(
            id = "v8",
            word = "Essential",
            hindiMeaning = "ज़रूरी / अनिवार्य",
            pronunciation = "ih-SEN-shul",
            partOfSpeech = "Adjective",
            exampleSentence = "Good vocabulary is essential for speaking English.",
            exampleHindi = "अंग्रेज़ी बोलने के लिए अच्छी शब्दावली बहुत ज़रूरी है।",
            difficulty = "Beginner"
        ),
        VocabularyWord(
            id = "v9",
            word = "Hesitate",
            hindiMeaning = "हिचकिचाना",
            pronunciation = "HEZ-ih-tayt",
            partOfSpeech = "Verb",
            exampleSentence = "Never hesitate to ask questions when learning.",
            exampleHindi = "सीखते समय सवाल पूछने में कभी न हिचकिचाएँ।",
            difficulty = "Beginner"
        ),
        VocabularyWord(
            id = "v10",
            word = "Mistake",
            hindiMeaning = "ग़लती",
            pronunciation = "mih-STAYK",
            partOfSpeech = "Noun",
            exampleSentence = "Making mistakes is a normal part of learning.",
            exampleHindi = "ग़लतियाँ करना सीखने की प्रक्रिया का एक सामान्य हिस्सा है।",
            difficulty = "Beginner"
        ),
        // Intermediate
        VocabularyWord(
            id = "v11",
            word = "Articulate",
            hindiMeaning = "साफ़ और स्पष्ट ढंग से व्यक्त करना",
            pronunciation = "ar-TIK-yoo-layt",
            partOfSpeech = "Verb/Adjective",
            exampleSentence = "He articulated his points clearly in the meeting.",
            exampleHindi = "उसने मीटिंग में अपने विचार बहुत स्पष्ट रूप से रखे।",
            difficulty = "Intermediate"
        ),
        VocabularyWord(
            id = "v12",
            word = "Persevere",
            hindiMeaning = "दृढ़ रहना / हार न मानना",
            pronunciation = "pur-suh-VEER",
            partOfSpeech = "Verb",
            exampleSentence = "If you persevere, you will master English speaking.",
            exampleHindi = "यदि आप निरंतर प्रयास करेंगे, तो आप अंग्रेज़ी बोलना सीख जाएँगे।",
            difficulty = "Intermediate"
        ),
        // Advanced
        VocabularyWord(
            id = "v13",
            word = "Eloquent",
            hindiMeaning = "सुवक्ता / प्रभावशाली बोलने वाला",
            pronunciation = "EL-uh-kwunt",
            partOfSpeech = "Adjective",
            exampleSentence = "Her speech was so eloquent that everyone clapped.",
            exampleHindi = "उसका भाषण इतना प्रभावशाली था कि सभी ने तालियाँ बजाईं।",
            difficulty = "Advanced"
        )
    )

    val flashcardsList = listOf(
        FlashcardItem(
            id = "fc1",
            frontEnglish = "How have you been?",
            backHindi = "आप कैसे रहे हैं? / आपका हाल-चाल कैसा है?",
            pronunciation = "हाउ हैव यू बीन?",
            grammarTip = "Used when meeting someone after some time, more natural than just 'How are you?'",
            category = "Greetings"
        ),
        FlashcardItem(
            id = "fc2",
            frontEnglish = "I didn't catch that, could you repeat?",
            backHindi = "मैंने सुना नहीं, क्या आप दोबारा बोल सकते हैं?",
            pronunciation = "आई डिडन्ट कैच दैट, कुड यू रिपीट?",
            grammarTip = "Polite and conversational way to ask someone to repeat what they said.",
            category = "Daily Speaking"
        ),
        FlashcardItem(
            id = "fc3",
            frontEnglish = "What do you do for a living?",
            backHindi = "आप रोज़गार या काम के लिए क्या करते हैं?",
            pronunciation = "व्हाट डू यू डू फ़ॉर अ लिविंग?",
            grammarTip = "Much more native than asking 'What is your job?'",
            category = "Introductions"
        ),
        FlashcardItem(
            id = "fc4",
            frontEnglish = "I am looking forward to meeting you.",
            backHindi = "मैं आपसे मिलने के लिए बहुत उत्सुक हूँ।",
            pronunciation = "आई एम लुकिंग फ़ॉरवर्ड टू मीटिंग यू।",
            grammarTip = "'Looking forward to' is followed by -ing verb (meeting, seeing, talking).",
            category = "Formal & Social"
        ),
        FlashcardItem(
            id = "fc5",
            frontEnglish = "Could you give me a hand?",
            backHindi = "क्या आप मेरी थोड़ी मदद कर सकते हैं?",
            pronunciation = "कुड यू गिव मी अ हैंड?",
            grammarTip = "'Give a hand' is an everyday idiom meaning 'help me'.",
            category = "Requests"
        ),
        FlashcardItem(
            id = "fc6",
            frontEnglish = "It slipped my mind.",
            backHindi = "यह मेरे दिमाग़ से निकल गया / मैं भूल गया।",
            pronunciation = "इट स्लिप्ड माई माइंड।",
            grammarTip = "Natural conversational expression instead of saying 'I forgot'.",
            category = "Everyday Idioms"
        ),
        FlashcardItem(
            id = "fc7",
            frontEnglish = "Let's call it a day.",
            backHindi = "चलो आज का काम यहीं ख़त्म करते हैं।",
            pronunciation = "लेट्स कॉल इट अ डे।",
            grammarTip = "Commonly used in offices/studying to conclude work for the day.",
            category = "Work & Study"
        )
    )

    val scenariosList = listOf(
        ConversationScenario(
            id = "sc_meeting",
            title = "Meeting a New Person",
            hindiTitle = "किसी नए व्यक्ति से परिचय",
            iconName = "PersonAdd",
            description = "Learn how to break the ice, introduce yourself, and ask polite questions.",
            initialAiGreeting = "Hello there! Nice to meet you. My name is Alex. Are you new here?",
            contextPrompt = "Roleplay as a friendly stranger at a networking event or college campus.",
            samplePrompts = listOf(
                "Nice to meet you too! My name is...",
                "Yes, I just joined recently.",
                "Where are you from?"
            )
        ),
        ConversationScenario(
            id = "sc_restaurant",
            title = "At a Restaurant",
            hindiTitle = "रेस्टोरेंट में ऑर्डर करना",
            iconName = "Restaurant",
            description = "Practice ordering food, asking for water, and requesting the bill.",
            initialAiGreeting = "Welcome to Green Olive Bistro! Can I get you started with something to drink?",
            contextPrompt = "Roleplay as a polite restaurant waiter.",
            samplePrompts = listOf(
                "Could I see the menu, please?",
                "What do you recommend for lunch?",
                "I would like a glass of water, please."
            )
        ),
        ConversationScenario(
            id = "sc_shopping",
            title = "Shopping at a Mall",
            hindiTitle = "दुकान या मॉल में खरीदारी",
            iconName = "ShoppingBag",
            description = "Ask for sizes, discounts, and payment methods naturally.",
            initialAiGreeting = "Hi! Let me know if you need any help finding sizes today.",
            contextPrompt = "Roleplay as a helpful shop assistant in a clothing store.",
            samplePrompts = listOf(
                "Do you have this shirt in medium size?",
                "Is there any discount on this item?",
                "Where is the fitting room?"
            )
        ),
        ConversationScenario(
            id = "sc_interview",
            title = "Job Interview Practice",
            hindiTitle = "नौकरी का इंटरव्यू",
            iconName = "Work",
            description = "Practice answering classic interview questions with poise and confidence.",
            initialAiGreeting = "Good morning! Thanks for coming in. To start, could you please tell me a little about yourself?",
            contextPrompt = "Roleplay as an encouraging corporate HR interviewer.",
            samplePrompts = listOf(
                "Good morning! I have a background in...",
                "My key strengths are problem-solving and communication.",
                "I am very excited about this opportunity."
            )
        ),
        ConversationScenario(
            id = "sc_friend",
            title = "Catching up with a Friend",
            hindiTitle = "दोस्त से अनौपचारिक बातचीत",
            iconName = "Chat",
            description = "Informal chat about movies, plans, weekend, and feelings.",
            initialAiGreeting = "Hey! Long time no see! How have you been keeping lately?",
            contextPrompt = "Roleplay as an energetic close friend catching up.",
            samplePrompts = listOf(
                "Hey! I've been a bit busy with work.",
                "Did you watch the new cricket match?",
                "Let's grab a coffee this weekend!"
            )
        ),
        ConversationScenario(
            id = "sc_travel",
            title = "Railway Station / Airport",
            hindiTitle = "रेलवे स्टेशन या एयरपोर्ट पर",
            iconName = "DirectionsTransit",
            description = "Ask for platform details, check-in, timings, and ticket queries.",
            initialAiGreeting = "Information Desk, how may I assist you with your journey today?",
            contextPrompt = "Roleplay as an airport or railway help desk officer.",
            samplePrompts = listOf(
                "Which platform does the train arrive on?",
                "Is the flight running on time?",
                "Could you tell me where the ticket counter is?"
            )
        )
    )

    val pronunciationList = listOf(
        PronunciationChallenge(
            id = "p1",
            wordOrPhrase = "Comfortable",
            phoneticGuide = "KUMF-ter-bul (not com-for-table)",
            hindiTip = "हिंदी वक्ताओं की आम भूल: इसे 'कम-फ़ॉर-टेबल' नहीं, बल्कि 'कम्फ़-टर-बल' बोलें। 'or' साइलेंट सा रहता है।",
            commonMistake = "Com-for-table",
            difficulty = "Medium"
        ),
        PronunciationChallenge(
            id = "p2",
            wordOrPhrase = "Wednesday",
            phoneticGuide = "WENZ-day (not wed-nes-day)",
            hindiTip = "यहाँ 'd' साइलेंट रहता है। इसे सीधा 'वेन्ज़-डे' बोलें।",
            commonMistake = "Wed-nes-day",
            difficulty = "Easy"
        ),
        PronunciationChallenge(
            id = "p3",
            wordOrPhrase = "Schedule",
            phoneticGuide = "SKED-jool (US) or SHED-yool (UK)",
            hindiTip = "अक्सर लोग इसे 'स्के-डूल' कहते हैं। US में 'स्के-जूल' और UK में 'शे-ड्यूल' बोला जाता है।",
            commonMistake = "Ske-dule",
            difficulty = "Medium"
        ),
        PronunciationChallenge(
            id = "p4",
            wordOrPhrase = "Receipt",
            phoneticGuide = "rih-SEET (not re-sipt)",
            hindiTip = "यहाँ 'p' पूरी तरह साइलेंट होता है। आवाज़ 'रि-सीट' जैसी होती है।",
            commonMistake = "Re-ceept / Re-sipt",
            difficulty = "Medium"
        ),
        PronunciationChallenge(
            id = "p5",
            wordOrPhrase = "Entrepreneur",
            phoneticGuide = "on-truh-pruh-NUR",
            hindiTip = "यह फ्रेंच मूल का शब्द है। इसे 'ऑन-ट्र-प्र-नर' उच्चारित करें।",
            commonMistake = "Enter-pree-ner",
            difficulty = "Challenging"
        ),
        PronunciationChallenge(
            id = "p6",
            wordOrPhrase = "Vegetable",
            phoneticGuide = "VEJ-tuh-bul (not ve-gee-table)",
            hindiTip = "'e' बीच में ड्रॉप हो जाता है। इसे 'वेज-ट-बल' बोलें, न कि 'वेजी-टेबल'।",
            commonMistake = "Ve-ji-ta-ble",
            difficulty = "Easy"
        ),
        PronunciationChallenge(
            id = "p7",
            wordOrPhrase = "Specific",
            phoneticGuide = "spuh-SIF-ik (not pacific)",
            hindiTip = "शुरुआत 'स्प' (S) से होती है, 'पैसिफ़िक' (P) से नहीं। 'स्प-सिफ़िक' बोलें।",
            commonMistake = "Pacific",
            difficulty = "Challenging"
        )
    )

    val dailyMotivationalTips = listOf(
        Pair("हर रोज़ 15 मिनट बोलने का अभ्यास आपको धाराप्रवाह बना सकता है!", "15 minutes of daily speaking practice can make you fluent!"),
        Pair("ग़लतियों से मत डरिए; हर ग़लती सीखने का एक नया क़दम है।", "Don't fear mistakes; each mistake is a stepping stone to fluency."),
        Pair("अंग्रेज़ी सिर्फ़ एक भाषा है, बुद्धिमत्ता का पैमाना नहीं। आत्मविश्वास से बोलें!", "English is just a language, not a measure of intelligence. Speak with confidence!"),
        Pair("धीमी शुरुआत भी एक शुरुआत है। बस रोज़ बोलते रहिए।", "Slow progress is still progress. Just keep speaking daily.")
    )
}
