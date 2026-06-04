package rs.edu.raf.rma.posts.list

import rs.edu.raf.rma.posts.domain.Post

enum class ViewMode { List, Gallery }

interface PostsListContract {
    data class UiState(
        val viewMode: ViewMode = ViewMode.List,
        val posts: List<Post> = emptyList(),
        val isRefreshing: Boolean = false,
        val error: Throwable? = null,
    )

    sealed class UiEvent {
        data object Refresh : UiEvent()
        data class SetViewMode(val mode: ViewMode) : UiEvent()
    }

    sealed class SideEffect
}
