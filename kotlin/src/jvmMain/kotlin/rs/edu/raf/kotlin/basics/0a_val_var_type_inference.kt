package rs.edu.raf.kotlin.basics

import kotlin.time.Clock

const val APP_NAME = "Kotlin Basics"

fun main() {

    // val = immutable (read-only), var = mutable
    val name: String = "Marko"
    var grade = 8.5 // type inferred as Double
    grade = 9.0     // OK, var can be reassigned
    // name = "Janko"  // ERROR: val cannot be reassigned

    // Basic types
    val age: Int = 21
    val initial: Char = 'M'
    val passed: Boolean = true
    val pi: Double = 3.14159
    val billion: Long = 1_000_000_000L

    println("=== Variables and Types ===")
    println("name=$name, age=$age, grade=$grade")
    println("initial=$initial, passed=$passed, pi=$pi")
    println("billion=$billion")

    // String templates
    println()
    println("=== String Templates ===")
    println("Student $name is $age years old.")
    println("Name has ${name.length} characters.")
    println("Grade rounded: ${grade.toInt()}")

    // const val vs val
    println()
    println("=== Const ===")
    println("App: $APP_NAME") // const val - compile-time constant
    val runtime = Clock.System.now().toEpochMilliseconds() // val - assigned at runtime
    println("Runtime val: $runtime")

    // Type conversions (explicit, no implicit widening)
    println()
    println("=== Type Conversions ===")
    val intVal: Int = 42
    val longVal: Long = intVal.toLong()  // explicit conversion required
    val doubleVal: Double = intVal.toDouble()
    println("Int=$intVal, Long=$longVal, Double=$doubleVal")

    // Any - root of the type hierarchy
    val anything: Any = "can be anything"
    println("Any: $anything (${anything::class.simpleName})")
}
