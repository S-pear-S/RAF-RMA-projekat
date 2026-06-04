package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread
import kotlin.time.measureTime

fun main() {
//    launchCoroutines(times = 10_000_000)
    launchThreads(times = 10_000)
}

fun launchThreads(times: Int) {
    val atomic = AtomicLong(0)
    val barrier = CyclicBarrier(times + 1)
    val start = System.nanoTime()
    repeat(times) {
        thread {
            print(".")
            atomic.set(System.nanoTime())
            barrier.await()
        }
    }
    barrier.await()
    val actualTime = atomic.get() - start
    println()
    println("Threads took: ${actualTime.div(1_000_000F)}ms")
}

fun launchCoroutines(times: Int) {
    val time = measureTime {
        runBlocking {
            repeat(times) {
                launch {
                    print(".")
                }
            }
        }
    }
    println()
    println("Coroutines took: $time")
}
