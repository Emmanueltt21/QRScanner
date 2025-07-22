package com.kottland.qrcanner.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kottland.qrcanner.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, historyViewModel: HistoryViewModel = viewModel()) {
    val history by historyViewModel.history.collectAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<Int?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Scan History") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(history) { scanResult ->
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val date = Date(scanResult.timestamp)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("history_detail/${scanResult.id}") },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AccountBox,
                                contentDescription = "QR Icon",
                                tint = Color(0xFF1976D2),
                                modifier = Modifier.size(40.dp)
                            )
                            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                                Text("QR code - ${scanResult.id}", color = Color.Black)
                                Text(sdf.format(date), color = Color.Gray)
                            }
                            IconButton(onClick = {
                                itemToDelete = scanResult.id
                                showDeleteDialog = true
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    }
                }
            }
            if (showDeleteDialog && itemToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Delete QR History") },
                    text = { Text("Are you sure you want to delete this scan history item?") },
                    confirmButton = {
                        Button(onClick = {
                            historyViewModel.deleteById(itemToDelete!!)
                            showDeleteDialog = false
                        }) { Text("Delete") }
                    },
                    dismissButton = {
                        Button(onClick = { showDeleteDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}
