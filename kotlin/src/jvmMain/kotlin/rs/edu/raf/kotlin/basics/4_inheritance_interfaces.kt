package rs.edu.raf.kotlin.basics

// open = can be inherited (classes are final by default in Kotlin)
open class Shape(val name: String) {
    open fun area(): Double = 0.0
    override fun toString() = "$name (area=${String.format("%.2f", area())})"
}

class Circle(val radius: Double) : Shape("Circle") {
    override fun area() = Math.PI * radius * radius
}

class Rectangle(val width: Double, val height: Double) : Shape("Rectangle") {
    override fun area() = width * height
}

// Abstract class
abstract class Animal(val name: String) {
    abstract fun sound(): String
    fun describe() = "$name says '${sound()}'"
}

class Dog(name: String) : Animal(name) {
    override fun sound() = "Woof"
}

class Cat(name: String) : Animal(name) {
    override fun sound() = "Meow"
}

// Interfaces
interface Drawable {
    fun draw(): String
}

interface Resizable {
    val maxSize: Int get() = 100 // default implementation
    fun resize(factor: Double): String
}

// Multiple interface implementation
class Canvas(val label: String) : Drawable, Resizable {
    override fun draw() = "Drawing canvas '$label'"
    override fun resize(factor: Double) = "Resizing '$label' by ${factor}x (max: $maxSize)"
}

// Resolving conflicts
interface A {
    fun greet() = "Hello from A"
}

interface B {
    fun greet() = "Hello from B"
}

class C : A, B {
    // Must override to resolve conflict
    override fun greet() = "${super<A>.greet()} and ${super<B>.greet()}"
}

fun main() {

    println("=== Inheritance ===")
    val shapes: List<Shape> = listOf(
        Circle(5.0),
        Rectangle(3.0, 4.0),
    )
    for (shape in shapes) {
        println("  $shape") // polymorphism: calls overridden area()
    }

    println()
    println("=== Abstract Classes ===")
    val animals: List<Animal> = listOf(Dog("Rex"), Cat("Whiskers"))
    for (animal in animals) {
        println("  ${animal.describe()}")
    }
    // val a = Animal("?")  // ERROR: cannot instantiate abstract class

    println()
    println("=== Interfaces ===")
    val canvas = Canvas("Main")
    println(canvas.draw())
    println(canvas.resize(2.5))

    println()
    println("=== Resolving Interface Conflicts ===")
    println(C().greet())
}
