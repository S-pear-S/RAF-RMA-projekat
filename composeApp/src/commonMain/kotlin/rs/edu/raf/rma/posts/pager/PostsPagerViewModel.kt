package rs.edu.raf.rma.posts.pager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import rs.edu.raf.rma.posts.domain.PostRepository
import rs.edu.raf.rma.posts.postIdOrThrow

class PostsPagerViewModel(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val argPostId: Int = savedStateHandle.postIdOrThrow

    private val _state = MutableStateFlow(PostsPagerContract.UiState(startId = argPostId))
    val state: StateFlow<PostsPagerContract.UiState> = _state.asStateFlow()

    init {
        observePosts()
    }

    private fun observePosts() {
        viewModelScope.launch {
            postRepository.observePosts(mediaType = "image")
                .catch { e -> setState { copy(error = e, isLoading = false) } }
                .collect { posts -> setState { copy(posts = posts, isLoading = false) } }
        }
    }

    private fun setState(reducer: PostsPagerContract.UiState.() -> PostsPagerContract.UiState) {
        _state.getAndUpdate(reducer)
    }
}
