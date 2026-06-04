package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main() = runBlocking {

    val errorHandler = CoroutineExceptionHandler { context, exception ->
        println("Caught exception $exception")
    }

    // Nacin 1 -> neispravno
//    val job2 = launch(errorHandler) {
//        println("Coroutine start")
//        throw RuntimeException("Something unexpected happened.")
//    }
//    job2.join()


    // Nacin 2 - neispravno
    val supervisorJob = SupervisorJob()
    val job3 = launch(supervisorJob + errorHandler) {
        println("Coroutine start")
        throw RuntimeException("Something unexpected happened.")
    }
    job3.join()


    // Nacin 3 - ispravno
    supervisorScope {
        launch(errorHandler) {
            println("Coroutine start")
            throw RuntimeException("Something unexpected happened.")
        }
    }

    println("Done")
}
