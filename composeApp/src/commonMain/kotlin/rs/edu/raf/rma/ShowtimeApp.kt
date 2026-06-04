package rs.edu.raf.rma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.rma.auth.AuthLandingScreen
import rs.edu.raf.rma.auth.LoginScreen
import rs.edu.raf.rma.auth.RegisterScreen
import rs.edu.raf.rma.core.auth.model.AuthState
import rs.edu.raf.rma.navigation.MainNavigation
import rs.edu.raf.rma.posts.splash.BootState
import rs.edu.raf.rma.posts.splash.SplashScreen
import rs.edu.raf.rma.posts.splash.SplashViewModel

@Composable
fun ShowtimeApp() {
    val splashViewModel: SplashViewModel = koinViewModel()
    val bootState by splashViewModel.bootState.collectAsState()
    val isLoggedIn by splashViewModel.isLoggedIn.collectAsState()

    when (bootState) {
        BootState.Loading -> SplashScreen()
        is BootState.Failed -> SplashScreen()
        BootState.Success -> {
            if (isLoggedIn) {
                MainNavigation(onLogout = { splashViewModel.onLogout() })
            } else {
                AuthNavigation(onAuthenticated = { splashViewModel.onAuthenticated() })
            }
        }
    }
}

@Composable
private fun AuthNavigation(onAuthenticated: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth_landing") {
        composable("auth_landing") {
            AuthLandingScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") },
            )
        }
        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register") { popUpTo("auth_landing") }
                },
                onSuccess = onAuthenticated,
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate("login") { popUpTo("auth_landing") }
                },
                onSuccess = onAuthenticated,
            )
        }
    }
}
