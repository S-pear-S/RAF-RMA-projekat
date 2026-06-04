package rs.edu.raf.rma.posts.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import rs.edu.raf.rma.posts.domain.Post

@Composable
fun PostsPagerScreen(
    viewModel: PostsPagerViewModel,
    onClose: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    PostsPagerScreen(state = state, onClose = onClose)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostsPagerScreen(
    state: PostsPagerContract.UiState,
    onClose: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { state.posts.size },
    )

    var initialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state.posts, state.startId) {
        if (!initialized && state.posts.isNotEmpty()) {
            val idx = state.posts.indexOfFirst { it.id == state.startId }.coerceAtLeast(0)
            pagerState.scrollToPage(idx)
            initialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.posts.isEmpty()) ""
                        else "${pagerState.currentPage + 1} / ${state.posts.size}",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            when {
                state.isLoading && state.posts.isEmpty() -> {
                    CircularProgressIndicator()
                }

                state.posts.isEmpty() -> {
                    Text(text = "No images.", color = Color.White)
                }

                else -> {
                    Gallery(pagerState, state)
                }
            }
        }
    }
}

@Composable
private fun Gallery(
    pagerState: PagerState,
    state: PostsPagerContract.UiState
) {
    BoxWithConstraints {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 4.dp,
            pageSize = PageSize.Fixed(pageSize = maxWidth - 32.dp),
            modifier = Modifier.fillMaxSize(),
            key = { page ->
                val post = state.posts[page]
                post.id
            },
        ) { page ->
            val post = state.posts[page]

            PostPage(
                modifier = Modifier.fillMaxSize(),
                post = post,
            )
        }

    }
}

@Composable
private fun PostPage(
    post: Post,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = post.imageUrl,
            contentDescription = post.title,
            contentScale = ContentScale.Fit,
            placeholder = rememberVectorPainter(Icons.Default.Photo),
            error = rememberVectorPainter(Icons.Default.Photo),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp),
        ) {
            Text(
                text = post.title,
                color = Color.White,
                maxLines = 1,
            )
        }
    }
}
