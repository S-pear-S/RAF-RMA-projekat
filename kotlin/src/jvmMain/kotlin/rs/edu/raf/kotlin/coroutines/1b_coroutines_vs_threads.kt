package rs.edu.raf.kotlin.coroutines

import kotlin.concurrent.thread

fun main() {
    println("main start")
    thread { doBlockingWork(num = 1, delay = 500L) }
    thread { doBlockingWork(num = 2, delay = 250L) }
    thread { doBlockingWork(num = 3, delay = 450L) }
    Thread.sleep(700L)
    println("main end")
}

private fun doBlockingWork(num: Int, delay: Long) {
    println("Start work on thread $num")
    Thread.sleep(delay)
    println("Completed work on thread $num")
}

