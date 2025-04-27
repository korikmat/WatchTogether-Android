package com.korikmat.watchtogether.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController

object MainDestinations {
    const val CREATE_USER_ROUTE = "create_user"
    const val SELECT_USER_ROUTE = "select_user"
    const val HOME_ROUTE = "home"
}


@Composable
fun rememberWatchTogetherNavController(navController: NavHostController = rememberNavController()): WatchTogetherNavController = remember(navController) {
        WatchTogetherNavController(navController)
    }

@Stable
class WatchTogetherNavController(
    val navController: NavHostController
) {

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != navController.currentDestination?.route) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = {
        launchSingleTop = true
        restoreState = true
    }) {
        navController.navigate(route, builder = builder)
    }
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}