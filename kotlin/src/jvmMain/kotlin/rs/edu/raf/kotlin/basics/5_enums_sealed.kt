package rs.edu.raf.kotlin.basics

// Enum with properties and methods
enum class Priority(val level: Int) {
    LOW(1),
    MEDIUM(5),
    HIGH(10),
    CRITICAL(100);

    fun label() = "$name (level=$level)"
}

// Sealed class: restricted hierarchy, enables exhaustive when
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

// Sealed interface
sealed interface UiEvent {
    data class Click(val x: Int, val y: Int) : UiEvent
    data class TextInput(val text: String) : UiEvent
    data object Back : UiEvent
}

fun <T> handleResult(result: Result<T>): String = when (result) {
    is Result.Success -> "Success: ${result.data}"
    is Result.Error -> "Error: ${result.message}"
    Result.Loading -> "Loading..."
    // no else needed - compiler knows all cases are covered
}

fun handleEvent(event: UiEvent): String = when (event) {
    is UiEvent.Click -> "Clicked at (${event.x}, ${event.y})"
    is UiEvent.TextInput -> "Typed: '${event.text}'"
    UiEvent.Back -> "Back pressed"
}

fun main() {

    // Enum basics
    println("=== Enum Class ===")
    for (p in Priority.entries) {
        println("  ${p.label()}")
    }

    // Enum valueOf and ordinal
    println()
    val high = Priority.valueOf("HIGH")
    println("valueOf(\"HIGH\"): $high, ordinal=${high.ordinal}, level=${high.level}")

    // Enum in when
    println()
    println("=== Enum in when ===")
    val task = Priority.CRITICAL
    val action = when (task) {
        Priority.LOW -> "Add to backlog"
        Priority.MEDIUM -> "Schedule this week"
        Priority.HIGH -> "Do today"
        Priority.CRITICAL -> "Do right now!"
    }
    println("$task -> $action")

    // Sealed class
    println()
    println("=== Sealed Class ===")
    val results = listOf(
        Result.Loading,
        Result.Success("Kotlin"),
        Result.Error("Network timeout"),
    )
    for (r in results) {
        println("  ${handleResult(r)}")
    }

    // Sealed interface
    println()
    println("=== Sealed Interface ===")
    val events = listOf(
        UiEvent.Click(100, 200),
        UiEvent.TextInput("Hello"),
        UiEvent.Back,
    )
    for (e in events) {
        println("  ${handleEvent(e)}")
    }
}
