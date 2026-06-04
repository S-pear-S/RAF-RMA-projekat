package rs.edu.raf.kotlin.basics

data class Point(val x: Double, val y: Double) {
    // Methods can be added to data classes
    fun distanceTo(other: Point): Double {
        val dx = x - other.x
        val dy = y - other.y
        return Math.sqrt(dx * dx + dy * dy)
    }
}

data class Person(
    val name: String,
    val email: String,
    val age: Int = 20,
)

fun main() {

    // Auto-generated toString
    println("=== toString ===")
    val p1 = Person("Marko", "marko@raf.rs")
    val p2 = Person("Marko", "marko@raf.rs")
    val p3 = Person("Ana", "ana@raf.rs", age = 22)
    println("p1: $p1")

    // Auto-generated equals (structural equality)
    println()
    println("=== Equality ===")
    println("p1 == p2: ${p1 == p2}")   // true  (structural)
    println("p1 === p2: ${p1 === p2}") // false (different instances)
    println("p1 == p3: ${p1 == p3}")   // false (different data)

    // copy() with named parameters
    println()
    println("=== copy() ===")
    val p4 = p1.copy(age = 25)
    println("p1: $p1")
    println("p4 (copy with age=25): $p4")
    println("p1 unchanged: ${p1.age}") // original not modified

    // Destructuring declarations
    println()
    println("=== Destructuring ===")
    val (name, email, age) = p3
    println("name=$name, email=$email, age=$age")

    // Underscore to skip components
    val (_, emailOnly) = p1
    println("email only: $emailOnly")

    // Destructuring in loops
    println()
    println("=== Destructuring in Loops ===")
    val people = listOf(p1, p3)
    for ((n, e, a) in people) {
        println("  $n ($e) age $a")
    }

    // Data class with methods
    println()
    println("=== Data Class with Methods ===")
    val origin = Point(0.0, 0.0)
    val target = Point(3.0, 4.0)
    println("Distance from $origin to $target = ${origin.distanceTo(target)}")
}
