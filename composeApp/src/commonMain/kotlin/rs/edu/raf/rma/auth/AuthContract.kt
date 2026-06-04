package rs.edu.raf.rma.auth

sealed interface AuthEvent {
    data class LoginChanged(val username: String) : AuthEvent
    data class PasswordChanged(val password: String) : AuthEvent
    data class FullNameChanged(val fullName: String) : AuthEvent
    data object Submit : AuthEvent
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class RegisterState(
    val fullName: String = "",
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface AuthEffect {
    data object NavigateToMain : AuthEffect
}
