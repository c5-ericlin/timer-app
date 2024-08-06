package com.westmonroe.timers

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.westmonroe.timers.ui.theme.TimersTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TimerApp(
                        modifier = Modifier.padding(
                        start = innerPadding.calculateStartPadding(layoutDirection = LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(layoutDirection = LocalLayoutDirection.current),
                        top = innerPadding.calculateTopPadding(),
                    ))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun TimerRow(uiData: TimerRowUiData, modifier: Modifier = Modifier) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .padding(bottom = 5.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)) {
            Text(text = "Timer")
            Text(text = "%.3fs".format(uiData.timeElapsedMillis / 1000.0))
        }
    }
}

@Composable
fun TimerApp(modifier: Modifier = Modifier,
             timerRowViewModel: TimerRowViewModel = viewModel()) {
    val uiState by timerRowViewModel.timerRowUiData.collectAsState()
    val state = rememberLazyListState()

    val visibleItems: List<Int> by remember {
        derivedStateOf {
            state.layoutInfo.visibleItemsInfo.map { it.index }
        }
    }

    val mainHandler = Handler(Looper.getMainLooper())

    mainHandler.post(object : Runnable {
        override fun run() {
            timerRowViewModel.updateItems(visibleItems)
            mainHandler.postDelayed(this, 100)
        }
    })

    LazyColumn(modifier = modifier, state = state) {
        items(10000) {
            TimerRow(uiState.getOrElse(it) {
                TimerRowUiData(0)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TimersTheme {
        Greeting("Android")
    }
}