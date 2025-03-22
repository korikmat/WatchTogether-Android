package com.korikmat.watchtogether.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.korikmat.watchtogether.ui.navigation.BottomNavigationBar
import com.korikmat.watchtogether.ui.navigation.BottomNavigationItems
import com.korikmat.watchtogether.ui.navigation.MainDestinations
import com.korikmat.watchtogether.ui.navigation.NavigationScreens
import com.korikmat.watchtogether.ui.navigation.WatchTogetherNavController
import com.korikmat.watchtogether.ui.navigation.rememberWatchTogetherNavController
import com.korikmat.watchtogether.ui.theme.WatchTogetherTheme

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun WatchTogetherApp() {
    WatchTogetherTheme {
        val watchTogetherNavController = rememberWatchTogetherNavController()
        NavHost(
            navController = watchTogetherNavController.navController,
            startDestination = MainDestinations.HOME_ROUTE
        ) {
            composable(route = MainDestinations.HOME_ROUTE) {
                MainScreen(
                    nsestedNavController = watchTogetherNavController,
                    onMovieClicked = { watchTogetherNavController.navigateToMovieDetails(it) },
                    onEditUserProfileClicked = {
                        watchTogetherNavController.navigateToBottomBarRoute(
                            NavigationScreens.EditUserProfile.screenRoute
                        )
                    },
                    onSpecifyGenresClicked = {
                        watchTogetherNavController.navigateToBottomBarRoute(
                            NavigationScreens.SpecifyGenres.screenRoute
                        )
                    }
                )
            }
            composable(NavigationScreens.EditUserProfile.screenRoute) {
                EditUserProfileScreen(
                    onBackClick = watchTogetherNavController::upPress,
                    onSaveClick = { _, _, _ -> })
            }
            composable(NavigationScreens.SpecifyGenres.screenRoute) {
                GenreSelectionScreen(
                    onBackClick = watchTogetherNavController::upPress,
                    onSaveClick = { })
            }
        }
    }
}


@Composable
fun MainScreen(
    nsestedNavController: WatchTogetherNavController,
    onMovieClicked: () -> Unit,
    onEditUserProfileClicked: () -> Unit,
    onSpecifyGenresClicked: () -> Unit,
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
    ) { paddingValues ->
        NavHost(
            navController = nestedNavController.navController,
            startDestination = NavigationScreens.Movies.screenRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .clip(RoundedCornerShape(30.dp))
        ) {
            composable(NavigationScreens.Profile.screenRoute) {
                UserProfileScreen(
                    onEditUserProfileClicked = onEditUserProfileClicked,
                    onLogOutClicked = {},
                    onSpecifyGenresClicked = onSpecifyGenresClicked,
                )
            }
            composable(NavigationScreens.Movies.screenRoute) {
                MovieSelectionScreen()
            }
            composable(NavigationScreens.Matches.screenRoute) { from ->
                MatchesScreen()
            }

        }
    }
}
