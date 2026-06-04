package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

suspend fun main() = coroutineScope {
    val myRandomNumberEmitter = MyRandomNumberEmitter(
        replay = 2
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
            .onEach { println("Collector1: $it") }
            .launchIn(this)
        delay(5.seconds)

        println("Starting collector 2")
        val collector2 = myRandomNumberEmitter.events
            .onEach { println("Collector2: $it") }
            .launchIn(this)
        delay(5.seconds)

        println("Stopping collectors.")
        collector1.cancel()
        collector2.cancel()
    }

    println("Start main.")
}


class MyRandomNumberEmitter(
    private val log: Boolean = false,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
    replay: Int = 0,
) {

    private val customScope = CoroutineScope(Dispatchers.IO)

    private val _events = MutableSharedFlow<Int>(
        onBufferOverflow = onBufferOverflow,
        replay = replay,
    )
    val events = _events.asSharedFlow()

    private var job: Job? = null

    fun start() {
        job?.cancel(CancellationException("Restarting emitter."))
        job = customScope.launch {
            generateAndEmitRandomNumbers()
        }
    }

    fun stop() {
        job?.cancel(CancellationException("Stopping emitter."))
    }

    private suspend fun generateAndEmitRandomNumbers() {
        while (true) {
            val randomNumber = Random.nextInt(100, 10_000)
            if (log) println("Emitting $randomNumber")
            _events.emit(randomNumber)
            delay(1.seconds)
        }
    }
}
