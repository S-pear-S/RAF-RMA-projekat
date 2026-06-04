package rs.edu.raf.rma.movies.data

import rs.edu.raf.rma.movies.db.ActorEntity
import rs.edu.raf.rma.movies.db.FavoriteEntity
import rs.edu.raf.rma.movies.db.GenreEntity
import rs.edu.raf.rma.movies.db.MovieActorCrossRef
import rs.edu.raf.rma.movies.db.MovieDetailEntity
import rs.edu.raf.rma.movies.db.MovieDetailWithGenresAndActors
import rs.edu.raf.rma.movies.db.MovieEntity
import rs.edu.raf.rma.movies.db.MovieGenreCrossRef
import rs.edu.raf.rma.movies.db.MovieWithGenres
import rs.edu.raf.rma.movies.db.WatchlistEntity
import rs.edu.raf.rma.movies.domain.Actor
import rs.edu.raf.rma.movies.domain.Genre
import rs.edu.raf.rma.movies.domain.Movie
import rs.edu.raf.rma.movies.domain.MovieDetails
import rs.edu.raf.rma.networking.model.ActorApiModel
import rs.edu.raf.rma.networking.model.MovieApiModel
import rs.edu.raf.rma.networking.model.MovieListItemApiModel

fun MovieListItemApiModel.toEntity() = MovieEntity(
    imdbId = imdbId,
    title = title,
    year = year,
    imdbRating = imdbRating,
    imdbVotes = imdbVotes,
    posterPath = posterPath,
)

fun MovieListItemApiModel.toGenreEntities() = genres.map { GenreEntity(it.id, it.name) }

fun MovieListItemApiModel.toGenreCrossRefs() = genres.map { MovieGenreCrossRef(imdbId, it.id) }

fun MovieApiModel.toDetailEntity() = MovieDetailEntity(
    imdbId = imdbId,
    tmdbId = tmdbId,
    title = title,
    originalTitle = originalTitle,
    overview = overview,
    tagline = tagline,
    releaseDate = releaseDate,
    year = year,
    runtime = runtime,
    languageCode = languageCode,
    imdbRating = imdbRating,
    imdbVotes = imdbVotes,
    tmdbRating = tmdbRating,
    tmdbVotes = tmdbVotes,
    posterPath = posterPath,
    backdropPath = backdropPath,
)

fun MovieApiModel.toMovieEntity() = MovieEntity(
    imdbId = imdbId,
    title = title,
    year = year,
    imdbRating = imdbRating,
    imdbVotes = imdbVotes,
    posterPath = posterPath,
)

fun MovieApiModel.toGenreEntities() = genres.map { GenreEntity(it.id, it.name) }

fun MovieApiModel.toGenreCrossRefs() = genres.map { MovieGenreCrossRef(imdbId, it.id) }

fun ActorApiModel.toEntity() = ActorEntity(
    imdbId = imdbId,
    name = name,
    profilePath = profilePath,
    department = department,
)

fun MovieWithGenres.toDomain() = Movie(
    imdbId = movie.imdbId,
    title = movie.title,
    year = movie.year,
    imdbRating = movie.imdbRating,
    posterPath = movie.posterPath,
    genres = genres.map { Genre(it.id, it.name) },
)

fun MovieDetailWithGenresAndActors.toDomain(
    isFavorite: Boolean = false,
    isOnWatchlist: Boolean = false,
) = MovieDetails(
    imdbId = detail.imdbId,
    title = detail.title,
    originalTitle = detail.originalTitle,
    overview = detail.overview,
    tagline = detail.tagline,
    releaseDate = detail.releaseDate,
    year = detail.year,
    runtime = detail.runtime,
    imdbRating = detail.imdbRating,
    imdbVotes = detail.imdbVotes,
    tmdbRating = detail.tmdbRating,
    posterPath = detail.posterPath,
    backdropPath = detail.backdropPath,
    genres = genres.map { Genre(it.id, it.name) },
    cast = actors.map { Actor(it.imdbId, it.name, it.profilePath) },
    isFavorite = isFavorite,
    isOnWatchlist = isOnWatchlist,
)

fun FavoriteEntity.toDomain() = Movie(
    imdbId = imdbId,
    title = title,
    year = year,
    imdbRating = imdbRating,
    posterPath = posterPath,
    genres = emptyList(),
)

fun WatchlistEntity.toDomain() = Movie(
    imdbId = imdbId,
    title = title,
    year = year,
    imdbRating = imdbRating,
    posterPath = posterPath,
    genres = emptyList(),
)

fun Movie.toFavoriteEntity(addedAt: Long) = FavoriteEntity(
    imdbId = imdbId,
    title = title,
    year = year,
    imdbRating = imdbRating,
    posterPath = posterPath,
    addedAt = addedAt,
)

fun Movie.toWatchlistEntity(addedAt: Long) = WatchlistEntity(
    imdbId = imdbId,
    title = title,
    year = year,
    imdbRating = imdbRating,
    posterPath = posterPath,
    addedAt = addedAt,
)

fun MovieListItemApiModel.toFavoriteEntity(addedAt: Long) = FavoriteEntity(
    imdbId = imdbId,
    title = title,
    year = year,
    imdbRating = imdbRating,
    posterPath = posterPath,
    addedAt = addedAt,
)

fun MovieListItemApiModel.toWatchlistEntity(addedAt: Long) = WatchlistEntity(
    imdbId = imdbId,
    title = title,
    year = year,
    imdbRating = imdbRating,
    posterPath = posterPath,
    addedAt = addedAt,
)
