package rs.edu.raf.rma.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import rs.edu.raf.rma.favorites.FavoritesScreen
import rs.edu.raf.rma.movies.detail.MovieDetailScreen
import rs.edu.raf.rma.movies.list.MoviesListScreen
import rs.edu.raf.rma.profile.ProfileScreen
import rs.edu.raf.rma.quiz.QuizResultScreen
import rs.edu.raf.rma.quiz.QuizScreen
import rs.edu.raf.rma.watchlist.WatchlistScreen

private sealed class Tab(val route: String, val label: String, val icon: ImageVector) {
    data object Movies : Tab("movies", "Filmovi", Icons.Default.Home)
    data object Favorites : Tab("favorites", "Favoriti", Icons.Default.Favorite)
    data object Watchlist : Tab("watchlist", "Watchlist", Icons.Default.Bookmark)
    data object Quiz : Tab("quiz_tab", "Kviz", Icons.Default.Quiz)
    data object Profile : Tab("profile", "Profil", Icons.Default.Person)
}

private val TABS = listOf(Tab.Movies, Tab.Favorites, Tab.Watchlist, Tab.Quiz, Tab.Profile)

@Composable
fun MainNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = TABS.any { tab ->
        currentDestination?.hierarchy?.any { it.route == tab.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    TABS.forEach { tab ->
                        val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Tab.Movies.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Tab.Movies.route) {
                MoviesListScreen(onMovieClick = { navController.navigate("movie/$it") })
            }

            composable("movie/{movieId}") { back ->
                val movieId = back.arguments?.getString("movieId") ?: return@composable
                MovieDetailScreen(movieId = movieId, onBack = { navController.popBackStack() })
            }

            composable(Tab.Favorites.route) {
                FavoritesScreen(onMovieClick = { navController.navigate("movie/$it") })
            }

            composable(Tab.Watchlist.route) {
                WatchlistScreen(onMovieClick = { navController.navigate("movie/$it") })
            }

            composable(Tab.Quiz.route) {
                QuizLandingScreen(onStartQuiz = { navController.navigate("quiz_session") })
            }

            composable("quiz_session") {
                QuizScreen(
                    onResult = { score, correct, total, timeUsed ->
                        navController.navigate("quiz_result/$score/$correct/$total/$timeUsed") {
                            popUpTo(Tab.Quiz.route)
                        }
                    },
                    onBack = { navController.popBackStack() },
                    onGoToMovies = {
                        navController.popBackStack(Tab.Quiz.route, inclusive = false)
                        navController.navigate(Tab.Movies.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }

            composable("quiz_result/{score}/{correct}/{total}/{timeUsed}") { back ->
                val score = back.arguments?.getString("score")?.toDoubleOrNull() ?: 0.0
                val correct = back.arguments?.getString("correct")?.toIntOrNull() ?: 0
                val total = back.arguments?.getString("total")?.toIntOrNull() ?: 10
                val timeUsed = back.arguments?.getString("timeUsed")?.toIntOrNull() ?: 0
                QuizResultScreen(
                    score = score,
                    correctAnswers = correct,
                    totalQuestions = total,
                    timeUsedSeconds = timeUsed,
                    onPlayAgain = {
                        navController.navigate("quiz_session") { popUpTo(Tab.Quiz.route) }
                    },
                    onBack = {
                        navController.navigate(Tab.Quiz.route) {
                            popUpTo(Tab.Quiz.route) { inclusive = true }
                        }
                    },
                )
            }

            composable(Tab.Profile.route) {
                ProfileScreen(onLogout = onLogout)
            }
        }
    }
}

@Composable
private fun QuizLandingScreen(onStartQuiz: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp),
        ) {
            Text("Kviz o filmovima", style = MaterialTheme.typography.headlineMedium)
            Text(
                "10 pitanja · 60 sekundi",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = onStartQuiz, modifier = Modifier.fillMaxWidth()) {
                Text("Započni kviz")
            }
        }
    }
}
