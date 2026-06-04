package rs.edu.raf.kotlin.basics

data class ServerConfig(
    var host: String = "localhost",
    var port: Int = 8080,
    var debug: Boolean = false,
)

fun main() {

    // let: transforms object, uses 'it', returns lambda result
    println("=== let ===")
    val name: String? = "Kotlin"
    val length = name?.let {
        println("Processing '$it'")
        it.length
    }

//    if (name != null) {
//        println("Processing '$name'")
//        name.length
//    }


    println("Length: $length")

    val nothing: String? = null
    nothing?.let { println("This won't print") }
    println("nothing?.let returned: ${nothing?.let { it.length }}")

    // apply: configures object, uses 'this', returns the object itself
    println()
    println("=== apply ===")
    val config = ServerConfig().apply {
        host = "192.168.1.1" // 'this.' is implicit
        port = 3000
        debug = true
    }
    println("Config: $config")

    // also: side effects, uses 'it', returns the object itself
    println()
    println("=== also ===")
    val numbers = mutableListOf(1, 2, 3)
        .also { println("Created list: $it") }
        .also { it.add(4) }
    println("Numbers: $numbers")

    // run: compute on object, uses 'this', returns lambda result
    println()
    println("=== run ===")
    val greeting = "Hello, World!".run {
        println("Processing '$this'")
        uppercase().take(5)
    }
    println("Result: $greeting")

    // run without receiver: just executes a block
    val result = run {
        val a = 10
        val b = 20
        a + b
    }
    println("run { 10 + 20 } = $result")

    // with: same as run but takes object as argument (non-extension)
    println()
    println("=== with ===")
    val info = with(config) {
        "Server at $host:$port (debug=$debug)"
    }
    println(info)

    // Summary
    println()
    println("=== Summary ===")
    println("Function | Ref  | Returns        | Use case")
    println("---------|------|----------------|---------------------------")
    println("let      | it   | lambda result  | null checks, transform")
    println("run      | this | lambda result  | compute on object")
    println("with     | this | lambda result  | group calls (non-extension)")
    println("apply    | this | object itself  | object configuration")
    println("also     | it   | object itself  | side effects, logging")
}
