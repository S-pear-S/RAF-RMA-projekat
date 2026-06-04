package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

suspend fun main() {
    val customDispatcher1 = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    val customDispatcher2 = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    val customDispatcher3 = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    coroutineScope {
        withContext(customDispatcher1) {
            val intFlow = flowOf(1, 2, 3, 4)
            intFlow // will be executed on Dispatchers.IO if context wasn't specified before
                .map {
                    println("${Thread.currentThread().name} mapping $it")
                    it + 100
                } // Will be executed in customDispatcher2
                .flowOn(customDispatcher2)
                .filter {
                    println("${Thread.currentThread().name} filtering $it")
                    it > 102
                } // Will be executed in customDispatcher3
                .flowOn(customDispatcher3)
                .onEach {
                    println("${Thread.currentThread().name} iterating $it")
                }
                // Will be executed in the customDispatcher1 (from withContext)
                .onCompletion {
                    println("${Thread.currentThread().name} completed.")
                }
                .launchIn(this)
        }
    }

    // Program se ne zavrsava zbog custom executora
    // u kome threadovi ostaju zivi iz nekog razloga.
    // Imajte to na umu ukoliko koristite custom dispatchere.
}