package rs.edu.raf.kotlin.basics

fun main() {

    // Immutable (read-only) collections
    println("=== Read-only Collections ===")
    val names = listOf("Marko", "Ana", "Nikola")
    val uniqueIds = setOf(1, 2, 3, 2, 1) // duplicates removed
    val grades = mapOf("Marko" to 9, "Ana" to 10, "Nikola" to 8)

    println("List: $names")
    println("Set: $uniqueIds")
    println("Map: $grades")

    // names.add("Janko")  // ERROR: List is read-only

    // Mutable collections
    println()
    println("=== Mutable Collections ===")
    val mutableNames = mutableListOf("Marko", "Ana")
    mutableNames.add("Nikola")
    mutableNames += "Janko"  // operator overload for add
    mutableNames -= "Ana"    // operator overload for remove
    println("MutableList: $mutableNames")

    val mutableGrades = mutableMapOf("Marko" to 9)
    mutableGrades["Ana"] = 10
    mutableGrades += "Nikola" to 8
    println("MutableMap: $mutableGrades")

    // Accessing elements
    println()
    println("=== Accessing Elements ===")
    println("names[0] = ${names[0]}")
    println("names.first() = ${names.first()}")
    println("names.last() = ${names.last()}")
    println("names.getOrNull(99) = ${names.getOrNull(99)}")
    println("grades[\"Ana\"] = ${grades["Ana"]}")
    println("grades.getOrDefault(\"Unknown\", 0) = ${grades.getOrDefault("Unknown", 0)}")

    // Checking contents
    println()
    println("=== Checking Contents ===")
    println("\"Marko\" in names = ${"Marko" in names}")
    println("names.contains(\"Janko\") = ${names.contains("Janko")}")
    println("names.isEmpty() = ${names.isEmpty()}")
    println("names.size = ${names.size}")

    // Iterating
    println()
    println("=== Iteration ===")
    print("forEach: ")
    names.forEach { print("$it ") }
    println()

    print("forEachIndexed: ")
    names.forEachIndexed { i, name -> print("$i:$name ") }
    println()

    // Map iteration with destructuring
    println("Map entries:")
    for ((name, grade) in grades) {
        println("  $name -> $grade")
    }

    // Conversion between types
    println()
    println("=== Conversions ===")
    println("list -> set: ${listOf(1, 2, 2, 3, 3).toSet()}")
    println("set -> list: ${setOf(3, 1, 2).toList()}")
    println("map -> list of pairs: ${grades.toList()}")
}
