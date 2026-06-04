package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate

fun main() {
    val myLoader = MyLoader()
    println(myLoader.state.value)

    myLoader.startLoading()
    println(myLoader.state.value)

    myLoader.finishSuccessfully()
    println(myLoader.state.value)

    myLoader.reset()
    println(myLoader.state.value)

    myLoader.startLoading()
    println(myLoader.state.value)

    myLoader.finishWithError()
    println(myLoader.state.value)

    println("End")
}

data class MyState(
    val loading: Boolean = false,
    val result: List<Int> = emptyList(),
    val error: Throwable? = null,
)

class MyLoader {

    private val _state = MutableStateFlow(value = MyState())
    val state = _state.asStateFlow()
    private fun setState(reducer: MyState.() -> MyState) = _state.getAndUpdate(reducer)

    fun reset() {
        println()
        println("Resetting state.")
        setState { MyState() }
    }

    fun startLoading() {
        println()
        println("Start loading.")
        setState {
            this.copy(
                loading = true,
            )
        }
    }

    fun finishSuccessfully() {
        println()
        println("finishSuccessfully.")
        setState {
            copy(
                loading = false,
                result = listOf(1, 2, 3),
            )
        }
    }

    fun finishWithError() {
        println()
        println("finishWithError.")
        setState {
            copy(
                loading = false,
                error = RuntimeException(),
            )
        }
    }
}
