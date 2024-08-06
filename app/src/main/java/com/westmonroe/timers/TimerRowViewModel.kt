package com.westmonroe.timers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TimerRowViewModel : ViewModel() {
    var timerRowUiData: MutableStateFlow<List<TimerRowUiData>> = MutableStateFlow((1..10000).map {
        TimerRowUiData(0)
    })

    fun updateItems(indices: List<Int>) {
        timerRowUiData.update {
            it.mapIndexed {
                idx, row ->
                    if (indices.contains(idx))
                        row.copy(timeElapsedMillis = row.timeElapsedMillis + 100)
                    else
                        row
            }
        }
    }
}

data class TimerRowUiData(
    val timeElapsedMillis: Int,
)
