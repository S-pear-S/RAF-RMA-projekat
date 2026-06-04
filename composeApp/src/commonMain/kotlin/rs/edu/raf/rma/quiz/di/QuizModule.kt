package rs.edu.raf.rma.quiz.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import rs.edu.raf.rma.networking.MoviesApi
import rs.edu.raf.rma.networking.di.Qualifiers
import rs.edu.raf.rma.quiz.QuizViewModel
import rs.edu.raf.rma.quiz.domain.QuizGenerator

val quizModule = module {
    factory {
        QuizGenerator(
            dao = get(),
            api = get<MoviesApi>(Qualifiers.Authenticated),
        )
    }
    viewModel { QuizViewModel(generator = get(), repository = get()) }
}
