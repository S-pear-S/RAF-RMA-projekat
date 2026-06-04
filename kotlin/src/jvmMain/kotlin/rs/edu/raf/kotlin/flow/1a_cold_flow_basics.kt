package rs.edu.raf.kotlin.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList

suspend fun main() {
    val namesFlow = listOf("Marko", "Janko", "Nikola", "Jovan", "Aleksandar", "Andrej").asFlow()
//    val namesFlow = flowOf("Marko", "Janko", "Nikola", "Jovan", "Aleksandar", "Andrej")

    val firstName = namesFlow.first()
    println("First Name = $firstName")

    val lastName = namesFlow.last()
    println("Last Name = $lastName")

    val count = namesFlow.count()
    println("There are $count names.")

    val countJNames = namesFlow.count { it.startsWith("J") }
    println("There are $countJNames names starting on J.")

    val listResult = namesFlow.toList()
    println("All names = $listResult")
}