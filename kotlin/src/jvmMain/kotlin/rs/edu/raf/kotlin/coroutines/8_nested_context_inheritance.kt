package rs.edu.raf.kotlin.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val parent = launch {
        val parentName = coroutineContext[CoroutineName]?.name
        println("Parent '$parentName' doing some work on '${Thread.currentThread().name}'.")
        delay(100L)

        val child = launch(CoroutineName("Kotlin")) {
            val childName = coroutineContext[CoroutineName]?.name
            println("Child '$childName' doing some work on '${Thread.currentThread().name}'.")
            delay(200L)

            val grandchild = launch(Dispatchers.IO) {
                val grandchildName = coroutineContext[CoroutineName]?.name
                println("Grandchild '$grandchildName' doing some work on '${Thread.currentThread().name}'.")
                delay(50L)

                val greatGrandchild = launch {
                    val greatGrandchildName = coroutineContext[CoroutineName]?.name
                    println("Great-Grandchild '$greatGrandchildName' doing some work on '${Thread.currentThread().name}'.")
                    delay(10L)
                    println("Coroutine (4) Great-Grandchild ended (nested level-3).")
                }
//                greatGrandchild.join()

                println("Coroutine (3) Grandchild ended (nested level-2).")
            }
//            grandchild.join()

            println("Coroutine (2) Child ended (nested level-1).")
        }
//        child.join()

        println("Coroutine (1) Parent ended (root level).")
    }
}
