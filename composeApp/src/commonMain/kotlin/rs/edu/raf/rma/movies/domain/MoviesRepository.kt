package rs.edu.raf.rma.movies.domain

import kotlinx.coroutines.flow.Flow

data class MovieFilter(
    val query: String? = null,
    val genreId: Int? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val minRating: Double? = null,
    val sortBy: String? = null,
    val sortOrder: String? = null,
)

interface MoviesRepository {
    fun observeMovies(filter: MovieFilter = MovieFilter()): Flow<List<Movie>>
    fun observeMovieDetails(id: String): Flow<MovieDetails?>
    suspend fun refreshMovies(page: Int = 1, filter: MovieFilter = MovieFilter()): Boolean
    suspend fun refreshMovieDetails(id: String)
    suspend fun countMoviesWithImages(): Int
    fun observeFavorites(): Flow<List<Movie>>
    fun observeWatchlist(): Flow<List<Movie>>
    fun observeIsFavorite(id: String): Flow<Boolean>
    fun observeIsOnWatchlist(id: String): Flow<Boolean>
    suspend fun syncFavorites()
    suspend fun syncWatchlist()
    suspend fun addFavorite(movie: Movie)
    suspend fun removeFavorite(id: String)
    suspend fun addToWatchlist(movie: Movie)
    suspend fun removeFromWatchlist(id: String)
    fun observeFavoritesCount(): Flow<Int>
    fun observeWatchlistCount(): Flow<Int>
    suspend fun clearUserData()
    fun observeBestQuizScore(): Flow<Double?>
    fun observeQuizCount(): Flow<Int>
    suspend fun saveQuizSession(score: Double, correctAnswers: Int, totalQuestions: Int, timeUsedSeconds: Int)
}
