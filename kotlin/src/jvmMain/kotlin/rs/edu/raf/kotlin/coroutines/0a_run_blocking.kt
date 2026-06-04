package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    println("main start")

    runBlocking {
        println("runBlocking start")

        launch { doSuspendingWork(num = 1, delay = 500L) }
        launch { doSuspendingWork(num = 2, delay = 250L) }
        launch { doSuspendingWork(num = 3, delay = 450L) }

        println("runBlocking end")
    }

    println("main end")
}

suspend fun doSuspendingWork(num: Int, delay: Long) {
    println("Start work on coroutine $num")
    delay(delay)
    println("Completed work on coroutine $num")
}