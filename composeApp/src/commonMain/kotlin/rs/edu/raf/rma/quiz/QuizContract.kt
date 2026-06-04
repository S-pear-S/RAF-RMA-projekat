package rs.edu.raf.rma.quiz

import rs.edu.raf.rma.quiz.domain.QuizQuestion

data class QuizState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val answers: Map<Int, String> = emptyMap(),
    val revealedAnswer: Boolean = false,
    val selectedAnswer: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isFinished: Boolean = false,
    val timeLeftSeconds: Int = 60,
    val showAbandonDialog: Boolean = false,
) {
    val currentQuestion: QuizQuestion? get() = questions.getOrNull(currentIndex)
    val totalQuestions: Int get() = questions.size
    val isLastQuestion: Boolean get() = currentIndex == totalQuestions - 1
    val correctCount: Int get() = answers.count { (idx, ans) -> questions.getOrNull(idx)?.correctAnswer == ans }
    val score: Double get() {
        val timeBonus = timeLeftSeconds.toDouble() / 60.0
        val raw = correctCount * (9.0 + timeBonus)
        return minOf(raw, 100.0)
    }
    val timeUsedSeconds: Int get() = 60 - timeLeftSeconds
}

sealed interface QuizEvent {
    data object StartQuiz : QuizEvent
    data class SelectAnswer(val answer: String) : QuizEvent
    data object NextQuestion : QuizEvent
    data object RequestAbandon : QuizEvent
    data object ConfirmAbandon : QuizEvent
    data object DismissAbandon : QuizEvent
    data object TimerTick : QuizEvent
}

sealed interface QuizEffect {
    data class NavigateToResult(
        val score: Double,
        val correct: Int,
        val total: Int,
        val timeUsedSeconds: Int,
    ) : QuizEffect
    data object NavigateBack : QuizEffect
}
