package rs.edu.raf.rma.passwords.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import rs.edu.raf.rma.passwords.domain.Password
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeDemoPasswordsApp2() {
    var isInAddMode by remember { mutableStateOf(false) }
    var data by remember {
        mutableStateOf(
            mutableListOf<Password>().apply {
                repeat(times = 30) { index ->
                    this.add(
                        Password(
                            id = (index + 1).toLong(),
                            name = "Google",
                            url = "https://www.google.com",
                            password = "",
                        )
                    )
                }
            }.toList()
        )
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            PasswordsTopAppBar(
                isInAddMode = isInAddMode,
                onButtonClick = {
                    isInAddMode = !isInAddMode
                }
            )
        },
        content = { paddingValues ->
            if (isInAddMode) {
                PasswordEditorContent(
                    paddingValues = paddingValues,
                    onSaveClick = { name, password ->
                        data = data.toMutableList().apply {
                            add(
                                Password(
                                    id = Random.nextLong(from = 1, until = 100),
                                    name = name,
                                    url = "https://www.google.com",
                                    password = password,
                                )
                            )
                        }
                        isInAddMode = false
                    }
                )
            } else {
                PasswordsList(
                    paddingValues = paddingValues,
                    data = data
                )
            }
        },
        bottomBar = {
            PasswordsBottomBar()
        },
    )

}

@Composable
private fun PasswordEditorContent(
    paddingValues: PaddingValues,
    onSaveClick: (name: String, password: String) -> Unit,
) {
    val name = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "Name")

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Password")

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSaveClick(name.value, password.value)
            }
        ) {
            Text(text = "Save")
        }

    }

}

@Composable
private fun PasswordsList(
    paddingValues: PaddingValues,
    data: List<Password>,
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState) {
        delay(2.seconds)
        scrollState.animateScrollTo(value = 1_000)
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(
                state = scrollState,
            ),
    ) {
        data.forEach { password ->
            PasswordListItem(
                password = password,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun PasswordsTopAppBar(
    isInAddMode: Boolean,
    onButtonClick: () -> Unit,
) {
//            CenterAlignedTopAppBar(
//                title = {
//                    Text(
//                        text = "Passwords Manager",
//                    )
//                }
//            )

    TopAppBar(
        title = {
            Text(
                text = "Passwords Manager",
            )
        },
        actions = {
            IconButton(onClick = onButtonClick) {
                Icon(
                    imageVector = if (!isInAddMode) Icons.Default.Add else Icons.Default.Close,
                    contentDescription = if (!isInAddMode) "Add" else "Close",
                )
            }
        }
    )
}

@Composable
private fun PasswordListItem(
    password: Password,
) {
    ListItem(
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

@Composable
private fun PasswordsBottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.LightGray)
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
        }
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Star, contentDescription = "Favorites")
        }
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Lock, contentDescription = "Passwords")
        }
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}