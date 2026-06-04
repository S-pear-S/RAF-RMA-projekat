package rs.edu.raf.rma.posts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import rs.edu.raf.rma.posts.domain.PostRepository

class PostsListViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PostsListContract.UiState())
    val state = _state.asStateFlow()

    private fun setState(reducer: PostsListContract.UiState.() -> PostsListContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    private val events = MutableSharedFlow<PostsListContract.UiEvent>()
    fun setEvent(event: PostsListContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    /**
     * Alternativni publishovanje eventova koje je podskup u odnosu
     * `setEvent`. `setEvent` vam omogucava vecu kontrolu, jer vi
     * kontroliste tok kroz `events` Flow, dok `alternativeSetEvent`
     * odmah obradjuje event u novu coroutine. `setEvent` vam daje
     * mogucnost serijske obrade eventova, dok `alternativeSetEvent`
     * radi sve paralelno.
     */
    fun alternativeSetEvent(event: PostsListContract.UiEvent) {
        viewModelScope.launch {
            when (event) {
                is PostsListContract.UiEvent.Refresh -> refresh()
                is PostsListContract.UiEvent.SetViewMode ->
                    setState { copy(viewMode = event.mode) }
            }
        }
    }

    init {
        observeEvents()
        observePosts()
        refresh()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is PostsListContract.UiEvent.Refresh -> refresh()
                    is PostsListContract.UiEvent.SetViewMode ->
                        setState { copy(viewMode = event.mode) }
                }
            }
        }
    }

    private fun observePosts() {
        viewModelScope.launch {
            postRepository.observePosts(mediaType = "image").collect { posts ->
                setState { copy(posts = posts) }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            setState { copy(isRefreshing = true, error = null) }
            runCatching { postRepository.refreshPosts() }
                .onFailure { setState { copy(error = it) } }
            setState { copy(isRefreshing = false) }
        }
    }
}
