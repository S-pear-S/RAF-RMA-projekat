package rs.edu.raf.kotlin.basics.cli

fun String.parseCommand(): Command? {
    val parts = trim().split("\\s+".toRegex())
    val keyword = parts.firstOrNull()?.lowercase() ?: return null

    return when (keyword) {
        "add" -> {
            val name = parts.getOrNull(1) ?: return null
            val email = parts.getOrNull(2) ?: return null
            Command.AddStudent(name, email)
        }
        "grade" -> {
            val email = parts.getOrNull(1) ?: return null
            val subject = parts.getOrNull(2) ?: return null
            val score = parts.getOrNull(3)?.toDoubleOrNull() ?: return null
            if (score !in 1.0..10.0) return null
            Command.AddGrade(email, subject, score)
        }
        "list" -> Command.ListStudents
        "stats" -> {
            val email = parts.getOrNull(1) ?: return null
            Command.StudentStats(email)
        }
        "top" -> {
            val count = parts.getOrNull(1)?.toIntOrNull() ?: 3
            Command.TopStudents(count)
        }
        "search" -> {
            val query = parts.drop(1).joinToString(" ").takeIf { it.isNotBlank() } ?: return null
            Command.Search(query)
        }
        "help" -> Command.Help
        "quit", "exit" -> Command.Quit
        else -> null
    }
}
