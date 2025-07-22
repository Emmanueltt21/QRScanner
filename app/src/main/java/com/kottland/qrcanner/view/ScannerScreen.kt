package com.kottland.qrcanner.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import androidx.navigation.NavController
import com.kottland.qrcanner.viewmodel.ScannerViewModel
import com.kottland.qrcanner.R
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.LocalLifecycleOwner

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScannerScreen(navController: NavController, scannerViewModel: ScannerViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        var torchEnabled by remember { mutableStateOf(false) }
        var cameraControl by remember { mutableStateOf<androidx.camera.core.CameraControl?>(null) }
        var pickedImageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
        val pickImageLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                pickedImageBitmap = bitmap
                inputStream?.close()
                if (bitmap != null) {
                    val image = InputImage.fromBitmap(bitmap, 0)
                    val options = BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                        .build()
                    val scanner = BarcodeScanning.getClient(options)
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            if (barcodes.isNotEmpty()) {
                                val barcode = barcodes.first()
                                val rawValue = barcode.rawValue
                                if (rawValue != null) {
                                    scannerViewModel.addScanResult(rawValue)
                                    val encoded = Uri.encode(rawValue)
                                    navController.navigate("scan_result/$encoded")
                                }
                            }
                        }
                        .addOnFailureListener {
                            // Handle failure
                        }
                }
            }
        }
        if (hasCameraPermission) {
            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context)
                    val preview = Preview.Builder().build()
                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        { imageProxy ->
                            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                            val image = imageProxy.image
                            if (image != null) {
                                val processImage =
                                    InputImage.fromMediaImage(image, rotationDegrees)
                                val options = BarcodeScannerOptions.Builder()
                                    .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                                    .build()
                                val scanner = BarcodeScanning.getClient(options)
                                scanner.process(processImage)
                                    .addOnSuccessListener { barcodes ->
                                        if (barcodes.isNotEmpty()) {
                                            val barcode = barcodes.first()
                                            val rawValue = barcode.rawValue
                                            if (rawValue != null) {
                                                scannerViewModel.addScanResult(rawValue)
                                                val encoded = Uri.encode(rawValue)
                                                navController.navigate("scan_result/$encoded")
                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        // Handle failure
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            }
                        }
                    )
                    try {
                        val camera = cameraProviderFuture.get().bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageAnalysis
                        )
                        cameraControl = camera.cameraControl
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        torchEnabled = !torchEnabled
                        cameraControl?.enableTorch(torchEnabled)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = if (torchEnabled) R.drawable.baseline_flash_on_24 else R.drawable.outline_flash_off_24),
                        contentDescription = if (torchEnabled) "Flashlight On" else "Flashlight Off",
                        tint = Color.Yellow
                    )
                }
                IconButton(
                    onClick = {
                        pickImageLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_hard_drive_2_24),
                        contentDescription = "Scan from Image",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
