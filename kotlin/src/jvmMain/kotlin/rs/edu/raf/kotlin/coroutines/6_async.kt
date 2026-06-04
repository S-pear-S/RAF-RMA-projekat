package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main() = runBlocking {
    val jobWithResult = async(start = CoroutineStart.LAZY) {
        myAsyncWork()
    }

    println("is isActive = ${jobWithResult.isActive}")
    println("is async work active: ${if (jobWithResult.isActive) "active" else "not active"}.")
    val isStarted = jobWithResult.start()
    println("is async work started = $isStarted")
    println("is async work active: ${if (jobWithResult.isActive) "active" else "not active"}.")

    val numberGuess = jobWithResult.await()
    println("In main we got: $numberGuess")

    val deferred1 = async {
        myAsyncWork()
    }

    val deferred2 = async {
        myAsyncWork()
    }

    val deferred3 = async {
        myAsyncWork()
    }

    deferred2.cancel()
}


suspend fun myAsyncWork(): Long {
    val random = Random.nextLong(10, 1_000)
    delay(random)
    println("We mined number: $random")
    return random
}