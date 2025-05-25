package com.korikmat.watchtogether.presentation.ui.screens

import androidx.compose.material3.TimePicker
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.ui.viewModels.TimerViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    onBackClick: () -> Unit, viewModel: TimerViewModel = koinViewModel<TimerViewModel>()
) {


    val context = LocalContext.current
    val notifLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context, "android.permission.POST_NOTIFICATIONS"
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                notifLauncher.launch("android.permission.POST_NOTIFICATIONS")
            }
        }
    }

    val ui by viewModel.state.collectAsState()
    val time = ui.time
    val duration = ui.duration
    val isRunning = ui.isRunning

    var showPicker by remember { mutableStateOf(false) }
    val pickerState = remember(duration) {
        TimePickerState(
            initialHour = TimeUnit.MILLISECONDS.toHours(duration).toInt(),
            initialMinute = (TimeUnit.MILLISECONDS.toMinutes(duration) % 60).toInt(),
            is24Hour = true
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.set_the_movie_timer)) }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 50.dp),
                text = stringResource(R.string.going_to_watch_a_movie_through),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(enabled = !isRunning) {
                        showPicker = !showPicker
                    },
                text = time,
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(Modifier.height(50.dp))

            Button(
                onClick = { if (!isRunning) viewModel.start() else viewModel.cancel() },
                contentPadding = PaddingValues(
                    horizontal = 40.dp, vertical = 10.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isRunning) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error,
                )

            ) {
                if (!isRunning) Text(
                    stringResource(R.string.start), style = MaterialTheme.typography.titleMedium
                )
                else Text(
                    stringResource(R.string.stop), style = MaterialTheme.typography.titleMedium
                )
            }

            if (showPicker) {
                AdvancedTimePickerDialog(
                    onDismiss = { showPicker = false },
                    onConfirm = {
                        showPicker = false
                        viewModel.create(pickerState.hour * 3_600_000L + pickerState.minute * 60_000L)
                    },
                ) {
                    TimePicker(
                        state = pickerState,
                    )
                }
            }
        }
    }
}

@Composable
fun AdvancedTimePickerDialog(
    title: String = stringResource(R.string.select_time),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
                    TextButton(onClick = onConfirm) { Text(stringResource(R.string.ok)) }
                }
            }
        }
    }
}
