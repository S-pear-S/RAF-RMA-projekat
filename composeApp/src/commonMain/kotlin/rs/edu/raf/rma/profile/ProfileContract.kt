package rs.edu.raf.rma.profile

data class ProfileState(
    val fullName: String = "",
    val username: String = "",
    val favoritesCount: Int = 0,
    val watchlistCount: Int = 0,
    val bestScore: Double? = null,
    val quizCount: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null,
)

sealed interface ProfileEvent {
    data object Logout : ProfileEvent
    data object Refresh : ProfileEvent
}

sealed interface ProfileEffect {
    data object NavigateToAuth : ProfileEffect
}
