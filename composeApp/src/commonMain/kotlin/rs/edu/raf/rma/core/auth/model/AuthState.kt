package rs.edu.raf.rma.core.auth.model

sealed class AuthState {
    data object Unauthenticated : AuthState()
    data class Authenticated(val data: AuthData) : AuthState()
}

fun AuthData.asAuthenticationState(): AuthState {
    return when {
        accessToken.isNullOrBlank() -> AuthState.Unauthenticated
        else -> AuthState.Authenticated(data = this.copy())
    }
}