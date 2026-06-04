package rs.edu.raf.rma.favorites

import rs.edu.raf.rma.movies.domain.Movie

data class FavoritesState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

sealed interface FavoritesEvent {
    data object Refresh : FavoritesEvent
    data class Remove(val movieId: String) : FavoritesEvent
    data object DismissError : FavoritesEvent
}

sealed interface FavoritesEffect {
    data class ShowMessage(val message: String) : FavoritesEffect
}
