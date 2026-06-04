package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlin.time.Duration.Companion.seconds

suspend fun main() {
    val names = listOf("Marko", "Janko", "Nikola", "Jovan", "Aleksandar", "Andrej")
    val namesFlow = names.asFlow()

    namesFlow.collect {
        delay(2.seconds)
        println(it)
    }

    println("Ovo se nece pozvati dok collect iznad ne zavrsi.")

    namesFlow.collect {
        println("$it has ${it.length} characters.")
    }
}
