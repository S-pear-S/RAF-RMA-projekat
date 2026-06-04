package rs.edu.raf.kotlin.basics

fun main() {

    // if is an expression (returns a value)
    println("=== if Expression ===")
    val score = 85
    val result = if (score >= 50) "Passed" else "Failed"
    println("Score $score: $result")

    val grade = if (score >= 90) "A"
        else if (score >= 80) "B"
        else if (score >= 70) "C"
        else "F"
    println("Grade: $grade")

    // when expression (replaces switch)
    println()
    println("=== when Expression ===")
    val day = 3
    val dayName = when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6, 7 -> "Weekend"
        else -> "Invalid"
    }
    println("Day $day = $dayName")

    // when with ranges and is-checks
    println()
    println("=== when with Ranges ===")
    val age = 21
    val category = when (age) {
        in 0..12 -> "Child"
        in 13..17 -> "Teenager"
        in 18..64 -> "Adult"
        in 65..Int.MAX_VALUE -> "Senior"
        else -> "Invalid age"
    }
    println("Age $age -> $category")

    // when with type checks (is)
    println()
    println("=== when with Type Checks ===")
    val items: List<Any> = listOf("Hello", 42, 3.14, true, listOf(1, 2))
    for (item in items) {
        val description = when (item) {
            is String -> "String of length ${item.length}"   // smart cast
            is Int -> "Int: ${item * 2}"                     // smart cast
            is Boolean -> "Boolean: ${!item}"                // smart cast
            is List<*> -> "List with ${item.size} elements"  // smart cast
            else -> "Unknown: $item"
        }
        println("  $item -> $description")
    }

    // when without argument (replaces if-else chains)
    println()
    println("=== when Without Argument ===")
    val temperature = 25
    val weather = when {
        temperature < 0 -> "Freezing"
        temperature < 15 -> "Cold"
        temperature < 25 -> "Pleasant"
        temperature < 35 -> "Warm"
        else -> "Hot"
    }
    println("${temperature}°C -> $weather")
}
