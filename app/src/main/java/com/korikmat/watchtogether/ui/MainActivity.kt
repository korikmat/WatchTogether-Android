package com.korikmat.watchtogether.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.korikmat.watchtogether.ui.navigation.NavigationScreens
import com.korikmat.watchtogether.ui.navigation.getBottomNavigationItems
import com.korikmat.watchtogether.ui.theme.WatchTogetherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WatchTogetherTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableIntStateOf(1) }

    val navController = rememberNavController()


    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedTab) { index, route ->
                selectedTab = index
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }

        },

    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationScreens.Movies.screenRoute,
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .clip(RoundedCornerShape(30.dp))
        ) {
            composable(NavigationScreens.Profile.screenRoute) {
                UserProfileScreen()
            }
            composable(NavigationScreens.Movies.screenRoute) {
                MovieRatingScreen()
            }
            composable(NavigationScreens.Matches.screenRoute) {
                MatchesScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int, String) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 0.dp)
            .shadow(16.dp, shape = RoundedCornerShape(30))
            .clip(RoundedCornerShape(30))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        NavigationBar(containerColor = Color.Transparent) {
            getBottomNavigationItems().forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == selectedTab,
                    label = {
                        Text(
                            text = stringResource(item.title),
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = stringResource(item.title),
                            modifier = Modifier.size(35.dp),
                        )
                    },
                    onClick = { onTabSelected(index, item.screenRoute) },
                    alwaysShowLabel = false,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}