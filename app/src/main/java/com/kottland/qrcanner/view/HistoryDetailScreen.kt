package com.kottland.qrcanner.view

import android.graphics.Bitmap
import android.graphics.Color
import android.content.Intent
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.kottland.qrcanner.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(id: Int, navController: NavController, historyViewModel: HistoryViewModel = viewModel()) {
    val context = LocalContext.current
    val history by historyViewModel.history.collectAsState(initial = emptyList())
    val scanResult = history.find { it.id == id }
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = scanResult?.timestamp?.let { sdf.format(Date(it)) } ?: ""
    val qrBitmap = remember(scanResult?.content) {
        scanResult?.content?.let { content ->
            try {
                val writer = QRCodeWriter()
                val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
                val bmp = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
                for (x in 0 until 512) {
                    for (y in 0 until 512) {
                        bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                bmp
            } catch (e: Exception) {
                null
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("QR Details") })
        }
    ) { padding ->
        if (scanResult == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("QR code not found", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Go Back")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                qrBitmap?.let {
                    Image(bitmap = it.asImageBitmap(), contentDescription = "QR Code", modifier = Modifier.size(200.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Raw Text:", style = MaterialTheme.typography.labelLarge)
                Text(scanResult.content, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Date Scanned: $date", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    qrBitmap?.let { bmp ->
                        val uri = MediaStore.Images.Media.insertImage(context.contentResolver, bmp, "QRcanner", null)
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, android.net.Uri.parse(uri))
                            type = "image/png"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
                    }
                }) {
                    Text("Share QR Code")
                }
            }
        }
    }
}