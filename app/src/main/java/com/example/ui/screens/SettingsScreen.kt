package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraFront
import androidx.compose.material.icons.filled.CameraRear
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.HighConfidenceColor
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.SettingsUiState
import com.example.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    uiState: SettingsUiState,
    onSettingsChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showResetDefaultsDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var importText by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 40.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            letterSpacing = (-0.5).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Configure machine vision, camera heuristics, and feedback",
            style = MaterialTheme.typography.bodySmall,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 1. Camera & Machine Vision Performance Section
        MinimalSectionHeader(icon = Icons.Default.Speed, title = "Camera Frame Rate & Performance")

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Target Frame Rate / Inference Frequency
                Text(
                    text = "Target Frame Rate",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Choose vision processing frequency for optimal fluidity vs battery life",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val fpsOptions = listOf(
                        Triple("Low", "15 FPS", "Eco Saver"),
                        Triple("Medium", "30 FPS", "Smooth"),
                        Triple("High", "60 FPS", "Ultra Smooth")
                    )
                    fpsOptions.forEach { (mode, fpsLabel, subtitle) ->
                        val isSelected = uiState.detectionFrequency.equals(mode, ignoreCase = true)
                        Surface(
                            color = if (isSelected) Color.White else Color(0x14FFFFFF),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, if (isSelected) Color.White else BorderSubtle),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    viewModel.setDetectionFrequency(mode)
                                    onSettingsChanged()
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = fpsLabel,
                                    color = if (isSelected) Color(0xFF09090B) else TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = subtitle,
                                    color = if (isSelected) Color(0xFF52525B) else TextGray,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Default Camera Lens Selector
                Text(
                    text = "Default Camera Lens",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Choose which camera lens opens automatically",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val cameraOptions = listOf(
                        Triple("Back", "Back Lens", Icons.Default.CameraRear),
                        Triple("Front", "Front / Selfie", Icons.Default.CameraFront)
                    )
                    cameraOptions.forEach { (cam, label, icon) ->
                        val isSelected = uiState.defaultCamera.equals(cam, ignoreCase = true)
                        Surface(
                            color = if (isSelected) Color.White else Color(0x14FFFFFF),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, if (isSelected) Color.White else BorderSubtle),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    viewModel.setDefaultCamera(cam)
                                    onSettingsChanged()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color(0xFF09090B) else TextWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = label,
                                    color = if (isSelected) Color(0xFF09090B) else TextWhite,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Detection & Vision Model Section
        MinimalSectionHeader(icon = Icons.Default.Tune, title = "Detection & Vision Heuristics")

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Confidence Threshold Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Confidence Threshold",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                    Text(
                        text = "${(uiState.confidenceThreshold * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Slider(
                    value = uiState.confidenceThreshold,
                    onValueChange = {
                        viewModel.setConfidenceThreshold(it)
                        onSettingsChanged()
                    },
                    valueRange = 0.20f..0.90f,
                    steps = 13,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color(0x1FFFFFFF)
                    )
                )

                // Quick Preset Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        0.40f to "40% Relaxed",
                        0.60f to "60% Balanced",
                        0.80f to "80% Strict"
                    ).forEach { (preset, label) ->
                        val isCurrent = kotlin.math.abs(uiState.confidenceThreshold - preset) < 0.05f
                        Surface(
                            color = if (isCurrent) Color(0x28FFFFFF) else Color(0x10FFFFFF),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isCurrent) Color.White.copy(alpha = 0.6f) else BorderSubtle),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    viewModel.setConfidenceThreshold(preset)
                                    onSettingsChanged()
                                }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isCurrent) Color.White else TextGray,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp,
                                    fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Bounding Boxes Switch
                MinimalSettingToggleRow(
                    title = "Bounding Box Viewport",
                    description = "Draw bounding boxes over detected physical objects",
                    checked = uiState.showBoundingBoxes,
                    onCheckedChange = {
                        viewModel.setShowBoundingBoxes(it)
                        onSettingsChanged()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Confidence Display Switch
                MinimalSettingToggleRow(
                    title = "Confidence Indicators",
                    description = "Display classification confidence match score on labels",
                    checked = uiState.showConfidence,
                    onCheckedChange = {
                        viewModel.setShowConfidence(it)
                        onSettingsChanged()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Feedback & Alerts Section
        MinimalSectionHeader(icon = Icons.AutoMirrored.Filled.VolumeUp, title = "Haptics & Audio")

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                MinimalSettingToggleRow(
                    title = "Text-to-Speech Announcements",
                    description = "Audibly announce detected objects via on-device speech engine",
                    checked = uiState.soundFeedback,
                    onCheckedChange = {
                        viewModel.setSoundFeedback(it)
                        onSettingsChanged()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                MinimalSettingToggleRow(
                    title = "Tactile Haptics",
                    description = "Provide subtle vibration pulse when an item is recognized",
                    checked = uiState.hapticFeedback,
                    onCheckedChange = {
                        viewModel.setHapticFeedback(it)
                        onSettingsChanged()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Cloud & Global Shared Learning Section
        MinimalSectionHeader(icon = Icons.Default.CloudSync, title = "Cloud & Shared Learning")

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0x1EA855F7),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFA855F7),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Shared Visual Knowledge",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                        Text(
                            text = "${uiState.learnedObjectCount} Learned Object(s) Active in Memory",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA855F7),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Each learned object is a ~250-byte visual vector. Syncing allows objects taught on one device to be instantly recognized across all users globally.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray,
                    lineHeight = 18.sp
                )

                // Sync status notification if present
                if (uiState.cloudSyncMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0x1E10B981),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0x4D10B981)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uiState.cloudSyncMessage,
                                    color = Color(0xFF10B981),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "Dismiss",
                                color = TextGray,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { viewModel.dismissCloudSyncMessage() }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary Sync with Cloud Button
                Button(
                    onClick = { viewModel.syncWithCloud() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isCloudSyncing
                ) {
                    if (uiState.isCloudSyncing) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Syncing with Cloud...", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    } else {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sync with Cloud", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Export and Import JSON action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.exportLearnedObjects { json ->
                                clipboardManager.setText(AnnotatedString(json))
                                showExportDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x14FFFFFF),
                            contentColor = TextWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export JSON", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            importText = ""
                            showImportDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x14FFFFFF),
                            contentColor = TextWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Import JSON", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Data & Privacy Section
        MinimalSectionHeader(icon = Icons.Default.Lock, title = "Data & Privacy")

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(HighConfidenceColor, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "100% On-Device Neural Processing",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Vision models run entirely locally. Camera frames never leave your device or get transmitted to any cloud servers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Clear History Button with visual feedback
                if (uiState.isHistoryCleared) {
                    Surface(
                        color = Color(0x1E10B981),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0x4D10B981)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Scan history cleared successfully",
                                    color = Color(0xFF10B981),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "Dismiss",
                                color = TextGray,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { viewModel.dismissHistoryClearedNotification() }
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = { showClearHistoryDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x1AEF4444),
                            contentColor = Color(0xFFEF4444)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0x3DEF4444)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear All Scan History", fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Reset Settings to Defaults Button
                Button(
                    onClick = { showResetDefaultsDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x14FFFFFF),
                        contentColor = TextWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reset Settings to Defaults", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Model Architecture & About
        MinimalSectionHeader(icon = Icons.Default.Memory, title = "System Information")

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                InfoRow(label = "Vision Model", value = "ML Kit Object Detection & Labeling")
                InfoRow(
                    label = "Target Frame Rate",
                    value = when (uiState.detectionFrequency) {
                        "Low" -> "15 FPS (Eco)"
                        "Medium" -> "30 FPS (Smooth)"
                        else -> "60 FPS (Ultra Smooth)"
                    }
                )
                InfoRow(label = "Classes", value = "400+ Real-World Categories")
                InfoRow(label = "Inference Pipeline", value = "Hardware Accelerated SurfaceView")
                InfoRow(label = "App Version", value = "1.0.0 (Release)")
            }
        }

        // Clear History Confirm Dialog
        if (showClearHistoryDialog) {
            AlertDialog(
                onDismissRequest = { showClearHistoryDialog = false },
                title = { Text("Clear All History?", color = TextWhite, fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to permanently clear all scan records?", color = TextGray) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.clearAllHistory()
                        showClearHistoryDialog = false
                    }) {
                        Text("Clear All", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearHistoryDialog = false }) {
                        Text("Cancel", color = TextWhite)
                    }
                },
                containerColor = DarkSurface,
                shape = RoundedCornerShape(18.dp)
            )
        }

        // Reset to Defaults Confirm Dialog
        if (showResetDefaultsDialog) {
            AlertDialog(
                onDismissRequest = { showResetDefaultsDialog = false },
                title = { Text("Reset to Defaults?", color = TextWhite, fontWeight = FontWeight.Bold) },
                text = { Text("This will restore all vision thresholds, 60 FPS frame rate, and sensor feedback to default factory values.", color = TextGray) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.resetToDefaults()
                        onSettingsChanged()
                        showResetDefaultsDialog = false
                    }) {
                        Text("Reset All", color = HighConfidenceColor, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDefaultsDialog = false }) {
                        Text("Cancel", color = TextWhite)
                    }
                },
                containerColor = DarkSurface,
                shape = RoundedCornerShape(18.dp)
            )
        }

        // Export Learned Data Dialog
        if (showExportDialog) {
            AlertDialog(
                onDismissRequest = { showExportDialog = false },
                title = { Text("Exported to Clipboard", color = TextWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        text = "Your learned objects have been exported to JSON and copied to your clipboard. You can share this with any other user or device to instantly transfer your camera's visual memory!",
                        color = TextGray,
                        lineHeight = 20.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showExportDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Done", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }
                },
                containerColor = DarkSurface,
                shape = RoundedCornerShape(18.dp)
            )
        }

        // Import Learned Data Dialog
        if (showImportDialog) {
            AlertDialog(
                onDismissRequest = { showImportDialog = false },
                title = { Text("Import Learned Objects", color = TextWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(
                            text = "Paste learned objects JSON string from another user or cloud backup below:",
                            color = TextGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = importText,
                            onValueChange = { importText = it },
                            placeholder = { Text("[{\"name\": \"...\"}]", color = TextMuted, fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA855F7),
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                cursorColor = Color(0xFFA855F7)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (importText.isNotBlank()) {
                                viewModel.importLearnedObjects(importText)
                            }
                            showImportDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7)),
                        enabled = importText.isNotBlank()
                    ) {
                        Text("Import & Merge", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showImportDialog = false }) {
                        Text("Cancel", color = TextWhite)
                    }
                },
                containerColor = DarkSurface,
                shape = RoundedCornerShape(18.dp)
            )
        }
    }
}

@Composable
fun MinimalSectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextGray,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = TextWhite
        )
    }
}

@Composable
fun MinimalSettingToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = TextGray
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF27272A),
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = Color(0x14FFFFFF),
                uncheckedBorderColor = BorderSubtle,
                checkedBorderColor = Color.White.copy(alpha = 0.4f)
            )
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextGray)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = TextWhite
        )
    }
}
