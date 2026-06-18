package rs.edu.raf.rma.movies.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import rs.edu.raf.rma.movies.db.MovieActorCrossRef
import rs.edu.raf.rma.movies.db.MovieDao
import rs.edu.raf.rma.movies.db.QuizSessionEntity
import rs.edu.raf.rma.movies.domain.Genre
import rs.edu.raf.rma.movies.domain.Movie
import rs.edu.raf.rma.movies.domain.MovieDetails
import rs.edu.raf.rma.movies.domain.MovieFilter
import rs.edu.raf.rma.movies.domain.MoviesRepository
import kotlin.time.Clock
import rs.edu.raf.rma.networking.MoviesApi
import rs.edu.raf.rma.networking.di.Qualifiers

class MoviesRepositoryImpl(
    private val api: MoviesApi,
    private val authApi: MoviesApi,
    private val dao: MovieDao,
) : MoviesRepository {

    override fun moviesPager(filter: MovieFilter): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                dao.moviesPagingSource(
                    query = filter.query,
                    genreId = filter.genreId,
                    minYear = filter.minYear,
                    maxYear = filter.maxYear,
                    minRating = filter.minRating,
                    sortBy = filter.sortBy,
                    sortOrder = filter.sortOrder,
                )
            },
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }

    override fun observeMovieDetails(id: String): Flow<MovieDetails?> {
        return combine(
            dao.observeMovieDetail(id),
            dao.observeIsFavorite(id),
            dao.observeIsOnWatchlist(id),
        ) { detail, isFav, isWl ->
            detail?.toDomain(isFavorite = isFav, isOnWatchlist = isWl)
        }
    }

    override suspend fun refreshMovies(page: Int, filter: MovieFilter): Boolean {
        val response = api.getMovies(
            page = page,
            pageSize = 20,
            query = filter.query,
            genreId = filter.genreId,
            minYear = filter.minYear,
            maxYear = filter.maxYear,
            minRating = filter.minRating,
            sortBy = filter.sortBy,
            sortOrder = filter.sortOrder,
        )
        val movies = response.items
        val entities = movies.map { it.toEntity() }
        val genres = movies.flatMap { it.toGenreEntities() }.distinctBy { it.id }
        val crossRefs = movies.flatMap { it.toGenreCrossRefs() }
        dao.upsertGenres(genres)
        dao.upsertMoviesWithGenres(entities, crossRefs)
        return response.page < response.totalPages
    }

    override suspend fun refreshMovieDetails(id: String) {
        val movie = api.getMovie(id)
        val detail = movie.toDetailEntity()
        val movieEntity = movie.toMovieEntity()
        val genres = movie.toGenreEntities()
        val crossRefs = movie.toGenreCrossRefs()

        dao.upsertGenres(genres)
        dao.upsertMovies(listOf(movieEntity))
        dao.upsertMovieGenreCrossRefs(crossRefs)
        dao.upsertMovieDetail(detail)

        try {
            val castResponse = api.getMovieCast(id, pageSize = 10)
            val actors = castResponse.items.map { it.toEntity() }
            val actorRefs = castResponse.items.mapIndexed { index, actor ->
                MovieActorCrossRef(
                    movieImdbId = id,
                    actorImdbId = actor.imdbId,
                    order = index,
                )
            }
            dao.upsertActors(actors)
            dao.upsertMovieActorCrossRefs(actorRefs)
        } catch (_: Exception) {
        }
    }

    override suspend fun countMoviesWithImages(): Int = dao.countMoviesWithImages()

    override fun observeGenres(): Flow<List<Genre>> =
        dao.observeGenres().map { list -> list.map { Genre(it.id, it.name) } }

    override fun observeFavorites(): Flow<List<Movie>> =
        dao.observeFavorites().map { list -> list.map { it.toDomain() } }

    override fun observeWatchlist(): Flow<List<Movie>> =
        dao.observeWatchlist().map { list -> list.map { it.toDomain() } }

    override fun observeIsFavorite(id: String): Flow<Boolean> = dao.observeIsFavorite(id)

    override fun observeIsOnWatchlist(id: String): Flow<Boolean> = dao.observeIsOnWatchlist(id)

    override suspend fun syncFavorites() {
        val items = authApi.getFavorites()
        val now = Clock.System.now().toEpochMilliseconds()
        dao.clearFavorites()
        dao.upsertFavorites(items.mapIndexed { i, it -> it.toFavoriteEntity(now - i) })
        val movies = items.map { it.toEntity() }
        val genres = items.flatMap { it.toGenreEntities() }.distinctBy { it.id }
        val crossRefs = items.flatMap { it.toGenreCrossRefs() }
        dao.upsertGenres(genres)
        dao.upsertMoviesWithGenres(movies, crossRefs)
    }

    override suspend fun syncWatchlist() {
        val items = authApi.getWatchlist()
        val now = Clock.System.now().toEpochMilliseconds()
        dao.clearWatchlist()
        dao.upsertWatchlistItems(items.mapIndexed { i, it -> it.toWatchlistEntity(now - i) })
        val movies = items.map { it.toEntity() }
        val genres = items.flatMap { it.toGenreEntities() }.distinctBy { it.id }
        val crossRefs = items.flatMap { it.toGenreCrossRefs() }
        dao.upsertGenres(genres)
        dao.upsertMoviesWithGenres(movies, crossRefs)
    }

    override suspend fun addFavorite(movie: Movie) {
        authApi.addFavorite(movie.imdbId)
        dao.upsertFavorite(movie.toFavoriteEntity(Clock.System.now().toEpochMilliseconds()))
    }

    override suspend fun removeFavorite(id: String) {
        authApi.removeFavorite(id)
        dao.deleteFavorite(id)
    }

    override suspend fun addToWatchlist(movie: Movie) {
        authApi.addToWatchlist(movie.imdbId)
        dao.upsertWatchlistItem(movie.toWatchlistEntity(Clock.System.now().toEpochMilliseconds()))
    }

    override suspend fun removeFromWatchlist(id: String) {
        authApi.removeFromWatchlist(id)
        dao.deleteWatchlistItem(id)
    }

    override fun observeFavoritesCount(): Flow<Int> = dao.observeFavoritesCount()

    override fun observeWatchlistCount(): Flow<Int> = dao.observeWatchlistCount()

    override suspend fun clearUserData() {
        dao.clearFavorites()
        dao.clearWatchlist()
        dao.clearQuizSessions()
    }

    override fun observeBestQuizScore(): Flow<Double?> = dao.observeBestScore()

    override fun observeQuizCount(): Flow<Int> = dao.observeQuizCount()

    override suspend fun saveQuizSession(
        score: Double,
        correctAnswers: Int,
        totalQuestions: Int,
        timeUsedSeconds: Int,
    ) {
        dao.insertQuizSession(
            QuizSessionEntity(
                score = score,
                correctAnswers = correctAnswers,
                totalQuestions = totalQuestions,
                timeUsedSeconds = timeUsedSeconds,
                playedAt = Clock.System.now().toEpochMilliseconds(),
            )
        )
    }
}
