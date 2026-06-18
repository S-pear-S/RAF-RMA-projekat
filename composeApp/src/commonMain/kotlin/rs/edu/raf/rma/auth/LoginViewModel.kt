package rs.edu.raf.rma.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.rma.core.auth.AuthStore
import rs.edu.raf.rma.core.auth.model.AuthData
import rs.edu.raf.rma.networking.MoviesApi
import rs.edu.raf.rma.networking.model.AuthRequestBody
import rs.edu.raf.rma.networking.di.Qualifiers

class LoginViewModel(
    private val api: MoviesApi,
    private val authStore: AuthStore,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effects = Channel<AuthEffect>()
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: AuthEvent) {
        _state.update { reduce(it, event) }
        if (event is AuthEvent.Submit) login()
    }

    private fun login() {
        val current = _state.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _state.update { it.copy(error = "Popunite sva polja.") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val response = api.login(AuthRequestBody(current.username, current.password))
                authStore.setAuthData(AuthData(accessToken = response.accessToken))
                _effects.send(AuthEffect.NavigateToMain)
            } catch (e: ResponseException) {
                val msg = when (e.response.status.value) {
                    401 -> "Pogrešni kredencijali."
                    else -> "Mrežna greška: ${e.response.status.value}"
                }
                _state.update { it.copy(isLoading = false, error = msg) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Greška: ${e.message}") }
            }
        }
    }
}

private fun reduce(state: LoginState, event: AuthEvent): LoginState = when (event) {
    is AuthEvent.LoginChanged -> state.copy(username = event.username, error = null)
    is AuthEvent.PasswordChanged -> state.copy(password = event.password, error = null)
    is AuthEvent.FullNameChanged, AuthEvent.Submit -> state
}
