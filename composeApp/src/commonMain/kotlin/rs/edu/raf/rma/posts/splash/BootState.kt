package rs.edu.raf.rma.posts.splash

/**
 * Boot state for the splash screen.
 */
sealed interface BootState {
    /**
     * Initial loading state - checking authentication and initializing app.
     */
    data object Loading : BootState

    /**
     * App successfully initialized and ready to navigate.
     */
    data object Success : BootState

    /**
     * App initialization failed.
     *
     * @param error The error that occurred during initialization.
     */
    data class Failed(val error: Throwable) : BootState
}
