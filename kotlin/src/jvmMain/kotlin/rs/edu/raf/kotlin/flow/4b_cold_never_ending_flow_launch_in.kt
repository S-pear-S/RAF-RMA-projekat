package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

suspend fun main() = coroutineScope {
    val collector1 = createRandomNumbersFlow()
        .onStart { println("started") }
        .onEach { println(it) }
        .onCompletion { println("ended") }
        .launchIn(this)

    val collector2 = createRandomNumbersFlow()
        .onEach { println("We got number: $it") }
        .launchIn(this)

    delay(5.seconds)
    println("Cancelling collector 2.")
    collector2.cancel()

    delay(5.seconds)
    collector1.cancel()
}

private fun createRandomNumbersFlow(): Flow<Long> {
    return flow {
        while (true) {
            val random = Random.nextLong(1, 1_000)
            emit(random)
            delay(1.seconds)
        }
    }
}
