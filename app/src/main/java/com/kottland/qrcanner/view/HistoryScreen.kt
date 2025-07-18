package com.kottland.qrcanner.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kottland.qrcanner.R
import com.kottland.qrcanner.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel = viewModel()) {
    val history by historyViewModel.history.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = { historyViewModel.clearHistory() }) {
            Text(stringResource(id = R.string.clear_history))
        }
        LazyColumn {
            items(history) { scanResult ->
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = Date(scanResult.timestamp)
                Text("Content: ${scanResult.content}, Scanned on: ${sdf.format(date)}")
            }
        }
    }
}
