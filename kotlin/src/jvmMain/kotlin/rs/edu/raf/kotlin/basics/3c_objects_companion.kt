package rs.edu.raf.kotlin.basics

// object declaration = singleton
object Logger {
    private val entries = mutableListOf<String>()

    fun log(message: String) {
        MyAppHelper.getDescription()
        entries.add(message)
        println("[LOG] $message")
    }

    fun dump() {
        println("Log entries (${entries.size}):")
        entries.forEach { println("  - $it") }
    }
}

object MyAppHelper {
    fun getDescription(): String = "This is fancy description."
}


// companion object = static-like members
class User private constructor(val name: String, val role: String) {

    companion object Factory {
        // Factory methods
        fun student(name: String) = User(name, "student")
        fun professor(name: String) = User(name, "professor")

        // Constants
        const val MAX_NAME_LENGTH = 50
    }

    override fun toString() = "$name ($role)"
}

fun main() {


//    User.Factory.MAX_NAME_LENGTH
//
//    User.Factory.student("Marko")
//    User.Factory.student("Marko")

    // Singleton - same instance everywhere
    println("=== Object (Singleton) ===")
    Logger.log("App started")
    Logger.log("User logged in")
    Logger.dump()

    // Companion object - factory pattern
    println()
    println("=== Companion Object (Factory) ===")
    val s = User.student("Marko")
    val p = User.professor("Dr. Jovanovic")
    println("Student: $s")
    println("Professor: $p")
    println("Max name length: ${User.MAX_NAME_LENGTH}")

//     User("Marko", "student")  // ERROR: constructor is private

    // Anonymous object (object expression)
    println()
    println("=== Anonymous Object ===")
    val comparator = object : Comparator<String> {
        override fun compare(a: String, b: String): Int {
            return a.length - b.length
        }
    }
    val sorted = listOf("Kotlin", "Go", "Java", "C").sortedWith(comparator)
    println("Sorted by length: $sorted")
}
