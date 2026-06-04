package rs.edu.raf.rma.passwords.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.edu.raf.rma.networking.BeskarApi
import rs.edu.raf.rma.passwords.domain.Password
import rs.edu.raf.rma.passwords.domain.PasswordRepository
import kotlin.time.Duration.Companion.seconds

class PasswordsListViewModel(
    private val passwordRepository: PasswordRepository,
    private val beskarApi: BeskarApi,
) : ViewModel() {

    private val _state = MutableStateFlow(PasswordsListContract.UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: PasswordsListContract.UiState.() -> PasswordsListContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    init {
//        observePasswords()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val mockedPasswords = runCatching {
                    val response = beskarApi.getPosts()
                    response.items.mapIndexed { index, model ->
                        Password(
                            id = index.toLong(),
                            name = model.title,
                            url = model.imageUrl ?: "",
                            password = "",
                        )
                    }
                }
                setState {
                    copy(
                        isLoading = false,
                        passwords = mockedPasswords.getOrNull() ?: emptyList(),
                    )
                }
            }
        }
    }

    private fun observePasswords() {
        viewModelScope.launch {
            delay(2.seconds)
            passwordRepository
                .observePasswords()
                .collect { passwords ->
                    setState {
                        this.copy(
                            passwords = passwords,
                            isLoading = false,
                        )
                    }
                }
        }
    }

}