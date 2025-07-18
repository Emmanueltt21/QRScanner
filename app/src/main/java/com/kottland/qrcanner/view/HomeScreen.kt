package com.kottland.qrcanner.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kottland.qrcanner.R

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(onClick = { navController.navigate("scanner") }) {
                    Icon(Icons.Filled.Star, contentDescription = stringResource(R.string.scan_qr))
                }
                FloatingActionButton(onClick = { navController.navigate("generator") }) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.generate_qr))
                }
                FloatingActionButton(onClick = { navController.navigate("batch_scan") }) {
                    Icon(Icons.Filled.List, contentDescription = stringResource(R.string.batch_scan))
                }
                FloatingActionButton(onClick = { navController.navigate("history") }) {
                    Icon(Icons.Filled.DateRange, contentDescription = stringResource(R.string.history))
                }
                FloatingActionButton(onClick = { navController.navigate("settings") }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.home_screen))
        }
    }
}
