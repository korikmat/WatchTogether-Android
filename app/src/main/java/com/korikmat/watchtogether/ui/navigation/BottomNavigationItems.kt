package com.korikmat.watchtogether.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.utils.Constants

data class BottomNavigationItem(
    @StringRes val title: Int = 0,
    val icon: ImageVector = Icons.Filled.Warning,
    val screenRoute: String = ""
)

@Composable
fun getBottomNavigationItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = R.string.profile,
            icon = Icons.Filled.Person,
            screenRoute = NavigationScreens.Profile.screenRoute
        ),
        BottomNavigationItem(
            title = R.string.movies,
            icon = ImageVector.vectorResource(R.drawable.tv_icon),
            screenRoute = NavigationScreens.Movies.screenRoute
        ),
        BottomNavigationItem(
            title = R.string.matches,
            icon = Icons.Filled.Favorite,
            screenRoute = NavigationScreens.Matches.screenRoute
        )
    )
}

sealed class NavigationScreens(var screenRoute: String) {
    data object Profile : NavigationScreens(Constants.PROFILE_ROUTES)
    data object Movies : NavigationScreens(Constants.MOVIES_ROUTES)
    data object Matches : NavigationScreens(Constants.MATCHES_ROUTES)
}
