package rs.edu.raf.rma.passwords

import androidx.compose.runtime.Composable

@Composable
fun PasswordsApp() {
//    NetworkingVerificationScreen()
    PasswordsNavigation(
        startDestination = "passwords"
    )
}