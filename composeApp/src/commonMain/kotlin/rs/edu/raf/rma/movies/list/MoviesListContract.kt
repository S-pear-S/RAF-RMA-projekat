package rs.edu.raf.rma.movies.list

import rs.edu.raf.rma.movies.domain.Genre
import rs.edu.raf.rma.movies.domain.Movie
import rs.edu.raf.rma.movies.domain.MovieFilter

data class MoviesListState(
    val movies: List<Movie> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val filter: MovieFilter = MovieFilter(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
    val showFilterSheet: Boolean = false,
)

sealed interface MoviesListEvent {
    data object Refresh : MoviesListEvent
    data object LoadNextPage : MoviesListEvent
    data class SearchChanged(val query: String) : MoviesListEvent
    data class FilterChanged(val filter: MovieFilter) : MoviesListEvent
    data object ToggleFilterSheet : MoviesListEvent
    data object DismissError : MoviesListEvent
}
