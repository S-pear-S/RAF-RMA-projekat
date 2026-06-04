package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("blocking start")

    val job1 = launch(start = CoroutineStart.ATOMIC) {
        println("Job1 started.")
        delay(200L)
        println("Job1 completed.")
    }

    val job2 = launch(start = CoroutineStart.LAZY) {
        println("Job2 started.")
        delay(500L)
        println("Job2 completed.")
    }

    val job3 = launch {
        println("Job3 started.")
        delay(2_000L)
        println("Job3 completed.")
    }

    job2.start()
    job1.cancel()

    println("blocking end")
}

