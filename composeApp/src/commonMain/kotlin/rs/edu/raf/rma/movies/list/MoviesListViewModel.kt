package rs.edu.raf.rma.movies.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.rma.movies.domain.MoviesRepository

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MoviesListViewModel(
    private val repository: MoviesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MoviesListState(isLoading = true))
    val state = _state.asStateFlow()

    val moviesFlow = _state
        .map { it.filter }
        .distinctUntilChanged()
        .flatMapLatest { filter -> repository.moviesPager(filter) }
        .cachedIn(viewModelScope)

    init {
        observeGenres()
        loadInitialMovies()
        observeSearchDebounced()
    }

    private fun observeGenres() {
        repository.observeGenres()
            .onEach { genres -> _state.update { it.copy(genres = genres) } }
            .launchIn(viewModelScope)
    }

    private fun observeSearchDebounced() {
        _state
            .map { it.filter.query }
            .distinctUntilChanged()
            .debounce(400)
            .onEach { refreshFromNetwork() }
            .launchIn(viewModelScope)
    }

    private fun loadInitialMovies() {
        viewModelScope.launch { refreshFromNetwork() }
    }

    private suspend fun refreshFromNetwork() {
        try {
            _state.update { it.copy(currentPage = 1, hasMorePages = true) }
            val hasMore = repository.refreshMovies(1, _state.value.filter)
            _state.update { it.copy(hasMorePages = hasMore, isLoading = false) }
        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    fun onEvent(event: MoviesListEvent) {
        _state.update { reduce(it, event) }

        when (event) {
            MoviesListEvent.Refresh -> viewModelScope.launch {
                _state.update { it.copy(isRefreshing = true) }
                try {
                    refreshFromNetwork()
                } finally {
                    _state.update { it.copy(isRefreshing = false) }
                }
            }

            MoviesListEvent.LoadNextPage -> {
                val current = _state.value
                if (current.isLoadingMore || !current.hasMorePages) return
                viewModelScope.launch {
                    _state.update { it.copy(isLoadingMore = true) }
                    val nextPage = current.currentPage + 1
                    try {
                        val hasMore = repository.refreshMovies(nextPage, current.filter)
                        _state.update { it.copy(currentPage = nextPage, isLoadingMore = false, hasMorePages = hasMore) }
                    } catch (e: Exception) {
                        _state.update { it.copy(isLoadingMore = false, error = e.message) }
                    }
                }
            }

            is MoviesListEvent.FilterChanged -> {
                viewModelScope.launch { refreshFromNetwork() }
            }

            is MoviesListEvent.SearchChanged,
            MoviesListEvent.ToggleFilterSheet,
            MoviesListEvent.DismissError -> Unit
        }
    }
}

private fun reduce(state: MoviesListState, event: MoviesListEvent): MoviesListState = when (event) {
    is MoviesListEvent.SearchChanged ->
        state.copy(filter = state.filter.copy(query = event.query.ifBlank { null }))

    is MoviesListEvent.FilterChanged ->
        state.copy(filter = event.filter, showFilterSheet = false)

    MoviesListEvent.ToggleFilterSheet -> state.copy(showFilterSheet = !state.showFilterSheet)
    MoviesListEvent.DismissError -> state.copy(error = null)
    MoviesListEvent.Refresh, MoviesListEvent.LoadNextPage -> state
}
