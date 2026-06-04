package rs.edu.raf.kotlin.basics

// Primary constructor with val/var parameters (auto-creates properties)
class Student(
    val name: String,
    val email: String,
    var year: Int = 1,
) {
    // init block runs when instance is created
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(year in 1..4) { "Year must be 1-4" }
    }

    // Computed property (no backing field)
    val isGraduating: Boolean
        get() = year == 4

    // Method
    fun advance() {
        if (year < 4) year++
    }

    override fun toString() = "$name ($email, year $year)"
}

// Class with private state and controlled access
class Course(val title: String, private val maxStudents: Int = 30) {

    // Private mutable list, exposed as read-only
    private val _students = mutableListOf<Student>()
    val students: List<Student> get() = _students

    val isFull: Boolean get() = _students.size >= maxStudents

    fun enroll(student: Student): Boolean {
        if (isFull) return false
        _students.add(student)
        return true
    }

    fun enrolled(): Int = _students.size

    override fun toString() = "$title (${enrolled()}/$maxStudents students)"
}

fun main() {

    println("=== Primary Constructor ===")
    val s1 = Student("Marko", "marko@raf.rs")
    val s2 = Student("Ana", "ana@raf.rs", year = 3)
    println("s1: $s1")
    println("s2: $s2")
    println("s2.isGraduating = ${s2.isGraduating}")

    s2.advance()
    println("After advance: $s2, isGraduating = ${s2.isGraduating}")

    // init block validates
    // val invalid = Student("", "x@raf.rs")  // throws IllegalArgumentException

    println()
    println("=== Encapsulation ===")
    val course = Course("Kotlin 101", maxStudents = 2)
    println(course)
    println("enroll s1: ${course.enroll(s1)}")
    println("enroll s2: ${course.enroll(s2)}")
    println("enroll again: ${course.enroll(Student("Nikola", "n@raf.rs"))}")
    println(course)
    println("Students: ${course.students}")
    // course._students.add(...)  // ERROR: _students is private
}
