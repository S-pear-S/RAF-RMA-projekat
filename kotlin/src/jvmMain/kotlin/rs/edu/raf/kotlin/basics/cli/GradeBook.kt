package rs.edu.raf.kotlin.basics.cli

data class Grade(val subject: String, val score: Double)

data class Student(val name: String, val email: String) {
    private val _grades = mutableListOf<Grade>()
    val grades: List<Grade> get() = _grades

    fun addGrade(grade: Grade) {
        _grades.add(grade)
    }

    val average: Double
        get() = if (_grades.isEmpty()) 0.0 else _grades.sumOf { it.score } / _grades.size
}

fun Student.format(): String = buildString {
    append("$name <$email>")
    if (grades.isNotEmpty()) {
        append(" | avg: ${String.format("%.1f", average)}")
        append(" | grades: ${grades.joinToString { "${it.subject}:${it.score}" }}")
    } else {
        append(" | no grades")
    }
}

class GradeBook {

    private val students = mutableListOf<Student>()

    fun addStudent(name: String, email: String): String {
        if (students.any { it.email == email }) {
            return "Student with email '$email' already exists."
        }
        val student = Student(name, email).also { students.add(it) }
        return "Added: ${student.name} <${student.email}>"
    }

    fun addGrade(email: String, subject: String, score: Double): String {
        val student = students.find { it.email == email }
            ?: return "Student '$email' not found."
        student.addGrade(Grade(subject, score))
        return "Recorded ${student.name}: $subject = $score"
    }

    fun listAll(): String {
        if (students.isEmpty()) return "No students registered."
        return students
            .sortedBy { it.name }
            .mapIndexed { i, s -> "${i + 1}. ${s.format()}" }
            .joinToString("\n")
    }

    fun stats(email: String): String {
        val student = students.find { it.email == email }
            ?: return "Student '$email' not found."
        if (student.grades.isEmpty()) return "${student.name} has no grades yet."

        val best = student.grades.maxBy { it.score }
        val worst = student.grades.minBy { it.score }
        return buildString {
            appendLine("${student.name} <${student.email}>")
            appendLine("  Grades: ${student.grades.size}")
            appendLine("  Average: ${String.format("%.1f", student.average)}")
            appendLine("  Best: ${best.subject} (${best.score})")
            append("  Worst: ${worst.subject} (${worst.score})")
        }
    }

    fun top(count: Int): String {
        val ranked = students
            .filter { it.grades.isNotEmpty() }
            .sortedByDescending { it.average }
            .take(count)
        if (ranked.isEmpty()) return "No students with grades yet."
        return ranked
            .mapIndexed { i, s -> "${i + 1}. ${s.name} (avg: ${String.format("%.1f", s.average)})" }
            .joinToString("\n")
    }

    fun search(query: String): String {
        val results = students.filter {
            it.name.contains(query, ignoreCase = true) ||
                it.email.contains(query, ignoreCase = true)
        }
        if (results.isEmpty()) return "No students matching '$query'."
        return results.joinToString("\n") { "  ${it.format()}" }
    }
}
