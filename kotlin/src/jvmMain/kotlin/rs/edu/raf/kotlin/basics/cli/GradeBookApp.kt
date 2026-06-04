package rs.edu.raf.kotlin.basics.cli

fun printHelp() {
    println("""
        |Available commands:
        |  add <name> <email>              Add a student
        |  grade <email> <subject> <score> Record a grade (1-10)
        |  list                            List all students
        |  stats <email>                   Show student statistics
        |  top [n]                         Show top N students (default: 3)
        |  search <query>                  Search by name or email
        |  help                            Show this help
        |  quit                            Exit the app
    """.trimMargin())
}

fun main() {
    val gradeBook = GradeBook()

    // Seed with sample data
    gradeBook.addStudent("Marko", "marko@raf.rs")
    gradeBook.addStudent("Ana", "ana@raf.rs")
    gradeBook.addStudent("Nikola", "nikola@raf.rs")
    gradeBook.addGrade("marko@raf.rs", "Kotlin", 9.0)
    gradeBook.addGrade("marko@raf.rs", "Math", 8.0)
    gradeBook.addGrade("ana@raf.rs", "Kotlin", 10.0)
    gradeBook.addGrade("ana@raf.rs", "Math", 10.0)
    gradeBook.addGrade("nikola@raf.rs", "Kotlin", 7.0)

    println("=== GradeBook CLI ===")
    println("Type 'help' for available commands.")
    println()

    while (true) {
        print("> ")
        val input = readlnOrNull() ?: break
        if (input.isBlank()) continue

        val command = input.parseCommand()

        val output = when (command) {
            is Command.AddStudent -> gradeBook.addStudent(command.name, command.email)
            is Command.AddGrade -> gradeBook.addGrade(command.email, command.subject, command.score)
            is Command.ListStudents -> gradeBook.listAll()
            is Command.StudentStats -> gradeBook.stats(command.email)
            is Command.TopStudents -> gradeBook.top(command.count)
            is Command.Search -> gradeBook.search(command.query)
            is Command.Help -> { printHelp(); null }
            is Command.Quit -> { println("Bye!"); return }
            null -> "Unknown command. Type 'help' for available commands."
        }
        output?.let { println(it) }
        println()
    }
}
