package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

suspend fun main() = coroutineScope {
    val collector1 = launch {
        createRandomNumbersFlow().collect {
            println(it)
        }

        // nikada ne pokrece ovaj deo
        println("never executed")
    }

    val collector2 = launch {
        createRandomNumbersFlow().collect {
            println("$it was received.")
        }
    }

    delay(5.seconds)
    collector2.cancel()
    delay(10.seconds)
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
