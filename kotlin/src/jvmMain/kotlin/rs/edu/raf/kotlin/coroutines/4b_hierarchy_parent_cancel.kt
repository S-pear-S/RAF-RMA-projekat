package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

fun main() = runBlocking {
    val parent = launch {
        launch {
            delay(100.milliseconds)
            println("Child1 completed.")
        }

        launch {
            delay(2_000.milliseconds)
            println("Child2 completed.")
        }

        launch {
            delay(500.milliseconds)
            println("Child3 completed.")
        }
    }
    delay(200.milliseconds)
    parent.cancel()
}
