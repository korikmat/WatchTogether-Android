package com.korikmat.watchtogether.presentation.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.korikmat.watchtogether.presentation.ui.navigation.BottomNavigationBar
import com.korikmat.watchtogether.presentation.ui.navigation.BottomNavigationItems
import com.korikmat.watchtogether.presentation.ui.navigation.MainDestinations
import com.korikmat.watchtogether.presentation.ui.navigation.NavigationScreens
import com.korikmat.watchtogether.presentation.ui.navigation.rememberWatchTogetherNavController
import com.korikmat.watchtogether.presentation.ui.screens.CreateUserScreen
import com.korikmat.watchtogether.presentation.ui.screens.DislikedMoviesScreen
import com.korikmat.watchtogether.presentation.ui.screens.EditUserProfileScreen
import com.korikmat.watchtogether.presentation.ui.screens.FavoriteMoviesScreen
import com.korikmat.watchtogether.presentation.ui.screens.GenreSelectionScreen
import com.korikmat.watchtogether.presentation.ui.screens.MatchesScreen
import com.korikmat.watchtogether.presentation.ui.screens.MovieSelectionScreen
import com.korikmat.watchtogether.presentation.ui.screens.SelectUserScreen
import com.korikmat.watchtogether.presentation.ui.screens.UserProfileScreen
import com.korikmat.watchtogether.presentation.ui.theme.WatchTogetherTheme
import com.korikmat.watchtogether.presentation.ui.viewModels.WatchTogetherAppViewModel
import org.koin.androidx.compose.koinViewModel

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun WatchTogetherApp(vm: WatchTogetherAppViewModel = koinViewModel<WatchTogetherAppViewModel>()) {

    WatchTogetherTheme {
        val watchTogetherNavController = rememberWatchTogetherNavController()

        val userState = vm.state.collectAsState().value
        val user = userState.currentUser
        val allUsers = userState.allUsers
        val startRoute = when {
            user != null -> MainDestinations.HOME_ROUTE
            allUsers.isEmpty() -> MainDestinations.CREATE_USER_ROUTE
            else -> MainDestinations.SELECT_USER_ROUTE
        }
        NavHost(
            navController = watchTogetherNavController.navController,
            startDestination = startRoute
        ) {
            composable(route = MainDestinations.CREATE_USER_ROUTE) {
                CreateUserScreen()
            }
            composable(route = MainDestinations.SELECT_USER_ROUTE) {
                SelectUserScreen(
                    onCreateUser = {
                        watchTogetherNavController.navigate(
                            MainDestinations.CREATE_USER_ROUTE
                        )
                    }
                )
            }
            composable(route = MainDestinations.HOME_ROUTE) {
                MainScreen(
                    onEditUserProfileClicked = {
                        watchTogetherNavController.navigate(
                            NavigationScreens.EditUserProfile.screenRoute
                        )
                    },
                    onSpecifyGenresClicked = {
                        watchTogetherNavController.navigate(
                            NavigationScreens.SpecifyGenres.screenRoute
                        )
                    },
                    onFavoriteMoviesClicked = {
                        watchTogetherNavController.navigate(
                            NavigationScreens.FavoriteMovies.screenRoute
                        )
                    },
                    onDislikedMoviesClicked = {
                        watchTogetherNavController.navigate(
                            NavigationScreens.DislikedMovies.screenRoute
                        )
                    },

                )
            }
            composable(NavigationScreens.EditUserProfile.screenRoute) {
                EditUserProfileScreen(
                    onBackClick = watchTogetherNavController::upPress,
                )
            }
            composable(NavigationScreens.SpecifyGenres.screenRoute) {
                GenreSelectionScreen(
                    onBackClick = watchTogetherNavController::upPress,
                    onSaveClick = { })
            }
            composable(NavigationScreens.FavoriteMovies.screenRoute) {
                FavoriteMoviesScreen(
                    onBackClick = watchTogetherNavController::upPress,
                )
            }
            composable(NavigationScreens.DislikedMovies.screenRoute) {
                DislikedMoviesScreen(
                    onBackClick = watchTogetherNavController::upPress,
                )
            }
        }
    }
}


@Composable
fun MainScreen(
    onEditUserProfileClicked: () -> Unit,
    onSpecifyGenresClicked: () -> Unit,
    onFavoriteMoviesClicked: () -> Unit,
    onDislikedMoviesClicked: () -> Unit,
) {

    val nestedNavController = rememberWatchTogetherNavController()
    val navBackStackEntry by nestedNavController.navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                tabs = BottomNavigationItems.entries.toTypedArray(),
                currentRoute = currentRoute ?: NavigationScreens.Movies.screenRoute,
                navigateToRoute = nestedNavController::navigateToBottomBarRoute,
            )
        },
    ) { inner ->
        NavHost(
            nestedNavController.navController,
            startDestination = NavigationScreens.Movies.screenRoute,
            modifier = Modifier.padding(inner),
            enterTransition = {
                slideInHorizontally { w -> slideDirection(initialState, targetState) * w } +
                        fadeIn()
            },
            exitTransition = {
                slideOutHorizontally { w -> slideDirection(initialState, targetState) * -w } +
                        fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally { w -> slideDirection(initialState, targetState) * w } +
                        fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally { w -> slideDirection(initialState, targetState) * -w } +
                        fadeOut()
            }
        ) {
            composable(NavigationScreens.Profile.screenRoute) {
                UserProfileScreen(
                    onEditUserProfileClicked = onEditUserProfileClicked,
                    onSpecifyGenresClicked = onSpecifyGenresClicked,
                )
            }
            composable(NavigationScreens.Movies.screenRoute) {
                MovieSelectionScreen()
            }
            composable(NavigationScreens.Matches.screenRoute) { from ->
                MatchesScreen(
                    onFavoritesClick = onFavoriteMoviesClicked,
                    onDislikedClick = onDislikedMoviesClicked
                )
            }
        }
    }
}

private fun slideDirection(
    from: NavBackStackEntry,
    to: NavBackStackEntry
) = if (
    BottomNavigationItems.entries.indexOfFirst { it.screenRoute == to.destination.route } >
    BottomNavigationItems.entries.indexOfFirst { it.screenRoute == from.destination.route }
) 1 else -1

