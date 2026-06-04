package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("Starting CoroutineScope example:")

//    try {
    runCatching {
        coroutineScope {
            launch {
                delay(1_000)
                println("Child 1 finished")
            }
            launch {
                delay(500)
                println("Child 2 finished")
            }
            launch {
                delay(200)
                println("Child 3 will throw an exception")
                throw Exception("Error in Child 3")
            }
            launch {
                delay(50)
                println("Child 4 finished")
            }
        }
    }.getOrElse { error ->
        println("Caught an exception: ${error.message}")
    }
//    } catch (e: Exception) {
//        println("Caught an exception: ${e.message}")
//    }

    println("\nCompleted.")
}
