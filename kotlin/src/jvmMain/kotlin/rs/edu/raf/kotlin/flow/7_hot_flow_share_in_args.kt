package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

suspend fun main() = coroutineScope {
    val coldFlow = flowOf(1, 2, 3, 4, 5)
    val hotFlow = coldFlow.shareIn(
        scope = this,
        started = SharingStarted.Eagerly,
//        started = SharingStarted.Lazily,
        replay = 1,
//        replay = 3,
    )

    hotFlow.onEach { println("Hot1 flow = $it") }.launchIn(this)
    hotFlow.onEach { println("Hot2 flow = $it") }.launchIn(this)
    hotFlow.onEach { println("Hot3 flow = $it") }.launchIn(this)

    println("End")
}
