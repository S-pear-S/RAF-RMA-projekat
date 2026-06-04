package rs.edu.raf.kotlin.basics

// Extension function on String
fun String.initials(): String =
    split(" ")
        .filter { it.isNotBlank() }
        .map { it.first().uppercase() }
        .joinToString("")

// Extension function on Int
fun Int.isEven(): Boolean = this % 2 == 0

// Extension function on List
fun <T> List<T>.secondOrNull(): T? = if (size >= 2) this[1] else null

// Extension property
val String.wordCount: Int
    get() = split("\\s+".toRegex()).count { it.isNotBlank() }

// Extension on nullable type
fun String?.orDefault(default: String = "N/A"): String = this ?: default

// Extension on custom class
data class Money(val amount: Double, val currency: String)

fun Money.format(): String = String.format("%.2f %s", amount, currency)

operator fun Money.plus(other: Money): Money {
    require(currency == other.currency) { "Cannot add different currencies" }
    return Money(amount + other.amount, currency)
}

fun main() {

    // String extension
    println("=== Extension Functions ===")
    println("\"Marko Markovic\".initials() = ${"Marko Markovic".initials()}")
    println("\"Ana Maria Jovic\".initials() = ${"Ana Maria Jovic".initials()}")

    // Int extension
    println()
    println("=== Extension on Int ===")
    for (i in 1..6) {
        println("  $i.isEven() = ${i.isEven()}")
    }

    // List extension
    println()
    println("=== Extension on List ===")
    println("listOf(1,2,3).secondOrNull() = ${listOf(1, 2, 3).secondOrNull()}")
    println("listOf(1).secondOrNull() = ${listOf(1).secondOrNull()}")
    println("emptyList<Int>().secondOrNull() = ${emptyList<Int>().secondOrNull()}")

    // Extension property
    println()
    println("=== Extension Property ===")
    val text = "Kotlin is a modern language"
    println("\"$text\".wordCount = ${text.wordCount}")

    // Nullable extension
    println()
    println("=== Nullable Extension ===")
    val name: String? = null
    val present: String? = "Kotlin"
    println("null.orDefault() = ${name.orDefault()}")
    println("\"Kotlin\".orDefault() = ${present.orDefault()}")

    // Custom class extension
    println()
    println("=== Extension on Custom Class ===")
    val price = Money(19.99, "EUR")
    val tax = Money(3.20, "EUR")
    println("Price: ${price.format()}")
    println("Tax: ${tax.format()}")
    println("Total: ${(price + tax).format()}")
}
