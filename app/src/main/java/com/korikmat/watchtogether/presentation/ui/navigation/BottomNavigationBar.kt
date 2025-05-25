package com.korikmat.watchtogether.presentation.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.utils.Constants

enum class BottomNavigationItems(
    @StringRes val title: Int = 0,
    val icon: @Composable () -> ImageVector = { Icons.Filled.Warning },
    val screenRoute: String = ""
) {
    PROFILE(
        R.string.profile,
        { Icons.Filled.Person },
        NavigationScreens.Profile.screenRoute
    ),
    MOVIES(
        R.string.movies,
        { ImageVector.vectorResource(R.drawable.tv_icon) },
        NavigationScreens.Movies.screenRoute
    ),
    MATCHES(
        R.string.matches,
        { Icons.Filled.Favorite },
        NavigationScreens.Matches.screenRoute
    ),
}

@Composable
fun BottomNavigationBar(
    tabs: Array<BottomNavigationItems>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 0.dp)
            .shadow(16.dp, shape = RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
            tabs.forEach { item ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    selected = item.screenRoute == currentRoute,
                    label = {
                        Text(
                            text = stringResource(item.title),
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    icon = {
                        Icon(
                            item.icon(),
                            contentDescription = stringResource(item.title),
                            modifier = Modifier.size(35.dp),
                        )
                    },
                    onClick = { navigateToRoute(item.screenRoute) },
                    alwaysShowLabel = false,
                )
            }
        }
    }
}

sealed class NavigationScreens(var screenRoute: String) {
    data object SpecifyGenres : NavigationScreens(Constants.SPECIFY_GENRES_ROUTES)
    data object Profile : NavigationScreens(Constants.PROFILE_ROUTES)
    data object Movies : NavigationScreens(Constants.MOVIES_ROUTES)
    data object Matches : NavigationScreens(Constants.MATCHES_ROUTES)
    data object EditUserProfile : NavigationScreens(Constants.EDIT_USER_PROFILE_ROUTES)
    data object FavoriteMovies : NavigationScreens(Constants.FAVORITE_MOVIES_ROUTES)
    data object DislikedMovies : NavigationScreens(Constants.DISLIKED_MOVIES_ROUTES)
    data object Timer : NavigationScreens(Constants.TIMER_ROUTES)
}
