package rs.edu.raf.kotlin.basics

data class StudentRecord(val name: String, val grade: Int, val year: Int)

fun main() {

    val students = listOf(
        StudentRecord("Marko", 9, 3),
        StudentRecord("Ana", 10, 2),
        StudentRecord("Nikola", 7, 3),
        StudentRecord("Jelena", 8, 1),
        StudentRecord("Stefan", 10, 2),
        StudentRecord("Mina", 6, 1),
    )

    // filter / filterNot
    println("=== filter ===")
    val topStudents = students.filter { it.grade >= 9 }
    println("Grade >= 9: $topStudents")

    val notTop = students.filterNot { it.grade >= 9 }
    println("Grade < 9: ${notTop.map { it.name }}")

    // map
    println()
    println("=== map ===")
    val names = students.map { it.name }
    println("Names: $names")

    val summaries = students.map { "${it.name} (${it.grade})" }
    println("Summaries: $summaries")

    // find / first / firstOrNull
    println()
    println("=== find ===")
    val ana = students.find { it.name == "Ana" }
    println("find Ana: $ana")
    val unknown = students.find { it.name == "Unknown" }
    println("find Unknown: $unknown")

    // any / all / none
    println()
    println("=== any / all / none ===")
    println("any grade 10: ${students.any { it.grade == 10 }}")
    println("all passed (>= 6): ${students.all { it.grade >= 6 }}")
    println("none failed (< 6): ${students.none { it.grade < 6 }}")

    // count / sumOf / average
    println()
    println("=== Aggregation ===")
    println("count year 3: ${students.count { it.year == 3 }}")
    println("sum of grades: ${students.sumOf { it.grade }}")
    println("average grade: ${students.map { it.grade }.average()}")
    println("max grade: ${students.maxOf { it.grade }}")
    println("min grade: ${students.minOf { it.grade }}")

    // sortedBy
    println()
    println("=== Sorting ===")
    val byGrade = students.sortedByDescending { it.grade }
    println("By grade desc: ${byGrade.map { "${it.name}:${it.grade}" }}")

    val byNameLength = students.sortedBy { it.name.length }
    println("By name length: ${byNameLength.map { it.name }}")

    // groupBy
    println()
    println("=== groupBy ===")
    val byYear = students.groupBy { it.year }
    for ((year, group) in byYear) {
        println("  Year $year: ${group.map { it.name }}")
    }

    // associateBy
    println()
    println("=== associateBy ===")
    val byName = students.associateBy { it.name }
    println("byName[\"Ana\"] = ${byName["Ana"]}")

    // flatMap
    println()
    println("=== flatMap ===")
    val courses = mapOf(
        "Kotlin" to listOf("Marko", "Ana"),
        "Java" to listOf("Ana", "Nikola"),
    )
    val allStudentNames = courses.flatMap { it.value }.distinct()
    println("All students: $allStudentNames")

    // fold / reduce
    println()
    println("=== fold ===")
    val gradeSum = students.map { it.grade }.fold(0) { acc, grade -> acc + grade }
    println("Grade sum (fold): $gradeSum")

    // Chaining: top 3 students by grade, formatted
    println()
    println("=== Chaining ===")
    val leaderboard = students
        .sortedByDescending { it.grade }
        .take(3)
        .mapIndexed { i, s -> "${i + 1}. ${s.name} (grade: ${s.grade})" }
        .joinToString("\n  ")
    println("Top 3:\n  $leaderboard")

    // zip
    println()
    println("=== zip ===")
    val subjects = listOf("Math", "CS", "Physics")
    val scores = listOf(9, 10, 8)
    val paired = subjects.zip(scores) { subj, score -> "$subj: $score" }
    println("Zipped: $paired")

    // partition
    println()
    println("=== partition ===")
    val (passed, atRisk) = students.partition { it.grade >= 8 }
    println("Passed: ${passed.map { it.name }}")
    println("At risk: ${atRisk.map { it.name }}")
}
