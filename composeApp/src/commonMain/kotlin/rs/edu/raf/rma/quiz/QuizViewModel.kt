package rs.edu.raf.rma.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.rma.movies.domain.MoviesRepository
import rs.edu.raf.rma.quiz.domain.QuizGenerator

class QuizViewModel(
    private val generator: QuizGenerator,
    private val repository: MoviesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    private val _effects = Channel<QuizEffect>()
    val effects = _effects.receiveAsFlow()

    private var timerJob: Job? = null

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val questions = generator.generateSession()
                _state.update { it.copy(questions = questions, isLoading = false) }
                startTimer()
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Greška pri generisanju kviza.") }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.timeLeftSeconds > 0 && !_state.value.isFinished) {
                delay(1000)
                _state.update { it.copy(timeLeftSeconds = it.timeLeftSeconds - 1) }
                if (_state.value.timeLeftSeconds <= 0) {
                    finishQuiz()
                }
            }
        }
    }

    fun onEvent(event: QuizEvent) {
        if (event is QuizEvent.SelectAnswer) {
            val state = _state.value
            if (state.revealedAnswer || state.currentQuestion == null) return
        }

        _state.update { reduce(it, event) }

        when (event) {
            QuizEvent.StartQuiz -> loadQuestions()

            is QuizEvent.SelectAnswer -> viewModelScope.launch {
                delay(1200)
                advanceQuestion()
            }

            QuizEvent.NextQuestion -> advanceQuestion()

            QuizEvent.ConfirmAbandon -> {
                timerJob?.cancel()
                viewModelScope.launch {
                    _effects.send(QuizEffect.NavigateBack)
                }
            }

            QuizEvent.RequestAbandon, QuizEvent.DismissAbandon, QuizEvent.TimerTick -> Unit
        }
    }

    private fun advanceQuestion() {
        val state = _state.value
        if (state.isLastQuestion) {
            finishQuiz()
        } else {
            _state.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    revealedAnswer = false,
                    selectedAnswer = null,
                )
            }
        }
    }

    private fun finishQuiz() {
        timerJob?.cancel()
        val state = _state.value
        if (state.isFinished) return
        _state.update { it.copy(isFinished = true) }

        viewModelScope.launch {
            val score = state.score
            val correct = state.correctCount
            val total = state.totalQuestions
            val timeUsed = state.timeUsedSeconds

            try {
                repository.saveQuizSession(score, correct, total, timeUsed)
            } catch (_: Exception) {
            }

            _effects.send(
                QuizEffect.NavigateToResult(
                    score = score,
                    correct = correct,
                    total = total,
                    timeUsedSeconds = timeUsed,
                )
            )
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}

private fun reduce(state: QuizState, event: QuizEvent): QuizState = when (event) {
    is QuizEvent.SelectAnswer -> state.copy(
        selectedAnswer = event.answer,
        revealedAnswer = true,
        answers = state.answers + (state.currentIndex to event.answer),
    )
    QuizEvent.RequestAbandon -> state.copy(showAbandonDialog = true)
    QuizEvent.DismissAbandon -> state.copy(showAbandonDialog = false)
    QuizEvent.StartQuiz, QuizEvent.NextQuestion, QuizEvent.ConfirmAbandon, QuizEvent.TimerTick -> state
}
