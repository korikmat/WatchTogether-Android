package com.korikmat.watchtogether.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.ui.viewModels.UserProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserProfileScreen(
    onEditUserProfileClicked: () -> Unit,
    onSpecifyGenresClicked: () -> Unit,
    onTimerClicked: () -> Unit,
    vm: UserProfileViewModel = koinViewModel<UserProfileViewModel>(),
) {
    val state = vm.state.collectAsState().value

    var showResetDialog by remember { mutableStateOf(false) }
    Box {
        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {

            ProfileBlock(
                name = state.name,
                nickname = "@${state.nickname}",
                imageUri = state.imageUri,
                onEditUserProfileClicked = onEditUserProfileClicked
            )

            SettingsBlock {
                SettingsItem(text = stringResource(R.string.specify_favorite_genres), onClick = onSpecifyGenresClicked)
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally)
                )
                SettingsItem(text = stringResource(R.string.set_the_movie_timer), onClick = onTimerClicked)
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally)
                )
                SettingsButton(text = stringResource(R.string.reset_rated_movies), onClick = { showResetDialog = true })
            }


            Spacer(modifier = Modifier.size(50.dp))
        }

        LogOutButton(
            text = stringResource(R.string.logout),
            contentAlignment = Alignment.BottomCenter,
            onClick = {
                vm.logout()
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.reset_rated_movies_question)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_reset)) },
            confirmButton = {
                TextButton(onClick = {
                    vm.resetRatedMovies()
                    showResetDialog = false
                }) { Text(stringResource(R.string.yes)) }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text(stringResource(R.string.no)) }
            }
        )
    }
}

@Composable
fun LogOutButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.logout),
    contentAlignment: Alignment = Alignment.BottomCenter,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = contentAlignment
    ) {

        SettingsBlock(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.error)
                .then(modifier)
        ) {
            SettingsButton(text, onClick)
        }
    }
}

@Composable
fun ProfileBlock(
    name: String,
    nickname: String,
    imageUri: String?,
    onEditUserProfileClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.36f)
            .shadow(
                16.dp,
                shape = RoundedCornerShape(
                    topStart = 15.dp,
                    topEnd = 15.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .width(150.dp)
                    .height(150.dp)
                    .shadow(16.dp, shape = RoundedCornerShape(20.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri == null) {
                    Text(stringResource(R.string.photo), color = Color.DarkGray)
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUri)
                            .build(),
                        contentDescription = stringResource(R.string.user_photo),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
            Text(
                text = nickname,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier

            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 10.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onEditUserProfileClicked() }
            ) {
                Text(
                    text = stringResource(R.string.edit),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(10.dp)
                )
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit Profile",
                    modifier = Modifier
                        .size(15.dp)
                )
            }
        }
    }
}


@Composable
fun SettingsBlock(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .shadow(16.dp, shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .then(modifier),

        ) {
        content()
    }
}

@Composable
fun SettingsItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 35.dp, vertical = 15.dp)
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.padding(15.dp)
        )
    }
}

@Composable
fun SettingsButton(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 35.dp, vertical = 15.dp)
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen({}, {}, {})
}