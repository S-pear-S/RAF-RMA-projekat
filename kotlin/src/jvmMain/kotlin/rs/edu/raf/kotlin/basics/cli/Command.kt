package rs.edu.raf.kotlin.basics.cli

sealed interface Command {
    data class AddStudent(val name: String, val email: String) : Command
    data class AddGrade(val email: String, val subject: String, val score: Double) : Command
    data object ListStudents : Command
    data class StudentStats(val email: String) : Command
    data class TopStudents(val count: Int) : Command
    data class Search(val query: String) : Command
    data object Help : Command
    data object Quit : Command
}
