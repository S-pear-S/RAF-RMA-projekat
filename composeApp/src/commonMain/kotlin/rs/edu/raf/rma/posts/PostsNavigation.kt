package rs.edu.raf.rma.posts

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.rma.posts.details.PostDetailsScreen
import rs.edu.raf.rma.posts.details.PostDetailsViewModel
import rs.edu.raf.rma.posts.list.PostsListScreen
import rs.edu.raf.rma.posts.list.PostsListViewModel
import rs.edu.raf.rma.posts.pager.PostsPagerScreen
import rs.edu.raf.rma.posts.pager.PostsPagerViewModel

@Composable
fun PostsNavigation(
    startDestination: String,
    onNavigateToPasswords: () -> Unit = {},
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = "welcome") {

        }

        composable(route = "login") {

        }

        composable(route = "register") {

        }

        composable(route = "posts") {
            val viewModel = koinViewModel<PostsListViewModel>()
            PostsListScreen(
                viewModel = viewModel,
                onPostClick = { navController.navigateToPostDetails(postId = it) },
                onImageTap = { navController.navigateToPostPager(postId = it) },
                onNavigateToPasswords = onNavigateToPasswords,
            )
        }

        composable(
            route = "posts/{$POST_ID}",
            arguments = listOf(
                navArgument(POST_ID) {
                    type = NavType.IntType
                    nullable = false
                },
            ),
        ) {
            val viewModel = koinViewModel<PostDetailsViewModel>()
            PostDetailsScreen(
                viewModel = viewModel,
                onClose = { navController.navigateUp() },
            )
        }

        composable(
            route = "posts/pager/{$POST_ID}",
            arguments = listOf(
                navArgument(POST_ID) {
                    type = NavType.IntType
                    nullable = false
                },
            ),
        ) {
            val viewModel = koinViewModel<PostsPagerViewModel>()
            PostsPagerScreen(
                viewModel = viewModel,
                onClose = { navController.navigateUp() },
            )
        }
    }
}

private fun NavController.navigateToPostDetails(postId: Int) {
    navigate("posts/$postId")
}

internal fun NavController.navigateToPostPager(postId: Int) {
    navigate("posts/pager/$postId")
}

const val POST_ID = "postId"
inline val SavedStateHandle.postId: Int? get() = get(POST_ID)
inline val SavedStateHandle.postIdOrThrow: Int get() = get(POST_ID)
    ?: throw IllegalStateException("$POST_ID is mandatory and can not be null")
