package rs.edu.raf.rma.passwords.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import rs.edu.raf.rma.passwords.core.AppBarIcon
import rs.edu.raf.rma.passwords.domain.Password

@Composable
fun PasswordsListScreen(
    viewModel: PasswordsListViewModel,
    onPasswordClick: (passwordId: Long) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    PasswordsListScreen(
        state = state,
        onPasswordClick = onPasswordClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordsListScreen(
    state: PasswordsListContract.UiState,
    onPasswordClick: ((passwordId: Long) -> Unit)? = null,
) {

    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Passwords")
                },
                actions = {
                    AppBarIcon(
                        icon = Icons.Default.Refresh,
                        onClick = {

                        },
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
            ) {
                AppBarIcon(
                    icon = Icons.Default.Add,
                    onClick = {}
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(state = scrollState),
            ) {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Error: ${state.error.message}")
                    }
                } else if (state.passwords.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "No passwords. Tap + to add new password.")
                    }
                } else {
                    state.passwords.forEach { password ->
                        PasswordListItem(
                            password = password,
                            onClick = { onPasswordClick?.invoke(password.id) },
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun PasswordListItem(
    password: Password,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = Modifier.clickable(
            enabled = onClick != null,
            onClick = { onClick?.invoke() },
        ),
        headlineContent = {
            Text(text = password.name)
        },
        supportingContent = {
            Text(text = password.url)
        },
        trailingContent = {
            Text(text = "12/12/2023")
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
            )
        }
    )
}