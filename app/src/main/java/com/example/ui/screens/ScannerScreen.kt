package com.example.ui.screens

import android.Manifest
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BoundingBoxOverlay
import com.example.ui.components.CameraPreviewView
import com.example.ui.components.ObjectDetailBottomSheet
import com.example.ui.components.ScanResultDialog
import com.example.ui.components.TeachObjectDialog
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.HighConfidenceColor
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.ScannerUiState
import com.example.ui.viewmodel.ScannerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel,
    uiState: ScannerUiState,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Shutter button interaction for smooth tactile scale
    val shutterInteraction = remember { MutableInteractionSource() }
    val isShutterPressed by shutterInteraction.collectIsPressedAsState()

    // Pulse animation for live status dot
    val statusTransition = rememberInfiniteTransition(label = "statusDot")
    val dotAlpha by statusTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    // Camera permission request launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.setCameraPermission(granted)
    }

    // Photo Picker for scanning gallery images
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bitmap = BitmapFactory.decodeStream(stream)
                    if (bitmap != null) {
                        viewModel.processImportedBitmap(bitmap)
                    }
                }
            } catch (_: Exception) {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Real Camera Viewport
        CameraPreviewView(
            isTorchOn = uiState.isTorchOn,
            useFrontCamera = uiState.useFrontCamera,
            hasCameraPermission = uiState.hasCameraPermission,
            onFrameAnalyzed = { imageProxy -> viewModel.onCameraFrame(imageProxy) },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Camera Permission Request Overlay (when permission not yet granted)
        if (!uiState.hasCameraPermission) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = Color(0x1AFFFFFF),
                        shape = CircleShape,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Camera Access Required",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ObjectLens uses your real camera to detect, label, and learn objects in real-time.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Button(
                        onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Enable Camera",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // 3. Real-time Bounding Box & Label Overlay
        if (uiState.hasCameraPermission) {
            BoundingBoxOverlay(
                detections = uiState.detections,
                showBoundingBoxes = uiState.showBoundingBoxes,
                showConfidence = uiState.showConfidence,
                selectedObjectId = uiState.selectedObject?.id,
                onObjectTapped = { obj -> viewModel.selectObjectForDetails(obj) },
                modifier = Modifier.fillMaxSize()
            )
        }

        // 3. Minimalist Top Controls Bar (Frosted Glass HUD)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Torch Glass Button
                Surface(
                    color = if (uiState.isTorchOn) Color(0xCCFFFFFF) else Color(0x80121215),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.size(42.dp)
                ) {
                    IconButton(onClick = { viewModel.toggleTorch() }) {
                        Icon(
                            imageVector = if (uiState.isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            contentDescription = "Toggle Torch",
                            tint = if (uiState.isTorchOn) Color(0xFF09090B) else TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Minimalist Live Status Pill
                Surface(
                    color = Color(0x99121215),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (uiState.isDetectionPaused) TextGray
                                    else HighConfidenceColor.copy(alpha = dotAlpha)
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.isDetectionPaused) "PAUSED" else "LIVE · ${uiState.fps} FPS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Right controls: Pause & Flip Camera
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Pause/Resume Button
                    Surface(
                        color = Color(0x80121215),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.size(42.dp)
                    ) {
                        IconButton(onClick = { viewModel.togglePause() }) {
                            Icon(
                                imageVector = if (uiState.isDetectionPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = "Toggle Pause",
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Flip Camera Button
                    Surface(
                        color = Color(0x80121215),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.size(42.dp)
                    ) {
                        IconButton(onClick = { viewModel.flipCamera() }) {
                            Icon(
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "Flip Camera",
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Modern Camera Shutter & Actions Dock
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x99000000),
                            Color(0xF0000000)
                        )
                    )
                )
                .navigationBarsPadding()
                .padding(horizontal = 36.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Gallery Image Picker Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Surface(
                        color = Color(0x6618181B),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.size(50.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = "Import from Gallery",
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Gallery",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextGray
                    )
                }

                // Teach Object Quick-Action Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        val activeObj = uiState.detections.firstOrNull()
                        if (activeObj != null) {
                            viewModel.openTeachDialog(activeObj)
                        }
                    }
                ) {
                    Surface(
                        color = Color(0x28A855F7),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color(0x66A855F7)),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Teach Camera",
                                tint = Color(0xFFA855F7),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Teach",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFA855F7),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Iconic Minimalist Shutter Button
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .scale(if (isShutterPressed) 0.92f else 1.0f)
                        .clickable(
                            interactionSource = shutterInteraction,
                            indication = null
                        ) {
                            viewModel.captureScan()
                        },
                    contentAlignment = Alignment.Center
                ) {}

                // Settings Shortcut Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigateToSettings() }
                ) {
                    Surface(
                        color = Color(0x6618181B),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextGray
                    )
                }
            }
        }

        // 5. Object Details Bottom Sheet
        if (uiState.selectedObject != null) {
            ObjectDetailBottomSheet(
                objectInfo = uiState.selectedObject,
                confidencePercent = uiState.selectedConfidencePercent,
                isFavorite = uiState.isSelectedObjectFavorite,
                onToggleFavorite = { viewModel.toggleFavorite(uiState.selectedObject) },
                onSimilarObjectClick = { similarName -> viewModel.selectObjectByName(similarName) },
                onDismiss = { viewModel.dismissObjectDetails() },
                sheetState = sheetState,
                onTeachClick = {
                    uiState.selectedDetectedObject?.let { viewModel.openTeachDialog(it) }
                }
            )
        }

        // 6. On-Device Object Teaching Dialog
        if (uiState.showTeachDialog && uiState.objectToTeach != null) {
            TeachObjectDialog(
                objectToTeach = uiState.objectToTeach,
                cropBitmap = uiState.teachCropBitmap,
                onConfirm = { name, category ->
                    viewModel.teachObject(name, category)
                },
                onDismiss = { viewModel.dismissTeachDialog() }
            )
        }

        // 7. Frozen Scan Results Dialog
        if (uiState.showResultDialog) {
            ScanResultDialog(
                detections = uiState.capturedDetections,
                onSaveScan = { viewModel.saveCapturedScan() },
                onObjectClick = { item ->
                    viewModel.dismissResultDialog()
                    viewModel.selectObjectForDetails(item)
                },
                onDismiss = { viewModel.dismissResultDialog() },
                isSaved = uiState.isCapturedSaved
            )
        }
    }
}
