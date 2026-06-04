package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main() {
    runBlocking {
        launch {
            val jobs = mutableListOf<Job>()
            repeat(3) {
                val childJob = launch {
                    doWorkAndPrintThreadName(
                        funName = it.toString(),
                        delay = Random.nextLong(100, 1000),
                    )
                }
                jobs.add(childJob)
            }
        }
    }
}


suspend fun doWorkAndPrintThreadName(funName: String, delay: Long) {
    println("Job $funName started on thread = ${Thread.currentThread().name}.")
    delay(delay)
    println("Job $funName completed on thread = ${Thread.currentThread().name}.")
}

