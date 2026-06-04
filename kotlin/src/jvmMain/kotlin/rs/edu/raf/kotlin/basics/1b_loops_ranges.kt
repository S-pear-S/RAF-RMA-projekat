package rs.edu.raf.kotlin.basics

fun main() {

    // Ranges
    println("=== Ranges ===")
    val oneToFive = 1..5        // inclusive: 1, 2, 3, 4, 5
    val oneToFour = 1 until 5   // exclusive end: 1, 2, 3, 4
    println("1..5 = ${oneToFive.toList()}")
    println("1 until 5 = ${oneToFour.toList()}")
    println("3 in 1..5 = ${3 in oneToFive}")
    println("6 in 1..5 = ${6 in oneToFive}")

    // for loops
    println()
    println("=== for Loops ===")
    print("1..5: ")
    for (i in 1..5) print("$i ")
    println()

    print("10 downTo 1 step 2: ")
    for (i in 10 downTo 1 step 2) print("$i ")
    println()

    // for over collection
    val fruits = listOf("Apple", "Banana", "Cherry")
    print("fruits: ")
    for (fruit in fruits) print("$fruit ")
    println()

    // for with index
    println("with index:")
    for ((index, fruit) in fruits.withIndex()) {
        println("  [$index] $fruit")
    }

    // while
    println()
    println("=== while ===")
    var count = 3
    while (count > 0) {
        print("$count ")
        count--
    }
    println("Go!")

    // repeat
    println()
    println("=== repeat ===")
    repeat(3) { i ->
        println("  Attempt ${i + 1}")
    }

    // Labels: break and continue
    println()
    println("=== Labels ===")
    print("Outer/inner (skip i=1,j=1): ")
    outer@ for (i in 0..2) {
        for (j in 0..2) {
            if (i == 1 && j == 1) continue@outer
            print("($i,$j) ")
        }
    }
    println()
}
