package rs.edu.raf.kotlin.basics

// Single-expression function
fun double(x: Int) = x * 2

// Default parameter values
fun greet(name: String, greeting: String = "Hello") = "$greeting, $name!"

// vararg
fun sum(vararg numbers: Int): Int = numbers.sum()

// Function returning Unit (void equivalent)
fun log(message: String): Unit {
    println("[LOG] $message")
}

// Multiple return values via Pair
fun divide(a: Int, b: Int): Pair<Int, Int> = Pair(a / b, a % b)

fun main() {

    println("=== Single-expression Functions ===")
    println("double(5) = ${double(5)}")

    // Default arguments
    println()
    println("=== Default Arguments ===")
    println(greet("Marko"))            // uses default greeting
    println(greet("Marko", "Hi"))      // overrides default

    // Named arguments
    println()
    println("=== Named Arguments ===")
    println(greet(greeting = "Hey", name = "Nikola")) // order doesn't matter

    // vararg
    println()
    println("=== Vararg ===")
    println("sum(1, 2, 3) = ${sum(1, 2, 3)}")
    println("sum(10, 20) = ${sum(10, 20)}")

    // Spread operator for passing arrays to vararg
    val nums = intArrayOf(4, 5, 6)
    println("sum(*intArrayOf) = ${sum(*nums)}")

    // Pair / destructuring return values
    println()
    println("=== Multiple Return Values ===")
    val (quotient, remainder) = divide(17, 5)
    println("17 / 5 = $quotient remainder $remainder")

    // Local functions (function inside a function)
    println()
    println("=== Local Functions ===")
    fun validate(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }
    println("validate('Marko') = ${validate("Marko")}")
    println("validate('') = ${validate("")}")
    println("validate('A') = ${validate("A")}")
}
