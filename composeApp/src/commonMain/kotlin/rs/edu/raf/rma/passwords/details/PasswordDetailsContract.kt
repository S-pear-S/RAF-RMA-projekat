package rs.edu.raf.rma.passwords.details

import rs.edu.raf.rma.passwords.domain.Password

interface PasswordDetailsContract {
    data class UiState(
        val data: Password? = null,
        val isLoading: Boolean = false,
        val error: Throwable? = null,
    )

    sealed class UiEvent {
        data object DeleteData : UiEvent()
    }

    sealed class SideEffect {
        data object DataDeleted : SideEffect()
    }

}