package rs.edu.raf.rma.movies.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.rma.movies.domain.MoviesRepository

class MovieDetailViewModel(
    private val movieId: String,
    private val repository: MoviesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state = _state.asStateFlow()

    private val _effects = Channel<MovieDetailEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        observeMovie()
        refresh()
    }

    private fun observeMovie() {
        viewModelScope.launch {
            repository.observeMovieDetails(movieId).collect { movie ->
                _state.update { it.copy(movie = movie, isLoading = false) }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                repository.refreshMovieDetails(movieId)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: MovieDetailEvent) {
        _state.update { reduce(it, event) }
        when (event) {
            MovieDetailEvent.Refresh -> refresh()
            MovieDetailEvent.ToggleFavorite -> toggleFavorite()
            MovieDetailEvent.ToggleWatchlist -> toggleWatchlist()
            MovieDetailEvent.DismissError -> Unit
        }
    }

    private fun toggleFavorite() {
        val movie = _state.value.movie ?: return
        viewModelScope.launch {
            _state.update { it.copy(isTogglingFavorite = true) }
            val wasAdded = !movie.isFavorite
            try {
                if (movie.isFavorite) {
                    repository.removeFavorite(movieId)
                } else {
                    repository.addFavorite(
                        rs.edu.raf.rma.movies.domain.Movie(
                            imdbId = movie.imdbId,
                            title = movie.title,
                            year = movie.year,
                            imdbRating = movie.imdbRating,
                            posterPath = movie.posterPath,
                            genres = movie.genres,
                        )
                    )
                }
            } catch (e: Exception) {
                val msg = if (wasAdded) "Dodavanje u favorite nije uspelo." else "Uklanjanje iz favorita nije uspelo."
                _effects.send(MovieDetailEffect.ShowMessage(msg))
            } finally {
                _state.update { it.copy(isTogglingFavorite = false) }
            }
        }
    }

    private fun toggleWatchlist() {
        val movie = _state.value.movie ?: return
        viewModelScope.launch {
            _state.update { it.copy(isTogglingWatchlist = true) }
            val wasAdded = !movie.isOnWatchlist
            try {
                if (movie.isOnWatchlist) {
                    repository.removeFromWatchlist(movieId)
                } else {
                    repository.addToWatchlist(
                        rs.edu.raf.rma.movies.domain.Movie(
                            imdbId = movie.imdbId,
                            title = movie.title,
                            year = movie.year,
                            imdbRating = movie.imdbRating,
                            posterPath = movie.posterPath,
                            genres = movie.genres,
                        )
                    )
                }
            } catch (e: Exception) {
                val msg = if (wasAdded) "Dodavanje na watchlist nije uspelo." else "Uklanjanje sa watchliste nije uspelo."
                _effects.send(MovieDetailEffect.ShowMessage(msg))
            } finally {
                _state.update { it.copy(isTogglingWatchlist = false) }
            }
        }
    }
}

private fun reduce(state: MovieDetailState, event: MovieDetailEvent): MovieDetailState = when (event) {
    MovieDetailEvent.Refresh -> state.copy(isLoading = true, error = null)
    MovieDetailEvent.DismissError -> state.copy(error = null)
    MovieDetailEvent.ToggleFavorite, MovieDetailEvent.ToggleWatchlist -> state
}
