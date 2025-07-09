package com.sutonglabs.tracestore.ui.qrscanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun QRScannerScreen(
    navController: NavController? = null,
    onResult: (String) -> Unit = { Log.d("QRScanner", "Scanned: $it") }
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    // Request permission on launch
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Camera Preview
            CameraPreview(
                context = context,
                lifecycleOwner = lifecycleOwner,
                onQRCodeScanned = {
                    onResult(it)
                    navController?.popBackStack()
                }
            )

            // Overlay with scanning area
            QRScannerOverlay()

            // Instructions text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Position QR code within the frame",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission is required to scan QR codes.")
        }
    }
}

@Composable
fun QRScannerOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Define scanning area dimensions
        val scanAreaSize = minOf(canvasWidth, canvasHeight) * 0.7f
        val scanAreaLeft = (canvasWidth - scanAreaSize) / 2
        val scanAreaTop = (canvasHeight - scanAreaSize) / 2

        // Draw semi-transparent overlay outside the scanning area
        val overlayColor = Color.Black.copy(alpha = 0.5f)

        // Top overlay
        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, 0f),
            size = Size(canvasWidth, scanAreaTop)
        )

        // Bottom overlay
        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, scanAreaTop + scanAreaSize),
            size = Size(canvasWidth, canvasHeight - (scanAreaTop + scanAreaSize))
        )

        // Left overlay
        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, scanAreaTop),
            size = Size(scanAreaLeft, scanAreaSize)
        )

        // Right overlay
        drawRect(
            color = overlayColor,
            topLeft = Offset(scanAreaLeft + scanAreaSize, scanAreaTop),
            size = Size(canvasWidth - (scanAreaLeft + scanAreaSize), scanAreaSize)
        )

        // Draw scanning frame corners
        val cornerLength = 30.dp.toPx()
        val cornerStrokeWidth = 4.dp.toPx()
        val cornerColor = Color.White

        // Top-left corner
        drawLine(
            color = cornerColor,
            start = Offset(scanAreaLeft, scanAreaTop),
            end = Offset(scanAreaLeft + cornerLength, scanAreaTop),
            strokeWidth = cornerStrokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = cornerColor,
            start = Offset(scanAreaLeft, scanAreaTop),
            end = Offset(scanAreaLeft, scanAreaTop + cornerLength),
            strokeWidth = cornerStrokeWidth,
            cap = StrokeCap.Round
        )

        // Top-right corner
        drawLine(
            color = cornerColor,
            start = Offset(scanAreaLeft + scanAreaSize - cornerLength, scanAreaTop),
            end = Offset(scanAreaLeft + scanAreaSize, scanAreaTop),
            strokeWidth = cornerStrokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = cornerColor,
            start = Offset(scanAreaLeft + scanAreaSize, scanAreaTop),
            end = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + cornerLength),
            strokeWidth = cornerStrokeWidth,
            cap = StrokeCap.Round
        )

        // Bottom-left corner
        drawLine(
            color = cornerColor,
            start = Offset(scanAreaLeft, scanAreaTop + scanAreaSize - cornerLength),
            end = Offset(scanAreaLeft, scanAreaTop + scanAreaSize),
            strokeWidth = cornerStrokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = cornerColor,
            start = Offset(scanAreaLeft, scanAreaTop + scanAreaSize),
            end = Offset(scanAreaLeft + cornerLength, scanAreaTop + scanAreaSize),
            strokeWidth = cornerStrokeWidth,
            cap = StrokeCap.Round
        )

        // Bottom-right corner
        drawLine(
            color = cornerColor,
            start = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + scanAreaSize - cornerLength),
            end = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + scanAreaSize),
            strokeWidth = cornerStrokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = cornerColor,
            start = Offset(scanAreaLeft + scanAreaSize - cornerLength, scanAreaTop + scanAreaSize),
            end = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + scanAreaSize),
            strokeWidth = cornerStrokeWidth,
            cap = StrokeCap.Round
        )

        // Optional: Add a subtle border around the scanning area
        drawRoundRect(
            color = Color.White.copy(alpha = 0.3f),
            topLeft = Offset(scanAreaLeft, scanAreaTop),
            size = Size(scanAreaSize, scanAreaSize),
            cornerRadius = CornerRadius(8.dp.toPx()),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(
    context: Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    onQRCodeScanned: (String) -> Unit
) {
    val previewView = remember { PreviewView(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var hasScanned by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(android.util.Size(1280, 720))
            .build()

        val barcodeScanner = BarcodeScanning.getClient()

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null && !hasScanned) {
                val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                barcodeScanner.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        barcodes.firstOrNull()?.rawValue?.let { code ->
                            if (!hasScanned) {
                                hasScanned = true
                                onQRCodeScanned(code)
                            }
                        }
                    }
                    .addOnFailureListener {
                        Log.e("QRScanner", "Scan error: ${it.localizedMessage}")
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (exc: Exception) {
            Log.e("QRScanner", "Failed to bind camera use cases", exc)
        }
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
}