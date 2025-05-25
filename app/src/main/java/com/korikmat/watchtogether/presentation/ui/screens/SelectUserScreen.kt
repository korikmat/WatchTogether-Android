package com.korikmat.watchtogether.presentation.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.korikmat.domain.models.UserDataModel
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.ui.viewModels.SelectUserScreenViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun SelectUserScreen(
    vm: SelectUserScreenViewModel = koinViewModel<SelectUserScreenViewModel>(),
    onCreateUser: () -> Unit,
) {
    val users by vm.users.collectAsState()
    SelectUser(
        users = users,
        onUserSelected = {
            vm.setCurrentUser(it)
        },
        onCreateUser = onCreateUser,
        onDeleteUsers = vm::deleteUsers
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectUser(
    users: List<UserDataModel>,
    onUserSelected: (UserDataModel) -> Unit,
    onCreateUser: () -> Unit,
    onDeleteUsers: (Set<Long>) -> Unit = {},
) {
    var selectionMode by rememberSaveable { mutableStateOf(false) }
    var selectedIds by rememberSaveable { mutableStateOf(setOf<Long>()) }
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            if (selectionMode) {
                CenterAlignedTopAppBar(
                    title = { Text("${selectedIds.size}") },
                    navigationIcon = {
                        IconButton(onClick = {
                            selectionMode = false; selectedIds = emptySet()
                        }) { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cancel)) }
                    },
                    actions = {
                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                )
            } else {
                CenterAlignedTopAppBar(
                    title = {
                        Text(stringResource(R.string.select_user))
                    },
                    navigationIcon = {
                    },
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(30.dp)),
                verticalArrangement = Arrangement.Center,
                contentPadding = PaddingValues(
                    bottom = 70.dp
                ),
            ) {
                items(users) { user ->
                    val isSelected = user.id in selectedIds
                    SwipeableItem(
                        onDeleteClicked = {
                            selectedIds += user.id ?: 0L
                            showDialog = true
                        },
                        selectionMode = selectionMode,
                        modifier = Modifier
                            .height(100.dp)
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                    ) {
                        UserItem(
                            name = user.name,
                            nickname = user.nickname,
                            imageUri = user.imageUri,
                            selected = isSelected && selectionMode,
                            onUserSelected = {
                                if (selectionMode) {
                                    selectedIds = if (isSelected)
                                        selectedIds - (user.id ?: 0L)
                                    else
                                        selectedIds + (user.id ?: 0L)
                                    if (selectedIds.isEmpty()) selectionMode = false
                                } else {
                                    onUserSelected(user)
                                }
                            },
                            onLongClick = {
                                if (!selectionMode) {
                                    selectionMode = true
                                    selectedIds = setOf(user.id ?: 0L)
                                }
                            }
                        )
                    }

                }
            }
            Button(
                onClick = onCreateUser,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
                    .height(50.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(stringResource(R.string.create_new_user))
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.delete_user_s)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete_user_s)) },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteUsers(selectedIds)
                    selectedIds = emptySet()
                    selectionMode = false
                    showDialog = false
                }) { Text(stringResource(R.string.yes)) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text(stringResource(R.string.no)) }
            }
        )
    }
}

@Composable
fun UserItem(
    name: String,
    nickname: String,
    imageUri: String?,
    selected: Boolean = false,
    onUserSelected: () -> Unit,
    onLongClick: () -> Unit = {},
) {
    val bg by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surfaceContainer
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onUserSelected, onLongClick = onLongClick),
        colors = CardDefaults.cardColors(containerColor = bg),
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .width(90.dp)
                    .height(90.dp)
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "@${nickname}",
                    style = MaterialTheme.typography.bodyMedium
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
fun SelectUserPrewiew() {
    SelectUser(
        users = listOf(
            UserDataModel(
                name = "John Doe",
                nickname = "johndoe"
            ),
            UserDataModel(
                name = "John Doe",
                nickname = "johndoe"
            ),
            UserDataModel(
                name = "John Doe",
                nickname = "johndoe"
            ),
            UserDataModel(
                name = "John Doe",
                nickname = "johndoe"
            ),
            UserDataModel(
                name = "John Doe",
                nickname = "johndoe"
            ),
            UserDataModel(
                name = "John Doe",
                nickname = "johndoe"
            ),
            UserDataModel(
                name = "John Doe",
                nickname = "johndoe"
            ),
            UserDataModel(
                name = "John Doe",
                nickname = "johndoe"
            ),
            UserDataModel(
                name = "Jane Smith",
                nickname = "janesmith"
            )
        ),
        {},
        {},
    )
}
