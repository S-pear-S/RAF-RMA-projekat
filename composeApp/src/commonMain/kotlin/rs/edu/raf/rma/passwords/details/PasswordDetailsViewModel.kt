package rs.edu.raf.rma.passwords.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import rs.edu.raf.rma.passwords.details.PasswordDetailsContract.SideEffect
import rs.edu.raf.rma.passwords.domain.PasswordRepository
import rs.edu.raf.rma.passwords.passwordIdOrThrow
import kotlin.time.Duration.Companion.seconds

class PasswordDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val passwordRepository: PasswordRepository,
) : ViewModel() {

    private val argPasswordId = savedStateHandle.passwordIdOrThrow

    private val _state = MutableStateFlow(PasswordDetailsContract.UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: PasswordDetailsContract.UiState.() -> PasswordDetailsContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    private val events = MutableSharedFlow<PasswordDetailsContract.UiEvent>()
    fun setEvent(event: PasswordDetailsContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    private val _effects = MutableSharedFlow<PasswordDetailsContract.SideEffect>()
    val effects = _effects.asSharedFlow()
    private fun setEffect(effect: PasswordDetailsContract.SideEffect) {
        viewModelScope.launch { _effects.emit(effect) }
    }


    init {
        observeEvents()
        loadPassword(argPasswordId)



    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    private fun loadPassword(argPasswordId: Long) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            runCatching {
                delay(2.seconds)
                passwordRepository.getById(id = argPasswordId)
            }.fold(
                onSuccess = { password ->
                    setState { copy(data = password, error = null) }
                },
                onFailure = { error ->
                    setState { copy(data = null, error = error) }
                }
            )
            setState { copy(isLoading = false) }
        }
    }

    /**
     * Alternativni publishovanje eventova koje je podskup u odnosu
     * `setEvent`. `setEvent` vam omogucava vecu kontrolu, jer vi
     * kontroliste tok kroz `events` Flow, dok `alternativeSetEvent`
     * odmah obradjuje event u novu coroutine. `setEvent` vam daje
     * mogucnost serijske obrade eventova, dok `alternativeSetEvent`
     * radi sve paralelno.
     */
    fun alternativeSetEvent(event: PasswordDetailsContract.UiEvent) {
        viewModelScope.launch {
            when (event) {
                is PasswordDetailsContract.UiEvent.DeleteData -> {
                    deletePassword(argPasswordId)
                }
            }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is PasswordDetailsContract.UiEvent.DeleteData -> {
                        deletePassword(argPasswordId)
                    }
                }
            }
        }
    }

    private fun deletePassword(passwordId: Long) {
        viewModelScope.launch {

            coroutineScope {
                launch { passwordRepository.delete(passwordId) }
                launch { passwordRepository.delete2(passwordId) }
            }

            passwordRepository.delete3(passwordId)
            passwordRepository.delete4(passwordId)

            setEffect(SideEffect.DataDeleted)
        }
    }
}