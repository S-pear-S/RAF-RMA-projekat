package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
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
        delay(5_000L)
        println("Job3 completed.")
    }

//    job1.join()
//    job2.join()
//    job3.join()

//    joinAll(job1, job2, job3)
    listOf(job1, job2, job3).joinAll()

    println("=== First phase completed. === ")

    joinAll(
        doJob(number = 4),
        doJob(number = 5),
    )

    println("blocking end")
}

private fun CoroutineScope.doJob(number: Int) = launch {
    println("Job$number started.")
    delay(500L)
    println("Job$number completed.")
}

private suspend fun doJob2(number: Int) = coroutineScope {
    launch {
        println("Job$number started.")
        delay(500L)
        println("Job$number completed.")
    }
}
