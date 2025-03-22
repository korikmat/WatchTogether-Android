package com.korikmat.watchtogether.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.korikmat.watchtogether.R

@Composable
fun UserProfileScreen(
    onEditUserProfileClicked: () -> Unit,
    onLogOutClicked: () -> Unit,
    onSpecifyGenresClicked: () -> Unit
) {

    Box {
        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {

            ProfileBlock(onEditUserProfileClicked = onEditUserProfileClicked)

            SettingsBlock {
                SettingsItem(text = "Specify favorite genres", onClick = onSpecifyGenresClicked)
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
            }
            SettingsBlock {
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
            }
            SettingsBlock {
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
                SettingsItem("Specify favorite genres", onSpecifyGenresClicked)
            }

            Spacer(modifier = Modifier.size(50.dp))
        }

        LogOutButton(
            text = "Logout",
            onClick = onLogOutClicked
        )
    }
}

@Composable
fun LogOutButton(text: String = "Log out", modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        SettingsBlock(
            modifier = Modifier
                .background(Color(0xFF990303))
                .then(modifier)
        ) {
            SettingsItem(text, onClick)
        }
    }
}

@Composable
fun ProfileBlock(onEditUserProfileClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.36f)
            .shadow(
                16.dp,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "User avatar",
                modifier = Modifier
                    .padding(top = 20.dp)

                    .size(150.dp)

                    .shadow(16.dp, shape = RoundedCornerShape(20.dp))
//                        .clip(RoundedCornerShape(20.dp))
//                        .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Killua",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
            Text(
                text = "@killua",
                style = MaterialTheme.typography.titleSmall,
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
                    text = "Edit",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(10.dp)
                )
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit info",
                    modifier = Modifier
//                            .padding(10.dp)
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
//            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surface)
            .then(modifier),

        ) {
        content()
//        HorizontalDivider(
//            modifier = Modifier
//                .fillMaxWidth(0.8f)
//                .align(Alignment.CenterHorizontally),
//        )
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
            modifier = Modifier.padding(horizontal = 35.dp, vertical = 15.dp)
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go to settings",
            modifier = Modifier.padding(15.dp)
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