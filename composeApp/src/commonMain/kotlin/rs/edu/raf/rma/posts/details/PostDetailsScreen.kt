package rs.edu.raf.rma.posts.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import rs.edu.raf.rma.passwords.core.AppBarIcon
import rs.edu.raf.rma.posts.domain.Category

@Composable
fun PostDetailsScreen(
    viewModel: PostDetailsViewModel,
    onClose: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    PostDetailsScreen(
        state = state,
        onClose = onClose,
        eventPublisher = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun PostDetailsScreen(
    state: PostDetailsContract.UiState,
    onClose: () -> Unit,
    eventPublisher: (PostDetailsContract.UiEvent) -> Unit,
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.details?.post?.title ?: "Post",
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    AppBarIcon(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        onClick = onClose,
                    )
                },
                actions = {
                    AppBarIcon(
                        icon = Icons.Default.Refresh,
                        onClick = { eventPublisher(PostDetailsContract.UiEvent.Refresh) },
                    )
                },
            )
        },
        content = { paddingValues ->
            val details = state.details
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
            ) {
                if (details == null && state.isRefreshing) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (details == null && state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Error: ${state.error.message}")
                            Spacer(Modifier.size(8.dp))
                            Button(onClick = { eventPublisher(PostDetailsContract.UiEvent.Refresh) }) { Text("Retry") }
                        }
                    }
                } else if (details != null) {
                    val post = details.post
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        model = details.imageHdUrl ?: post.imageUrl,
                        contentDescription = post.title,
                        contentScale = ContentScale.Crop,
                        placeholder = rememberVectorPainter(Icons.Default.Photo),
                    )
                    Spacer(Modifier.size(12.dp))
                    Text(
                        text = post.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.size(4.dp))
                    Row {
                        Text(text = post.date.toString())
                        if (details.mediaAuthor != null) {
                            Text(text = "  ·  by ${details.mediaAuthor}")
                        }
                    }
                    Spacer(Modifier.size(12.dp))

                    if (details.description != null) {
                        Text(text = details.description)
                    } else if (state.isRefreshing) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp))
                            Spacer(Modifier.size(8.dp))
                            Text("Loading details...")
                        }
                    }

                    if (details.copyright != null) {
                        Spacer(Modifier.size(12.dp))
                        Text(
                            text = "© ${details.copyright}",
                            fontSize = 12.sp,
                        )
                    }

                    if (post.categories.isNotEmpty()) {
                        Spacer(Modifier.size(16.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            post.categories.forEach { CategoryChip(it) }
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun CategoryChip(category: Category) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            text = category.name,
            fontSize = 12.sp,
        )
    }
}
