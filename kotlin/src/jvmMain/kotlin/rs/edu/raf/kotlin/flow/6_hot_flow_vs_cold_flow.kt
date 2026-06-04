package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlin.time.Duration.Companion.seconds

suspend fun main() = coroutineScope {
    val coldFlow = flow {
        listOf(1, 2, 3, 4, 5).forEach {
            emit(it)
            delay(1.seconds)
        }
    }

//    coldFlow.onEach { println("Cold1 flow = $it") }.launchIn(this)
//    coldFlow.onEach { println("Cold2 flow = $it") }.launchIn(this)
//    coldFlow.onEach { println("Cold3 flow = $it") }.launchIn(this)

    val hotFlow = coldFlow.shareIn(
        scope = this,
        started = SharingStarted.Eagerly,
        replay = 1,
    )

    hotFlow.onEach { println("Hot1 flow = $it") }.launchIn(this)
    delay(10.seconds)
    hotFlow.onEach { println("Hot2 flow = $it") }.launchIn(this)
    hotFlow.onEach { println("Hot3 flow = $it") }.launchIn(this)

    Unit
}