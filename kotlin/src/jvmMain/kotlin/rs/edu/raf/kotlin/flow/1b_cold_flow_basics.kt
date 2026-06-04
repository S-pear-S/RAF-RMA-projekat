package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

suspend fun main() {
    val namesFlow = flowOf("Marko", "Janko", "Nikola", "Jovan", "Aleksandar", "Andrej")

    val lengthFlow = namesFlow.map { name -> name.length }
    val allLengths = lengthFlow.toList()
    println(allLengths)

    val longNamesFlow = lengthFlow.filter { length -> length > 5 }
    val longNamesList = longNamesFlow.toList()
    println(longNamesList)


    val longNames2 = namesFlow
        .filter { it.length > 5 }
        .toList()

    println(longNames2)

    namesFlow
        .filterNot { it.length > 5 }
        .drop(count = 1)
//        .take(count = 1)
        .toList()
        .let {
            println(it)
        }

}