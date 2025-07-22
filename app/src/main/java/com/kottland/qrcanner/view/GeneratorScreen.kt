package com.kottland.qrcanner.view

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.kottland.qrcanner.R
import com.kottland.qrcanner.viewmodel.GeneratorViewModel
import java.io.OutputStream
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun GeneratorScreen(generatorViewModel: GeneratorViewModel = viewModel()) {
    var selectedTemplate by remember { mutableStateOf("Default") }
    val templates = listOf("Default", "Business Card", "Wi-Fi", "Event", "Contact")
    var expanded by remember { mutableStateOf(false) }
    var qrColor by remember { mutableStateOf(ComposeColor.Black) }
    var bgColor by remember { mutableStateOf(ComposeColor.White) }
    var logoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showColorPicker by remember { mutableStateOf(false) }
    var codeType by remember { mutableStateOf("QR Code") }
    val codeTypes = listOf("QR Code", "Barcode")
    val context = LocalContext.current
    val logoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            logoBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
        }
    }
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("QR Templates", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = selectedTemplate,
                    onValueChange = {},
                    label = { Text("Template") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    enabled = false
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    templates.forEach { template ->
                        DropdownMenuItem(text = { Text(template) }, onClick = {
                            selectedTemplate = template
                            expanded = false
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = codeType,
                    onValueChange = {},
                    label = { Text("Type") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showColorPicker = false; expanded = false },
                    enabled = false
                )
                DropdownMenu(expanded = false, onDismissRequest = {}) {
                    // Placeholder for type dropdown
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(stringResource(id = R.string.enter_text_to_generate)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("QR Color")
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(qrColor)
                        .clickable { showColorPicker = true }
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("BG Color")
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(bgColor)
                        .clickable { showColorPicker = true }
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Logo")
                IconButton(onClick = { logoPickerLauncher.launch("image/*") }) {
                    if (logoBitmap != null) {
                        Image(bitmap = logoBitmap!!.asImageBitmap(), contentDescription = "Logo", modifier = Modifier.size(32.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                    } else {
                        Icon(painter = painterResource(id = R.drawable.ic_image), contentDescription = "Pick Logo")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    val writer = QRCodeWriter()
                    try {
                        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
                        val width = bitMatrix.width
                        val height = bitMatrix.height
                        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        for (x in 0 until width) {
                            for (y in 0 until height) {
                                bmp.setPixel(x, y, if (bitMatrix[x, y]) qrColor.toArgb() else bgColor.toArgb())
                            }
                        }
                        // Overlay logo if present
                        logoBitmap?.let { logo ->
                            val canvas = android.graphics.Canvas(bmp)
                            val scaledLogo = Bitmap.createScaledBitmap(logo, width / 4, height / 4, true)
                            val left = (width - scaledLogo.width) / 2
                            val top = (height - scaledLogo.height) / 2
                            canvas.drawBitmap(scaledLogo, left.toFloat(), top.toFloat(), null)
                        }
                        qrCodeBitmap = bmp
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        ) {
            Text(stringResource(id = R.string.generate_qr_code))
        }
        Spacer(modifier = Modifier.height(8.dp))
        qrCodeBitmap?.let { bmp ->
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "Generated QR Code",
                modifier = Modifier.size(256.dp)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    // Save to gallery
                    val filename = "QRcanner_${System.currentTimeMillis()}.png"
                    val fos: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val resolver = context.contentResolver
                        val contentValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/QRcanner")
                        }
                        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                        resolver.openOutputStream(imageUri!!)
                    } else {
                        val imagesDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES)
                        val image = java.io.File(imagesDir, filename)
                        java.io.FileOutputStream(image)
                    }
                    fos?.use {
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, it)
                        Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Save to Gallery")
                }
                Button(onClick = {
                    // Share to other apps
                    val uri = android.net.Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, bmp, "QRcanner", null))
                    val shareIntent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_STREAM, uri)
                        type = "image/png"
                    }
                    context.startActivity(android.content.Intent.createChooser(shareIntent, "Share QR Code"))
                }) {
                    Text("Share")
                }
            }
        }
    }
}
