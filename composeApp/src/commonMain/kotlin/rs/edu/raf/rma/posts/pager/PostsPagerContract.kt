package rs.edu.raf.rma.posts.pager

import rs.edu.raf.rma.posts.domain.Post

interface PostsPagerContract {
    data class UiState(
        val posts: List<Post> = emptyList(),
        val startId: Int = -1,
        val isLoading: Boolean = true,
        val error: Throwable? = null,
    )

    sealed class UiEvent

    sealed class SideEffect
}
