package rs.edu.raf.rma.movies.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class MovieWithGenres(
    @Embedded val movie: MovieEntity,
    @Relation(
        parentColumn = "imdbId",
        entityColumn = "id",
        associateBy = Junction(
            value = MovieGenreCrossRef::class,
            parentColumn = "imdbId",
            entityColumn = "genreId",
        ),
    )
    val genres: List<GenreEntity>,
)

data class MovieBackdrop(val imdbId: String, val backdropPath: String)

data class MovieDetailWithGenresAndActors(
    @Embedded val detail: MovieDetailEntity,
    @Relation(
        parentColumn = "imdbId",
        entityColumn = "id",
        associateBy = Junction(
            value = MovieGenreCrossRef::class,
            parentColumn = "imdbId",
            entityColumn = "genreId",
        ),
    )
    val genres: List<GenreEntity>,
    @Relation(
        parentColumn = "imdbId",
        entityColumn = "imdbId",
        associateBy = Junction(
            value = MovieActorCrossRef::class,
            parentColumn = "movieImdbId",
            entityColumn = "actorImdbId",
        ),
    )
    val actors: List<ActorEntity>,
)

@Dao
interface MovieDao {

    // --- Movies List ---

    @Transaction
    @Query("SELECT * FROM movies ORDER BY imdbRating DESC NULLS LAST")
    fun observeMovies(): Flow<List<MovieWithGenres>>

    @Transaction
    @Query("""
        SELECT * FROM movies
        WHERE (:query IS NULL OR title LIKE '%' || :query || '%')
        AND (:genreId IS NULL OR imdbId IN (SELECT imdbId FROM movie_genre_cross_ref WHERE genreId = :genreId))
        AND (:minYear IS NULL OR year >= :minYear)
        AND (:maxYear IS NULL OR year <= :maxYear)
        AND (:minRating IS NULL OR imdbRating >= :minRating)
        ORDER BY
            CASE WHEN :sortBy = 'title' AND :sortOrder = 'asc' THEN title END ASC,
            CASE WHEN :sortBy = 'title' AND :sortOrder = 'desc' THEN title END DESC,
            CASE WHEN :sortBy = 'year' AND :sortOrder = 'asc' THEN year END ASC,
            CASE WHEN :sortBy = 'year' AND :sortOrder = 'desc' THEN year END DESC,
            CASE WHEN :sortBy = 'imdb_rating' OR :sortBy IS NULL THEN imdbRating END DESC
    """)
    fun moviesPagingSource(
        query: String? = null,
        genreId: Int? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        minRating: Double? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
    ): PagingSource<Int, MovieWithGenres>

    @Query("SELECT COUNT(*) FROM movies WHERE posterPath IS NOT NULL")
    suspend fun countMoviesWithImages(): Int

    // --- Movie Detail ---

    @Transaction
    @Query("SELECT * FROM movie_details WHERE imdbId = :id")
    fun observeMovieDetail(id: String): Flow<MovieDetailWithGenresAndActors?>

    // --- Upsert ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovieDetail(detail: MovieDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGenres(genres: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovieGenreCrossRefs(refs: List<MovieGenreCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertActors(actors: List<ActorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovieActorCrossRefs(refs: List<MovieActorCrossRef>)

    @Transaction
    suspend fun upsertMoviesWithGenres(movies: List<MovieEntity>, genreRefs: List<MovieGenreCrossRef>) {
        upsertMovies(movies)
        upsertMovieGenreCrossRefs(genreRefs)
    }

    // --- Favorites ---

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun observeFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE imdbId = :id)")
    fun observeIsFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFavorite(favorite: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFavorites(favorites: List<FavoriteEntity>)

    @Query("DELETE FROM favorites WHERE imdbId = :id")
    suspend fun deleteFavorite(id: String)

    @Query("DELETE FROM favorites")
    suspend fun clearFavorites()

    @Query("SELECT COUNT(*) FROM favorites")
    fun observeFavoritesCount(): Flow<Int>

    // --- Watchlist ---

    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun observeWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE imdbId = :id)")
    fun observeIsOnWatchlist(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWatchlistItem(item: WatchlistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWatchlistItems(items: List<WatchlistEntity>)

    @Query("DELETE FROM watchlist WHERE imdbId = :id")
    suspend fun deleteWatchlistItem(id: String)

    @Query("DELETE FROM watchlist")
    suspend fun clearWatchlist()

    @Query("SELECT COUNT(*) FROM watchlist")
    fun observeWatchlistCount(): Flow<Int>

    // --- Genres ---

    @Query("SELECT * FROM genres ORDER BY name ASC")
    fun observeGenres(): Flow<List<GenreEntity>>

    // --- Quiz Sessions ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizSession(session: QuizSessionEntity)

    @Query("SELECT MAX(score) FROM quiz_sessions")
    fun observeBestScore(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM quiz_sessions")
    fun observeQuizCount(): Flow<Int>

    @Query("DELETE FROM quiz_sessions")
    suspend fun clearQuizSessions()

    // --- Backdrop lookup for quiz ---

    @Query("SELECT imdbId, backdropPath FROM movie_details WHERE backdropPath IS NOT NULL")
    suspend fun getMovieBackdropPaths(): List<MovieBackdrop>
}
