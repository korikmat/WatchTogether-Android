package com.korikmat.watchtogether.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.usecases.GetActiveTimerUseCase
import com.korikmat.domain.usecases.StartTimerUseCase
import com.korikmat.domain.usecases.CancelTimerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class TimerUiState(
    val time: String = "",
    val duration: Long = 0L,
    val isRunning: Boolean = false
)

class TimerViewModel(
    private val startTimer: StartTimerUseCase,
    private val cancelTimer: CancelTimerUseCase,
    private val getActive: GetActiveTimerUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<TimerUiState> = MutableStateFlow(TimerUiState())
    val state: StateFlow<TimerUiState> = _state

    private var tickerJob: Job? = null

    private var duration: Long = 0L

    fun create(
        durationMs: Long
    ) {
        duration = durationMs
        _state.value = TimerUiState(
            time = formatHms(durationMs),
            duration = durationMs,
            isRunning = false
        )
    }

    fun start() = viewModelScope.launch(Dispatchers.IO) {
        if (duration == 0L) return@launch

        startTimer(duration)
        resumeTicker(
            startEpoch = System.currentTimeMillis(),
            durationMs = duration
        )
    }

    fun cancel() {
        viewModelScope.launch() {
            cancelTimer()
            stopTicker()
        }
    }

    private fun resumeTicker(startEpoch: Long, durationMs: Long) {
        stopTicker()

        tickerJob = viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                val remaining = (startEpoch + durationMs) - System.currentTimeMillis()
                if (remaining > 0) {
                    _state.value = _state.value.copy(
                        time = formatHms(remaining),
                        duration = durationMs,
                        isRunning = true
                    )
                    delay(1_000)
                } else {
                    stopTicker()
                    break
                }
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel(); tickerJob = null
        _state.value = TimerUiState(
            time = formatHms(duration),
            duration = duration,
        )
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getActive().collect { saved ->
                    val startEpochMs = saved?.startEpochMs ?: 0L
                    duration = saved?.durationMs ?: 0L

                    resumeTicker(startEpochMs, duration)
                }

        }

    }
}

fun split(ms: Long): Triple<Long, Long, Long> {
    val h = TimeUnit.MILLISECONDS.toHours(ms)
    val m = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
    val s = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
    return Triple(h, m, s)
}

fun formatHms(ms: Long): String {
    val (h, m, s) = split(ms)
    if (h == 0L)
        return "%02d:%02d".format(m, s)

    return "%02d:%02d:%02d".format(h, m, s)
}



