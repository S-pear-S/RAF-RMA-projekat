package rs.edu.raf.rma.movies.domain

data class Movie(
    val imdbId: String,
    val title: String,
    val year: Int,
    val imdbRating: Double?,
    val posterPath: String?,
    val genres: List<Genre>,
) {
    val posterUrl: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
}

data class MovieDetails(
    val imdbId: String,
    val title: String,
    val originalTitle: String?,
    val overview: String?,
    val tagline: String?,
    val releaseDate: String?,
    val year: Int,
    val runtime: Int?,
    val imdbRating: Double?,
    val imdbVotes: Int?,
    val tmdbRating: Double?,
    val posterPath: String?,
    val backdropPath: String?,
    val genres: List<Genre>,
    val cast: List<Actor>,
    val isFavorite: Boolean = false,
    val isOnWatchlist: Boolean = false,
) {
    val posterUrl: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    val backdropUrl: String? get() = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" }
    val runtimeFormatted: String? get() = runtime?.let { "${it / 60}h ${it % 60}m" }
}

data class Genre(
    val id: Int,
    val name: String,
)

data class Actor(
    val imdbId: String,
    val name: String,
    val profilePath: String?,
) {
    val profileUrl: String? get() = profilePath?.let { "https://image.tmdb.org/t/p/w185$it" }
}
