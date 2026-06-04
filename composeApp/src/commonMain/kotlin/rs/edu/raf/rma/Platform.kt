package rs.edu.raf.rma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform