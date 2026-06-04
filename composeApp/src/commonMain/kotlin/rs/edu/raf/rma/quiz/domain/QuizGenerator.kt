package rs.edu.raf.rma.quiz.domain

import kotlinx.coroutines.flow.first
import rs.edu.raf.rma.movies.db.MovieDao
import rs.edu.raf.rma.movies.db.MovieWithGenres
import rs.edu.raf.rma.networking.MoviesApi

class QuizGenerator(
    private val dao: MovieDao,
    private val api: MoviesApi,
) {
    companion object {
        const val SESSION_SIZE = 10
        const val MAX_TYPE_COUNT = 4
        private const val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    }

    suspend fun generateSession(): List<QuizQuestion> {
        val allMovies = dao.observeMovies().first()
        val moviesWithImages = allMovies.filter { it.movie.posterPath != null }

        if (moviesWithImages.size < SESSION_SIZE) {
            error("Browse the catalog first to populate your quiz pool.")
        }

        val shuffled = moviesWithImages.shuffled()
        val questions = mutableListOf<QuizQuestion>()
        val usedMovieIds = mutableSetOf<String>()
        val usedImages = mutableSetOf<String>()

        val typeSequence = buildTypeSequence()

        for (type in typeSequence) {
            if (questions.size >= SESSION_SIZE) break

            val candidate = shuffled.firstOrNull { it.movie.imdbId !in usedMovieIds } ?: break

            val question = when (type) {
                0 -> generateGuessMovie(candidate, shuffled, usedImages)
                1 -> generateGuessYear(candidate, shuffled)
                2 -> generateGuessActor(candidate, shuffled)
                else -> null
            } ?: continue

            questions.add(question)
            usedMovieIds.add(candidate.movie.imdbId)
            if (type == 0) {
                candidate.movie.posterPath?.let { usedImages.add(it) }
            }
        }

        // Fallback: if some question types failed (e.g. actor API calls), fill with GuessTheYear
        // which never makes network calls and always succeeds.
        if (questions.size < SESSION_SIZE) {
            for (movie in shuffled) {
                if (questions.size >= SESSION_SIZE) break
                if (movie.movie.imdbId in usedMovieIds) continue
                questions.add(generateGuessYear(movie, shuffled))
                usedMovieIds.add(movie.movie.imdbId)
            }
        }

        if (questions.size < SESSION_SIZE) {
            error("Browse the catalog first to populate your quiz pool.")
        }

        return questions.take(SESSION_SIZE)
    }

    private fun buildTypeSequence(): List<Int> {
        val types = mutableListOf<Int>()
        repeat(MAX_TYPE_COUNT) { types.add(0) }
        repeat(MAX_TYPE_COUNT) { types.add(1) }
        repeat(MAX_TYPE_COUNT) { types.add(2) }
        return types.shuffled()
    }

    private fun generateGuessMovie(
        movie: MovieWithGenres,
        allMovies: List<MovieWithGenres>,
        usedImages: Set<String>,
    ): QuizQuestion.GuessTheMovie? {
        val posterPath = movie.movie.posterPath ?: return null
        if (posterPath in usedImages) return null
        val imageUrl = "$IMAGE_BASE$posterPath"

        val distractors = allMovies
            .filter { it.movie.imdbId != movie.movie.imdbId }
            .shuffled()
            .take(3)
            .map { it.movie.title }

        if (distractors.size < 3) return null

        val options = (distractors + movie.movie.title).shuffled()
        return QuizQuestion.GuessTheMovie(
            movieId = movie.movie.imdbId,
            imageUrl = imageUrl,
            options = options,
            correctAnswer = movie.movie.title,
        )
    }

    private fun generateGuessYear(
        movie: MovieWithGenres,
        allMovies: List<MovieWithGenres>,
    ): QuizQuestion.GuessTheYear {
        val correctYear = movie.movie.year
        val wrongYears = generateDistinctOffsets(correctYear, count = 3)
        val options = (wrongYears.map { it.toString() } + correctYear.toString()).shuffled()

        return QuizQuestion.GuessTheYear(
            movieId = movie.movie.imdbId,
            posterUrl = movie.movie.posterPath?.let { "$IMAGE_BASE$it" },
            title = movie.movie.title,
            options = options,
            correctAnswer = correctYear.toString(),
        )
    }

    private suspend fun generateGuessActor(
        movie: MovieWithGenres,
        allMovies: List<MovieWithGenres>,
    ): QuizQuestion.GuessTheLeadActor? {
        return try {
            val castResponse = api.getMovieCast(movie.movie.imdbId, pageSize = 10)
            val cast = castResponse.items
            if (cast.isEmpty()) return null

            val correctActor = cast.take(3).random().name

            val distractors = allMovies
                .filter { it.movie.imdbId != movie.movie.imdbId }
                .shuffled()
                .take(5)
                .flatMap { m ->
                    try {
                        api.getMovieCast(m.movie.imdbId, pageSize = 5).items.map { it.name }
                    } catch (_: Exception) {
                        emptyList()
                    }
                }
                .filter { it != correctActor }
                .distinct()
                .shuffled()
                .take(3)

            if (distractors.size < 3) return null

            val options = (distractors + correctActor).shuffled()
            QuizQuestion.GuessTheLeadActor(
                movieId = movie.movie.imdbId,
                posterUrl = movie.movie.posterPath?.let { "$IMAGE_BASE$it" },
                title = movie.movie.title,
                options = options,
                correctAnswer = correctActor,
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun generateDistinctOffsets(base: Int, count: Int): List<Int> {
        val result = mutableListOf<Int>()
        val offsets = listOf(1, -1, 2, -2, 3, -3, 5, -5, 7, -7, 10, -10)
        for (offset in offsets.shuffled()) {
            val candidate = base + offset
            if (candidate != base && candidate > 1900 && candidate !in result) {
                result.add(candidate)
                if (result.size == count) break
            }
        }
        var extra = 1
        while (result.size < count) {
            val candidate = base + extra * 11
            if (candidate !in result && candidate != base) result.add(candidate)
            extra++
        }
        return result
    }
}
