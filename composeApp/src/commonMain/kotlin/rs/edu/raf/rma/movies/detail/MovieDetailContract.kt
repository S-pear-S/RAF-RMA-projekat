package rs.edu.raf.rma.movies.detail

import rs.edu.raf.rma.movies.domain.MovieDetails

data class MovieDetailState(
    val movie: MovieDetails? = null,
    val isLoading: Boolean = true,
    val isTogglingFavorite: Boolean = false,
    val isTogglingWatchlist: Boolean = false,
    val error: String? = null,
)

sealed interface MovieDetailEvent {
    data object Refresh : MovieDetailEvent
    data object ToggleFavorite : MovieDetailEvent
    data object ToggleWatchlist : MovieDetailEvent
    data object DismissError : MovieDetailEvent
}

sealed interface MovieDetailEffect {
    data class ShowMessage(val message: String) : MovieDetailEffect
}
