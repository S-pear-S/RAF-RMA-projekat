package rs.edu.raf.rma.passwords.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import rs.edu.raf.rma.passwords.core.AppBarIcon

@Composable
fun PasswordsDetailsScreen(
    viewModel: PasswordDetailsViewModel,
    onClose: () -> Unit,
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.error) {
        withContext(Dispatchers.IO) {
            if (state.error != null) {
//            fsadfdasfas
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                PasswordDetailsContract.SideEffect.DataDeleted -> {
                    onClose()
                }
            }
        }
    }

    PasswordDetailsScreen(
        state = state,
        onClose = onClose,
        eventPublisher = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordDetailsScreen(
    state: PasswordDetailsContract.UiState,
    eventPublisher: (PasswordDetailsContract.UiEvent) -> Unit,
    onClose: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Passwords Details")
                },
                navigationIcon = {
                    AppBarIcon(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        onClick = onClose,
                    )
                },
                actions = {
                    AppBarIcon(
                        icon = Icons.Default.Delete,
                        onClick = {
                            eventPublisher(
                                PasswordDetailsContract.UiEvent.DeleteData
                            )
                        },
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(state = scrollState),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "$state")
                }
            }
        },
    )
}