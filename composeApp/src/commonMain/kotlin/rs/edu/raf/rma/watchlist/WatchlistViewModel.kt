package rs.edu.raf.rma.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.rma.movies.domain.MoviesRepository

class WatchlistViewModel(
    private val repository: MoviesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(WatchlistState())
    val state = _state.asStateFlow()

    private val _effects = Channel<WatchlistEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        observeWatchlist()
        sync()
    }

    private fun observeWatchlist() {
        viewModelScope.launch {
            repository.observeWatchlist().collect { movies ->
                _state.update { it.copy(movies = movies, isLoading = false) }
            }
        }
    }

    private fun sync() {
        viewModelScope.launch {
            try {
                repository.syncWatchlist()
            } catch (_: Exception) {
            }
        }
    }

    fun onEvent(event: WatchlistEvent) {
        val previousMovies = _state.value.movies
        _state.update { reduce(it, event) }
        when (event) {
            WatchlistEvent.Refresh -> viewModelScope.launch {
                try {
                    repository.syncWatchlist()
                } catch (e: Exception) {
                    _effects.send(WatchlistEffect.ShowMessage("Greška pri sinhronizaciji: ${e.message}"))
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }
            }

            is WatchlistEvent.Remove -> viewModelScope.launch {
                try {
                    repository.removeFromWatchlist(event.movieId)
                } catch (e: Exception) {
                    _state.update { it.copy(movies = previousMovies) }
                    _effects.send(WatchlistEffect.ShowMessage("Uklanjanje nije uspelo."))
                }
            }
        }
    }
}

private fun reduce(state: WatchlistState, event: WatchlistEvent): WatchlistState = when (event) {
    WatchlistEvent.Refresh -> state.copy(isLoading = true)
    is WatchlistEvent.Remove -> state.copy(movies = state.movies.filter { it.imdbId != event.movieId })
}
