package rs.edu.raf.rma.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.rma.core.auth.AuthStore
import rs.edu.raf.rma.movies.domain.MoviesRepository
import rs.edu.raf.rma.networking.MoviesApi

class ProfileViewModel(
    private val authApi: MoviesApi,
    private val authStore: AuthStore,
    private val repository: MoviesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _effects = Channel<ProfileEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        observeLocalData()
        loadProfile()
    }

    private fun observeLocalData() {
        viewModelScope.launch {
            combine(
                repository.observeFavoritesCount(),
                repository.observeWatchlistCount(),
                repository.observeQuizCount(),
            ) { favs, wl, count ->
                _state.update { it.copy(favoritesCount = favs, watchlistCount = wl, quizCount = count) }
            }.collect {}
        }
        viewModelScope.launch {
            repository.observeBestQuizScore().collect { best ->
                _state.update { it.copy(bestScore = best) }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val user = authApi.getProfile()
                _state.update { it.copy(fullName = user.fullName, username = user.username, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        _state.update { reduce(it, event) }
        when (event) {
            ProfileEvent.Logout -> viewModelScope.launch {
                repository.clearUserData()
                authStore.clearAuthData()
                _effects.send(ProfileEffect.NavigateToAuth)
            }
            ProfileEvent.Refresh -> loadProfile()
        }
    }
}

private fun reduce(state: ProfileState, event: ProfileEvent): ProfileState = when (event) {
    ProfileEvent.Refresh -> state.copy(isLoading = true, error = null)
    ProfileEvent.Logout -> state
}
