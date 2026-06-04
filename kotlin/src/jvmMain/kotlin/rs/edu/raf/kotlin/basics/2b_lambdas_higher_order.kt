package rs.edu.raf.kotlin.basics

// typealias for function types
typealias Predicate<T> = (T) -> Boolean

// Higher-order function: takes a function as parameter
fun <T> countMatching(items: List<T>, predicate: Predicate<T>): Int {
    var count = 0
    for (item in items) {
        if (predicate(item)) count++
    }
    return count
}

// Higher-order function: returns a function
fun multiplierOf(factor: Int): (Int) -> Int {
    return { number -> number * factor }
}

fun main() {

    // Lambda syntax
    println("=== Lambda Syntax ===")
    val square: (Int) -> Int = { x: Int -> x * x }
    println("square(5) = ${square(5)}")

    // Type inference in lambda
    val cube = { x: Int -> x * x * x }
    println("cube(3) = ${cube(3)}")

    // Implicit it parameter (single-parameter lambda)
    println()
    println("=== Implicit 'it' Parameter ===")
    val names = listOf("Marko", "Ana", "Nikola", "Jo")
    val longNames = names.filter { it.length > 3 }
    println("Names longer than 3: $longNames")

    // Trailing lambda convention
    println()
    println("=== Trailing Lambda ===")
    val count = countMatching(names) { it.startsWith("M") }
    println("Names starting with M: $count")

    // Function references with ::
    println()
    println("=== Function References (::) ===")
    val lengths = names.map(String::length)
    println("Name lengths: $lengths")

    val upperNames = names.map(String::uppercase)
    println("Uppercase: $upperNames")

    // Returning functions
    println()
    println("=== Functions Returning Functions ===")
    val triple = multiplierOf(3)
    val tenTimes = multiplierOf(10)
    println("triple(7) = ${triple(7)}")
    println("tenTimes(7) = ${tenTimes(7)}")

    // Chaining lambdas
    println()
    println("=== Chaining ===")
    val result = names
        .filter { it.length > 2 }
        .map { it.uppercase() }
        .sortedBy { it.length }
    println("Filtered, uppercased, sorted: $result")
}
