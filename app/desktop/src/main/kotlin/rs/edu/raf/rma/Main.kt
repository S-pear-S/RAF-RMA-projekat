package rs.edu.raf.rma

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import rs.edu.raf.rma.di.initKoin
import rs.edu.raf.rma.posts.PostsApp

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Demo",
        ) {
//            PasswordsApp()
            PostsApp()
        }
    }
}
