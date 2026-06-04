package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

suspend fun main() {
    val namesFlow = flowOf("Marko", "Janko", "Nikola", "Jovan", "Aleksandar", "Andrej")

    namesFlow
        .onStart { println("Flow started") }
        .onEach { println("On each (1) = $it") }
        .filterNot { it.length > 5 }
        .drop(count = 1)
        .onEach { println("On each (2) = $it") }
//        .take(count = 1) // Otkazuje Flow nakon prvog elementa, zbog toga onEach stampa samo 1
        .onCompletion { println("Completed.") }
        .toList()
        .let {
            println("Done = $it")
        }

}