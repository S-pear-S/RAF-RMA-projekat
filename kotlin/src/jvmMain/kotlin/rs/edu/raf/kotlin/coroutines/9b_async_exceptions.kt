package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main() = runBlocking {
    println("blocking start")

    // CoroutineScope exceptions behaviour

//    coroutineScope {
//        val job = launch {
//            println("Job started.")
//            throwSomething(500L)
//            println("Job completed.")
//        }
//        // Program puca
//    }

//    coroutineScope {
//        val deferred = async {
//            println("Async started.")
//            throwSomething(500L)
//            println("Async completed.")
//        }
//        // Program puca
//    }


    // SupervisorScope exceptions behaviour
//
//    supervisorScope {
//        val job = launch {
//            println("Job started.")
//            throwSomething(500L)
//            println("Job completed.")
//        }
//        // coroutina puca, ali ne i program (imamo blocking end u logu)
////        job.join()
//    }
//
//    supervisorScope {
//        val deferred = async {
//            println("Async started.")
//            throwSomething(500L)
//            println("Async completed.")
//        }
//
//        // Sve uredno
//    }

    supervisorScope {
        val deferred = async {
            println("Async started.")
            throwSomething(500L)
            println("Async completed.")
        }

        // Program puca zbog await()-a
        deferred.await()
    }

    println("blocking end")
}

private suspend fun throwSomething(delay: Long) {
    delay(delay)
    throw IllegalStateException("Cant wait that long.")
}