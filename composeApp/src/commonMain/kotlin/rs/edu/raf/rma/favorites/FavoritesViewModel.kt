package rs.edu.raf.rma.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.rma.movies.domain.MoviesRepository

class FavoritesViewModel(
    private val repository: MoviesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()

    private val _effects = Channel<FavoritesEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        observeFavorites()
        sync()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.observeFavorites().collect { movies ->
                _state.update { it.copy(movies = movies, isLoading = false) }
            }
        }
    }

    private fun sync() {
        viewModelScope.launch {
            try {
                repository.syncFavorites()
            } catch (_: Exception) {
            }
        }
    }

    fun onEvent(event: FavoritesEvent) {
        when (event) {
            FavoritesEvent.Refresh -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    try {
                        repository.syncFavorites()
                    } catch (e: Exception) {
                        _effects.send(FavoritesEffect.ShowMessage("Greška pri sinhronizaciji: ${e.message}"))
                    } finally {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }

            is FavoritesEvent.Remove -> {
                val previousMovies = _state.value.movies
                _state.update { it.copy(movies = it.movies.filter { m -> m.imdbId != event.movieId }) }
                viewModelScope.launch {
                    try {
                        repository.removeFavorite(event.movieId)
                    } catch (e: Exception) {
                        _state.update { it.copy(movies = previousMovies) }
                        _effects.send(FavoritesEffect.ShowMessage("Uklanjanje nije uspelo."))
                    }
                }
            }

            FavoritesEvent.DismissError -> _state.update { it.copy(error = null) }
        }
    }
}
