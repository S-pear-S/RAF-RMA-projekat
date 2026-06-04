package rs.edu.raf.rma.posts.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import rs.edu.raf.rma.passwords.core.AppBarIcon
import rs.edu.raf.rma.posts.domain.Post

@Composable
fun PostsListScreen(
    viewModel: PostsListViewModel,
    onPostClick: (postId: Int) -> Unit,
    onImageTap: (postId: Int) -> Unit,
    onNavigateToPasswords: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    PostsListScreen(
        state = state,
        onPostClick = onPostClick,
        onImageTap = onImageTap,
        eventPublisher = viewModel::setEvent,
        onNavigateToPasswords = onNavigateToPasswords,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostsListScreen(
    state: PostsListContract.UiState,
    onPostClick: (postId: Int) -> Unit = {},
    onImageTap: (postId: Int) -> Unit = {},
    eventPublisher: (PostsListContract.UiEvent) -> Unit = {},
    onNavigateToPasswords: () -> Unit = {},
) {
    val uiScope = rememberCoroutineScope()
    val postsLazyListState = rememberLazyListState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val shouldScrollToTop by remember {
        derivedStateOf { postsLazyListState.firstVisibleItemIndex > 0 }
    }

    LaunchedEffect(postsLazyListState.firstVisibleItemIndex) {
        Napier.d { "${postsLazyListState.firstVisibleItemIndex}" }
    }

    LaunchedEffect(shouldScrollToTop) {
        Napier.d { "$shouldScrollToTop" }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Menu",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                NavigationDrawerItem(
                    label = { Text("Posts") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    selected = true,
                    onClick = { uiScope.launch { drawerState.close() } },
                )
                NavigationDrawerItem(
                    label = { Text("Passwords") },
                    icon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    selected = false,
                    onClick = {
                        uiScope.launch { drawerState.close() }
                        onNavigateToPasswords()
                    },
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                PostsListTopAppBar(
                    onMenuClick = { uiScope.launch { drawerState.open() } },
                    onRefreshClick = { eventPublisher(PostsListContract.UiEvent.Refresh) },
                )
            },
            floatingActionButton = {
                if (shouldScrollToTop) {
                    FloatingActionButton(
                        onClick = {
                            uiScope.launch {
                                postsLazyListState.animateScrollToItem(index = 0)
                            }
                        }
                    ) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = "Scroll to top")
                    }
                }
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    ViewModeToggle(
                        selected = state.viewMode,
                        onSelected = { mode -> eventPublisher(PostsListContract.UiEvent.SetViewMode(mode)) },
                    )
                    when {
                        state.isRefreshing && state.posts.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        state.error != null && state.posts.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Error: ${state.error.message}")
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Button(onClick = { eventPublisher(PostsListContract.UiEvent.Refresh) }) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }

                        state.posts.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(text = "No posts. Tap + to add a new post.")
                            }
                        }

                        state.viewMode == ViewMode.List -> {
                            PostsLazyColumn(
                                posts = state.posts,
                                listState = postsLazyListState,
                                onPostClick = onPostClick,
                            )
                        }

                        state.viewMode == ViewMode.Gallery -> {
                            PostsGalleryGrid(
                                posts = state.posts,
                                onImageTap = onImageTap,
                            )
                        }
                    }
                }
            },
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun PostsListTopAppBar(
    onMenuClick: () -> Unit,
    onRefreshClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Posts") },
        navigationIcon = {
            AppBarIcon(
                icon = Icons.Default.Menu,
                onClick = onMenuClick,
            )
        },
        actions = {
            AppBarIcon(
                icon = Icons.Default.Refresh,
                onClick = onRefreshClick,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewModeToggle(
    selected: ViewMode,
    onSelected: (ViewMode) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        SegmentedButton(
            selected = selected == ViewMode.List,
            onClick = { onSelected(ViewMode.List) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            icon = { Icon(Icons.Default.List, contentDescription = null) },
            label = { Text("List") },
        )
        SegmentedButton(
            selected = selected == ViewMode.Gallery,
            onClick = { onSelected(ViewMode.Gallery) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            icon = { Icon(Icons.Default.GridView, contentDescription = null) },
            label = { Text("Gallery") },
        )
    }
}

@Composable
private fun PostsLazyColumn(
    posts: List<Post>,
    listState: LazyListState,
    onPostClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        // Vas zadatak ovde je da OBJASNITE kako lista
        // treba da se renderise, compose code ne moze ovde
        //  Text(text = "Posts") //ovo se ne kompajlira

        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(color = Color.Blue),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Posts",
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
        }

        itemsIndexed(
            items = posts,
            key = { index, post -> "${post.id}" },
            contentType = { index, post -> post.mediaType },
        ) { index, post ->
            when (post.mediaType) {
                "image" -> {
                    PostListItem(
                        post = post,
                        onClick = { onPostClick(post.id) },
                    )
                }

                "video" -> {
                    Unit
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun PostsGalleryGrid(
    posts: List<Post>,
    onImageTap: (postId: Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        columns = GridCells.Adaptive(minSize = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(items = posts, key = { it.id }) { post ->
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable { onImageTap(post.id) },
                model = post.thumbnailUrl ?: post.imageUrl,
                contentDescription = post.title,
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(Icons.Default.Photo),
                error = rememberVectorPainter(Icons.Default.Photo),
            )
        }
    }
}

@Composable
private fun PostListItem(
    post: Post,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = {
            Text(
                text = post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Column {
                Text(text = post.date.toString())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.HeartBroken,
                        contentDescription = null,
                        tint = Color.Red,
                    )
                    Text(text = "${post.likes} likes")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${post.dislikes} dislikes")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${post.commentsCount} comments")
                }
            }
        },
        leadingContent = {
            AsyncImage(
                modifier = Modifier.size(72.dp),
                model = post.imageUrl,
                contentDescription = post.title,
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(Icons.Default.Photo),
            )
        }
    )
}
