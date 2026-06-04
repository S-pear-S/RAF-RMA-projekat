package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

suspend fun main() = coroutineScope {
    val myRandomNumberEmitter = MyRandomNumberEmitter()
    myRandomNumberEmitter.start()
    myRandomNumberEmitter.events.onEach { println("$it") }.launchIn(this)

    // Creating state flow
    val stateFlow = myRandomNumberEmitter.events.stateIn(this)
    launch {
        while (true) {
            println("State = ${stateFlow.value}")
            delay(3.seconds)
        }
    }


    println("Start main with state = ${stateFlow.value}")
}