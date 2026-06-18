package rs.edu.raf.rma.splash

sealed interface BootState {
    data object Loading : BootState
    data object Success : BootState
    data class Failed(val error: Throwable) : BootState
}
