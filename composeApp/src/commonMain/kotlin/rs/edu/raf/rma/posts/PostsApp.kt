package rs.edu.raf.rma.posts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.rma.AppNavigation
import rs.edu.raf.rma.posts.splash.BootState
import rs.edu.raf.rma.posts.splash.SplashScreen
import rs.edu.raf.rma.posts.splash.SplashViewModel

@Composable
fun PostsApp() {
    val splashViewModel: SplashViewModel = koinViewModel()
    val bootState by splashViewModel.bootState.collectAsState()

    when (bootState) {
        BootState.Success -> {
            AppNavigation()
        }

        is BootState.Failed -> {
            // Ovde opciono mozete prikazati ekran u slucaju
            // da app ne moze da se startuje iz bilo kog razloga
            // npr. verzija vise nije podrzana,
            // ili auth podaci su korumpirani,
            // ili sta god da bude potrebno.
        }

        BootState.Loading -> {
            SplashScreen()
        }
    }

}


@Composable
fun OldPostsApp() {
    PostsNavigation(
        startDestination = "posts"
    )
}
