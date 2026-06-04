package rs.edu.raf.rma.posts.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import rs.edu.raf.rma.core.auth.AuthStore
import rs.edu.raf.rma.core.auth.model.AuthState
import kotlin.time.Duration.Companion.seconds

class SplashViewModel(
    private val authStore: AuthStore,
) : ViewModel() {

    private val _bootState = MutableStateFlow<BootState>(BootState.Loading)
    val bootState: StateFlow<BootState> = _bootState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        checkAuthState()
        observeAuthState()
    }

    private fun observeAuthState() {
        authStore.authState
            .onEach { authState ->
                if (_bootState.value == BootState.Success) {
                    _isLoggedIn.value = authState is AuthState.Authenticated
                }
            }
            .launchIn(viewModelScope)
    }

    private fun checkAuthState() = viewModelScope.launch {
        try {
            val authState = authStore.awaitInitialAuthState()
            _isLoggedIn.value = authState is AuthState.Authenticated
            delay(1.5.seconds)
            _bootState.value = BootState.Success
        } catch (e: Exception) {
            _bootState.value = BootState.Failed(e)
        }
    }

    fun onAuthenticated() {
        _isLoggedIn.value = true
    }

    fun onLogout() {
        _isLoggedIn.value = false
    }

    fun retryBoot() {
        _bootState.value = BootState.Loading
        checkAuthState()
    }
}
