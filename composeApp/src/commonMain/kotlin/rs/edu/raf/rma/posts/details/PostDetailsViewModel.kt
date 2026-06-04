package rs.edu.raf.rma.posts.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import rs.edu.raf.rma.posts.domain.PostRepository
import rs.edu.raf.rma.posts.postIdOrThrow

class PostDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val argPostId: Int = savedStateHandle.postIdOrThrow

    private val _state = MutableStateFlow(PostDetailsContract.UiState())
    val state = _state.asStateFlow()

    private fun setState(reducer: PostDetailsContract.UiState.() -> PostDetailsContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    private val events = MutableSharedFlow<PostDetailsContract.UiEvent>()
    fun setEvent(event: PostDetailsContract.UiEvent) {
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
    fun alternativeSetEvent(event: PostDetailsContract.UiEvent) {
        viewModelScope.launch {
            when (event) {
                is PostDetailsContract.UiEvent.Refresh -> refresh()
            }
        }
    }

    init {
        observeEvents()
        observePost()
        refresh()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is PostDetailsContract.UiEvent.Refresh -> refresh()
                }
            }
        }
    }

    private fun observePost() {
        viewModelScope.launch {
            postRepository.observePost(argPostId).collect { details ->
                setState { copy(details = details) }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            setState { copy(isRefreshing = true, error = null) }
            runCatching { postRepository.refreshPost(argPostId) }
                .onFailure { setState { copy(error = it) } }
            setState { copy(isRefreshing = false) }
        }
    }
}
