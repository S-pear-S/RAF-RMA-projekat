package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

suspend fun main() = coroutineScope {
    createRandomNumbersFlowWithRandomCrash()
//        .filter { it % 2 == 0 }
        .catch {
            println("Caught after filter = $it")
            // Exception je terminalna operacija.
            // Flow uvek prestaje sa radom nakon greske.
        }
        .map {
            if (it % 2 == 0) {
                throw IllegalStateException()
            } else {
                it
            }
        }
        .catch {
            println("Caught after map = $it")
//            emit(10000000)
//            emitAll(
//                flowOf(1, 2, 3, 4, 5)
//            )
        }
        .collect {
            println(it)
        }
}

private fun createRandomNumbersFlowWithRandomCrash(): Flow<Int> {
    return flow {

        var count = 0
        while (true) {
            if (count == 21) throw IllegalStateException()

            val random = Random.nextInt(1, 1_000)
            emit(random)
            count++

            delay(0.1.seconds)
        }
    }
}
