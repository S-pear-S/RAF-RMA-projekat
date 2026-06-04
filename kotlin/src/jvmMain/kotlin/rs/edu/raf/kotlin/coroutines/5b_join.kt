package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("blocking start")

    val job1 = launch {
        println("Job1 started.")
        println("Job1 completed.")
    }

    val job2 = launch {
        println("Job2 started.")
        delay(500L)
        println("Job2 completed.")
    }

    val job3 = launch {
        println("Job3 started.")
        delay(2_000L)
        println("Job3 completed.")
    }

    job2.join()
    println("join() completed")

    job1.cancel()

    val job4 = launch {
        println("Job4 started.")
        delay(1_500L)
        println("Job4 completed.")
    }

    println("blocking end")
}

