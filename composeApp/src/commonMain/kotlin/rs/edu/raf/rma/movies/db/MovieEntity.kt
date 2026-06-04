package rs.edu.raf.rma.movies.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val imdbId: String,
    val title: String,
    val year: Int,
    val imdbRating: Double?,
    val imdbVotes: Int?,
    val posterPath: String?,
)

@Entity(tableName = "movie_details")
data class MovieDetailEntity(
    @PrimaryKey val imdbId: String,
    val tmdbId: Int?,
    val title: String,
    val originalTitle: String?,
    val overview: String?,
    val tagline: String?,
    val releaseDate: String?,
    val year: Int,
    val runtime: Int?,
    val languageCode: String?,
    val imdbRating: Double?,
    val imdbVotes: Int?,
    val tmdbRating: Double?,
    val tmdbVotes: Int?,
    val posterPath: String?,
    val backdropPath: String?,
)

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String,
)

@Entity(
    tableName = "movie_genre_cross_ref",
    primaryKeys = ["imdbId", "genreId"],
)
data class MovieGenreCrossRef(
    val imdbId: String,
    val genreId: Int,
)

@Entity(tableName = "actors")
data class ActorEntity(
    @PrimaryKey val imdbId: String,
    val name: String,
    val profilePath: String?,
    val department: String?,
)

@Entity(
    tableName = "movie_actor_cross_ref",
    primaryKeys = ["movieImdbId", "actorImdbId", "order"],
)
data class MovieActorCrossRef(
    val movieImdbId: String,
    val actorImdbId: String,
    val order: Int,
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val imdbId: String,
    val title: String,
    val year: Int,
    val imdbRating: Double?,
    val posterPath: String?,
    val addedAt: Long = 0L,
)

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val imdbId: String,
    val title: String,
    val year: Int,
    val imdbRating: Double?,
    val posterPath: String?,
    val addedAt: Long = 0L,
)

@Entity(tableName = "quiz_sessions")
data class QuizSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val score: Double,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timeUsedSeconds: Int,
    val playedAt: Long,
)
