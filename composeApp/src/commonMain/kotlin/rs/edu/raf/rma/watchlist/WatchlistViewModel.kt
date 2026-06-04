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
        when (event) {
            WatchlistEvent.Refresh -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    try {
                        repository.syncWatchlist()
                    } catch (e: Exception) {
                        _effects.send(WatchlistEffect.ShowMessage("Greška pri sinhronizaciji: ${e.message}"))
                    } finally {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }

            is WatchlistEvent.Remove -> {
                val previousMovies = _state.value.movies
                _state.update { it.copy(movies = it.movies.filter { m -> m.imdbId != event.movieId }) }
                viewModelScope.launch {
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
}
