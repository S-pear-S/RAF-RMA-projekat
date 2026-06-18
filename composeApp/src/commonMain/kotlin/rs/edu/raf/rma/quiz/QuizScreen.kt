package rs.edu.raf.rma.quiz

import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.rma.quiz.domain.QuizQuestion

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QuizScreen(
    onResult: (score: Double, correct: Int, total: Int, timeUsed: Int) -> Unit,
    onBack: () -> Unit,
    onGoToMovies: () -> Unit,
    viewModel: QuizViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is QuizEffect.NavigateToResult -> onResult(effect.score, effect.correct, effect.total, effect.timeUsedSeconds)
                QuizEffect.NavigateBack -> onBack()
            }
        }
    }

    BackHandler(enabled = state.currentQuestion != null) {
        viewModel.onEvent(QuizEvent.RequestAbandon)
    }

    if (state.showAbandonDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(QuizEvent.DismissAbandon) },
            title = { Text("Napusti kviz?") },
            text = { Text("Abandon quiz? Your progress will be lost.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(QuizEvent.ConfirmAbandon) }) { Text("Napusti") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(QuizEvent.DismissAbandon) }) { Text("Nastavi") }
            },
        )
    }

    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(16.dp))
                        Text("Pripremamo pitanja...")
                    }
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = state.error!!,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error,
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = onGoToMovies) {
                            Text("Idi na katalog")
                        }
                    }
                }

                state.currentQuestion != null -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        QuizHeader(
                            current = state.currentIndex + 1,
                            total = state.totalQuestions,
                            timeLeft = state.timeLeftSeconds,
                            onAbandon = { viewModel.onEvent(QuizEvent.RequestAbandon) },
                        )

                        AnimatedContent(
                            targetState = state.currentIndex,
                            transitionSpec = {
                                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                            },
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                        ) { _ ->
                            QuizQuestionContent(
                                question = state.currentQuestion!!,
                                selectedAnswer = state.selectedAnswer,
                                revealedAnswer = state.revealedAnswer,
                                onAnswer = { viewModel.onEvent(QuizEvent.SelectAnswer(it)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizHeader(current: Int, total: Int, timeLeft: Int, onAbandon: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Pitanje $current / $total",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${timeLeft}s",
                style = MaterialTheme.typography.titleMedium,
                color = if (timeLeft <= 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { current.toFloat() / total },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun QuizQuestionContent(
    question: QuizQuestion,
    selectedAnswer: String?,
    revealedAnswer: Boolean,
    onAnswer: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (question) {
            is QuizQuestion.GuessTheMovie -> {
                Text(
                    text = "Koji je ovo film?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                AsyncImage(
                    model = question.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                )
            }

            is QuizQuestion.GuessTheYear -> {
                Text(
                    text = "Koje je godine izašao \"${question.title}\"?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                if (question.posterUrl != null) {
                    AsyncImage(
                        model = question.posterUrl,
                        contentDescription = question.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(width = 120.dp, height = 180.dp),
                    )
                }
            }

            is QuizQuestion.GuessTheLeadActor -> {
                Text(
                    text = "Ko glumi u filmu \"${question.title}\"?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                if (question.posterUrl != null) {
                    AsyncImage(
                        model = question.posterUrl,
                        contentDescription = question.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(width = 120.dp, height = 180.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            question.options.forEach { option ->
                AnswerButton(
                    text = option,
                    isSelected = selectedAnswer == option,
                    isCorrect = option == question.correctAnswer,
                    isRevealed = revealedAnswer,
                    onClick = { if (!revealedAnswer) onAnswer(option) },
                )
            }
        }
    }
}

@Composable
private fun AnswerButton(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isRevealed: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = when {
        !isRevealed -> MaterialTheme.colorScheme.surface
        isCorrect -> Color(0xFF4CAF50)
        isSelected && !isCorrect -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.surface
    }

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = if (isRevealed && (isCorrect || isSelected)) Color.White else MaterialTheme.colorScheme.onSurface,
        )
    }
}
