package rs.edu.raf.kotlin.basics

fun main() {

    println("=== Nullable Types ===")
    val name: String? = null
    // val len = name.length  // ERROR: only safe (?.) oruyer non-null asserted (!!) calls allowed
    println("name=$name")

    // Safe call operator ?.
    println()
    println("=== Safe Call (?.) ===")
    val length: Int? = name?.length
    println("name?.length = $length") // null

    val greeting: String? = "Hello"
    println("greeting?.length = ${greeting?.length}") // 5

    // Chaining safe calls
    println("greeting?.uppercase()?.take(3) = ${greeting?.uppercase()?.take(3)}")

    // Elvis operator ?:
    println()
    println("=== Elvis Operator (?:) ===")
    val len = name?.length ?: 0
    println("name?.length ?: 0 = $len") // 0

    val displayName = name ?: "Unknown"
    println("displayName = $displayName")

    // let for null checks
    println()
    println("=== let for Null Checks ===")
    name?.let { println("name is: $it") }       // does not execute
    greeting?.let { println("greeting is: $it") } // executes

    // Safe cast as?
    println()
    println("=== Safe Cast (as?) ===")
    val obj: Any = "Kotlin"
    val str: String? = obj as? String
    val num: Int? = obj as? Int
    println("as? String = $str")  // Kotlin
    println("as? Int = $num")     // null




    // Non-null assertion !! (use sparingly - throws NPE if null)
    println()
    println("=== Non-null Assertion (!!) ===")
    val definitelyNotNull: String? = "I exist"
    println("!!: ${definitelyNotNull!!.length}")
    // val crash = name!!.length  // Would throw NullPointerException
}
