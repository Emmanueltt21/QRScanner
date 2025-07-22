package com.kottland.qrcanner.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kottland.qrcanner.R

private data class GridItem(val label: String, val iconRes: Int, val route: String)

@Composable
fun HomeScreen(navController: NavController) {
    val items = listOf(
        GridItem("Scan QR", R.drawable.outline_qr_code_scanner_24, "scanner"),
        GridItem("Batch Scan", R.drawable.baseline_document_scanner_24, "batch_scan"),
        GridItem("Generate QR", R.drawable.outline_qr_code_24, "generator"),
        GridItem("History", R.drawable.outline_history_24, "history"),
        GridItem("Settings", R.drawable.baseline_settings_24, "settings"),
        GridItem("About Us", R.drawable.outline_help_24, "about_us")
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF232526), Color(0xFF414345))
                )
            )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .clickable { navController.navigate(item.route) }
                        .padding(8.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.label,
                            modifier = Modifier.padding(bottom = 8.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text(text = item.label, style = MaterialTheme.typography.bodyLarge, color = Color.White)
                    }
                }
            }
        }
    }
}