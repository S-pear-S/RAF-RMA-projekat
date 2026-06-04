package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val scope = CoroutineScope(Dispatchers.IO)

fun main() {
    println("main start")

    scope.launch {
        println("main coroutine start")

        launch { doSuspendingWork2(num = 1, delay = 500L) }
        launch { doSuspendingWork2(num = 2, delay = 900L) }
        launch { doSuspendingWork2(num = 3, delay = 0L) }

        println("main coroutine end")
    }

    println("main end")
    Thread.sleep(600)
}

suspend fun doSuspendingWork2(num: Int, delay: Long) {
    println("Start work on coroutine $num")
    delay(delay)
    println("Completed work on coroutine $num")
}