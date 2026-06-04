package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() = runBlocking {
    println("blocking start")

    launch {
        val jobs = mutableListOf<Job>()
        repeat(3) {
            val childJob = launch {

                println("child $it started on ${Thread.currentThread().name}")

                withContext(Dispatchers.IO) {
                    delay(500L)
                    println("child $it worked on ${Thread.currentThread().name}.")
                }

                println("child $it completed on ${Thread.currentThread().name}.")
            }

            jobs.add(childJob)
        }
        jobs.joinAll()
    }

    println("blocking end")
}
