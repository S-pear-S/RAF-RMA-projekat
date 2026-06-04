package rs.edu.raf.rma.posts.details

import rs.edu.raf.rma.posts.domain.PostDetails

interface PostDetailsContract {
    data class UiState(
        val details: PostDetails? = null,
        val isRefreshing: Boolean = false,
        val error: Throwable? = null,
    )

    sealed class UiEvent {
        data object Refresh : UiEvent()
    }

    sealed class SideEffect
}
