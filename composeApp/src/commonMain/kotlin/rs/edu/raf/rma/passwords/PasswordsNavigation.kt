package rs.edu.raf.rma.passwords

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.rma.passwords.details.PasswordDetailsViewModel
import rs.edu.raf.rma.passwords.details.PasswordsDetailsScreen
import rs.edu.raf.rma.passwords.list.PasswordsListScreen
import rs.edu.raf.rma.passwords.list.PasswordsListViewModel

@Composable
fun PasswordsNavigation(
    startDestination: String,
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            route = "passwords",
        ) {
            val viewModel = koinViewModel<PasswordsListViewModel>()
            PasswordsListScreen(
                viewModel = viewModel,
                onPasswordClick = {
                    navController.navigateToPasswordDetails(passwordId = it)
                }
            )
        }

        composable(
            route = "passwords/{$PASSWORD_ID}",
            arguments = listOf(
                navArgument(PASSWORD_ID) {
                    type = NavType.LongType
                    nullable = false
                }
            ),
        ) {
            val viewModel = koinViewModel<PasswordDetailsViewModel>()
            PasswordsDetailsScreen(
                viewModel = viewModel,
                onClose = {
                    navController.navigateUp()
                }
            )
        }

    }
}

private fun NavController.navigateToPasswordDetails(passwordId: Long) {
    navigate("passwords/$passwordId")
}

const val PASSWORD_ID = "passwordId"
inline val SavedStateHandle.passwordId: Long? get() = get(PASSWORD_ID)
inline val SavedStateHandle.passwordIdOrThrow: Long get() = get(PASSWORD_ID)
    ?: throw IllegalStateException("$PASSWORD_ID is mandatory and can not be null")
