package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

suspend fun main() = coroutineScope {
    val myRandomNumberEmitter = MyRandomNumberEmitter(
        log = true,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
        replay = 1,
    )

    // Kontrolise emitter
    launch {
        while (true) {
            myRandomNumberEmitter.start()
            println("Start emitter.")
            delay(10.seconds)

            myRandomNumberEmitter.stop()
            println("Stop emitter.")
            delay(5.seconds)
        }
    }

    // Kontrolise kolektore
    launch {
        println("Starting collector 1")
        val collector1 = myRandomNumberEmitter.events
            .onEach {
                println("Collector1: $it")
                delay(5.seconds)
            }
            .launchIn(this)
        delay(5.seconds)

        println("Starting collector 2")
        val collector2 = myRandomNumberEmitter.events
            .onEach {
                println("Collector2: $it")
                delay(5.seconds)
            }
            .launchIn(this)
        delay(5.seconds)

        println("Stopping collectors.")
        collector1.cancel()
        collector2.cancel()
    }

    println("Start main.")
}
