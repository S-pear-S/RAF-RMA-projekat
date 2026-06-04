package rs.edu.raf.rma.quiz.domain

sealed class QuizQuestion {
    abstract val movieId: String
    abstract val options: List<String>
    abstract val correctAnswer: String

    data class GuessTheMovie(
        override val movieId: String,
        val imageUrl: String,
        override val options: List<String>,
        override val correctAnswer: String,
    ) : QuizQuestion()

    data class GuessTheYear(
        override val movieId: String,
        val posterUrl: String?,
        val title: String,
        override val options: List<String>,
        override val correctAnswer: String,
    ) : QuizQuestion()

    data class GuessTheLeadActor(
        override val movieId: String,
        val posterUrl: String?,
        val title: String,
        override val options: List<String>,
        override val correctAnswer: String,
    ) : QuizQuestion()
}
