package rs.edu.raf.rma.passwords.list

import rs.edu.raf.rma.passwords.domain.Password

interface PasswordsListContract {

    data class UiState(
        val passwords: List<Password> = emptyList(),
        val isLoading: Boolean = true,
        val error: Throwable? = null,
    )



}