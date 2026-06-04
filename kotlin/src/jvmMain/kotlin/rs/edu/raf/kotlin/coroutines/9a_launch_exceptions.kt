package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("blocking start")

    val job1 = launch {
        println("Job1 started.")
        throwSomething(500L)
        println("Job1 completed.")
    }

    try {
        launch {
            println("Job2 started.")
            throwSomething(2_000L)
            println("Job2 completed.")
        }
    } catch (error: IllegalStateException) {
        println("Why this doesnt work?")
        // Zato sto try-catch hvata poziv launch(),
        // a ne izvrsavanje bloka unutar njega
    }

    launch {
        try {
            println("Job3 started.")
            throwSomething(1_000L)
        } catch (error: IllegalStateException) {
            println("Job3 had an $error")
        }
        println("Job3 completed.")
    }

    job1.cancel()

    println("blocking end")
}

private suspend fun throwSomething(delay: Long) {
    delay(delay)
    throw IllegalStateException("Cant wait that long.")
}