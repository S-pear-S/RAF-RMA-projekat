package rs.edu.raf.rma

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import rs.edu.raf.rma.passwords.PasswordsNavigation
import rs.edu.raf.rma.posts.PostsNavigation

enum class AppTab { Posts, Passwords }

@Composable
fun AppNavigation() {
    var selectedTab by remember { mutableStateOf(AppTab.Posts) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == AppTab.Posts,
                    onClick = { selectedTab = AppTab.Posts },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Posts") },
                )
                NavigationBarItem(
                    selected = selectedTab == AppTab.Passwords,
                    onClick = { selectedTab = AppTab.Passwords },
                    icon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    label = { Text("Passwords") },
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()),
        ) {
            when (selectedTab) {
                AppTab.Posts -> PostsNavigation(
                    startDestination = "posts",
                    onNavigateToPasswords = { selectedTab = AppTab.Passwords },
                )
                AppTab.Passwords -> PasswordsNavigation(
                    startDestination = "passwords",
                )
            }
        }
    }
}
