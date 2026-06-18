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
import rs.edu.raf.rma.networking.model.RegisterRequestBody

class RegisterViewModel(
    private val api: MoviesApi,
    private val authStore: AuthStore,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _effects = Channel<AuthEffect>()
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: AuthEvent) {
        _state.update { reduce(it, event) }
        if (event is AuthEvent.Submit) register()
    }

    private fun register() {
        val current = _state.value
        val error = validate(current)
        if (error != null) {
            _state.update { it.copy(error = error) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val response = api.register(
                    RegisterRequestBody(
                        fullName = current.fullName.trim(),
                        username = current.username.trim(),
                        password = current.password,
                    )
                )
                authStore.setAuthData(AuthData(accessToken = response.accessToken))
                _effects.send(AuthEffect.NavigateToMain)
            } catch (e: ResponseException) {
                val msg = when (e.response.status.value) {
                    409 -> "Korisničko ime je zauzeto."
                    400 -> "Nevalidni podaci. Proverite polja."
                    else -> "Mrežna greška: ${e.response.status.value}"
                }
                _state.update { it.copy(isLoading = false, error = msg) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Greška: ${e.message}") }
            }
        }
    }

    private fun validate(state: RegisterState): String? {
        if (state.fullName.isBlank()) return "Ime je obavezno."
        if (state.username.length < 3) return "Username mora imati najmanje 3 karaktera."
        if (!state.username.matches(Regex("[a-zA-Z0-9_]+"))) return "Username može sadržati samo slova, cifre i _."
        if (state.password.length < 8) return "Lozinka mora imati najmanje 8 karaktera."
        return null
    }
}

private fun reduce(state: RegisterState, event: AuthEvent): RegisterState = when (event) {
    is AuthEvent.FullNameChanged -> state.copy(fullName = event.fullName, error = null)
    is AuthEvent.LoginChanged -> state.copy(username = event.username, error = null)
    is AuthEvent.PasswordChanged -> state.copy(password = event.password, error = null)
    AuthEvent.Submit -> state
}
