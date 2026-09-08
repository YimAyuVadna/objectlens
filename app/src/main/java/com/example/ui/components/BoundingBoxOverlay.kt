package com.example.ui.components

import android.graphics.Paint as NativePaint
import android.graphics.Typeface as NativeTypeface
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ml.ConfidenceLevel
import com.example.ml.DetectedObject
import com.example.ui.theme.HighConfidenceColor
import com.example.ui.theme.LowConfidenceColor
import com.example.ui.theme.MediumConfidenceColor

@Composable
fun BoundingBoxOverlay(
    detections: List<DetectedObject>,
    showBoundingBoxes: Boolean,
    showConfidence: Boolean,
    selectedObjectId: String?,
    onObjectTapped: (DetectedObject) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val strokeWidthPx = with(density) { 1.5.dp.toPx() }
    val bracketLenPx = with(density) { 18.dp.toPx() }
    val labelTextSizePx = with(density) { 12.sp.toPx() }
    val cornerRadiusPx = with(density) { 8.dp.toPx() }

    val infiniteTransition = rememberInfiniteTransition(label = "viewfinderPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.30f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(detections) {
                detectTapGestures { tapOffset ->
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val clicked = detections.lastOrNull { obj ->
                        val left = obj.normalizedLeft * canvasWidth
                        val top = obj.normalizedTop * canvasHeight
                        val right = obj.normalizedRight * canvasWidth
                        val bottom = obj.normalizedBottom * canvasHeight
                        tapOffset.x in left..right && tapOffset.y in top..bottom
                    }
                    if (clicked != null) {
                        onObjectTapped(clicked)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            if (detections.isEmpty()) {
                // Draw minimalist camera viewfinder frame in the center
                drawMinimalistViewfinder(width, height, pulseAlpha)
                return@Canvas
            }

            if (!showBoundingBoxes) return@Canvas

            for (obj in detections) {
                val isSelected = obj.id == selectedObjectId
                val left = obj.normalizedLeft * width
                val top = obj.normalizedTop * height
                val right = obj.normalizedRight * width
                val bottom = obj.normalizedBottom * height
                val boxWidth = (right - left).coerceAtLeast(10f)
                val boxHeight = (bottom - top).coerceAtLeast(10f)

                val semanticColor = when {
                    obj.isLearned -> Color(0xFFA855F7) // Purple for Learned Objects
                    obj.confidenceLevel == ConfidenceLevel.HIGH -> HighConfidenceColor
                    obj.confidenceLevel == ConfidenceLevel.MEDIUM -> MediumConfidenceColor
                    else -> LowConfidenceColor
                }

                val frameColor = when {
                    isSelected -> Color.White
                    obj.isLearned -> Color(0xFFA855F7).copy(alpha = 0.85f)
                    else -> Color.White.copy(alpha = 0.75f)
                }
                val fillAlpha = if (isSelected) 0.08f else if (obj.isLearned) 0.06f else 0.03f

                // Subtle frosted fill inside bounding box
                drawRoundRect(
                    color = if (obj.isLearned) Color(0xFFA855F7).copy(alpha = fillAlpha) else Color.White.copy(alpha = fillAlpha),
                    topLeft = Offset(left, top),
                    size = Size(boxWidth, boxHeight),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                    style = Fill
                )

                // Hairline rounded outline
                drawRoundRect(
                    color = frameColor.copy(alpha = if (isSelected) 0.9f else 0.5f),
                    topLeft = Offset(left, top),
                    size = Size(boxWidth, boxHeight),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                    style = Stroke(width = strokeWidthPx)
                )

                // Precision corner accent brackets
                drawMinimalCornerBrackets(
                    left = left,
                    top = top,
                    right = right,
                    bottom = bottom,
                    bracketLen = bracketLenPx.coerceAtMost(boxWidth / 3f).coerceAtMost(boxHeight / 3f),
                    strokeWidth = strokeWidthPx * 1.6f,
                    color = if (isSelected) Color.White else semanticColor
                )

                // Label Text: "Object · 95%" or "Object · 95% ✨"
                val prefix = if (obj.isLearned) "✨ " else ""
                val labelText = if (showConfidence) {
                    "$prefix${obj.displayName} · ${obj.confidencePercent}%"
                } else {
                    "$prefix${obj.displayName}"
                }

                // Minimalist frosted glassmorphic pill badge
                drawMinimalLabelBadge(
                    text = labelText,
                    dotColor = semanticColor,
                    left = left,
                    top = top,
                    textSize = labelTextSizePx,
                    isSelected = isSelected
                )
            }
        }
    }
}

/**
 * Draws sleek, camera-style corner brackets on the bounding box.
 */
private fun DrawScope.drawMinimalCornerBrackets(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    bracketLen: Float,
    strokeWidth: Float,
    color: Color
) {
    if (bracketLen <= 2f) return

    val path = Path().apply {
        // Top-Left
        moveTo(left, top + bracketLen)
        lineTo(left, top)
        lineTo(left + bracketLen, top)

        // Top-Right
        moveTo(right - bracketLen, top)
        lineTo(right, top)
        lineTo(right, top + bracketLen)

        // Bottom-Left
        moveTo(left, bottom - bracketLen)
        lineTo(left, bottom)
        lineTo(left + bracketLen, bottom)

        // Bottom-Right
        moveTo(right - bracketLen, bottom)
        lineTo(right, bottom)
        lineTo(right, bottom - bracketLen)
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

/**
 * Draws a clean, frosted dark pill badge with a semantic status dot.
 */
private fun DrawScope.drawMinimalLabelBadge(
    text: String,
    dotColor: Color,
    left: Float,
    top: Float,
    textSize: Float,
    isSelected: Boolean
) {
    val paddingHoriz = 20f
    val paddingVert = 12f
    val dotRadius = 7f
    val dotSpacing = 14f

    val paint = NativePaint().apply {
        color = android.graphics.Color.WHITE
        this.textSize = textSize
        typeface = NativeTypeface.create("sans-serif", NativeTypeface.BOLD)
        isAntiAlias = true
    }

    val textWidth = paint.measureText(text)
    val badgeWidth = textWidth + paddingHoriz * 2 + dotRadius * 2 + dotSpacing
    val badgeHeight = textSize + paddingVert * 2
    val badgeTop = (top - badgeHeight - 6f).coerceAtLeast(8f)

    // Frosted pill container
    drawRoundRect(
        color = Color(0xEE121215),
        topLeft = Offset(left, badgeTop),
        size = Size(badgeWidth, badgeHeight),
        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
        style = Fill
    )

    // Subtle hairline border
    drawRoundRect(
        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.15f),
        topLeft = Offset(left, badgeTop),
        size = Size(badgeWidth, badgeHeight),
        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
        style = Stroke(width = 1.dp.toPx())
    )

    // Semantic status dot
    val dotCenterY = badgeTop + badgeHeight / 2f
    val dotCenterX = left + paddingHoriz + dotRadius
    drawCircle(
        color = dotColor,
        radius = dotRadius,
        center = Offset(dotCenterX, dotCenterY)
    )

    // Text label
    drawContext.canvas.nativeCanvas.drawText(
        text,
        dotCenterX + dotRadius + dotSpacing,
        badgeTop + badgeHeight - paddingVert - 2f,
        paint
    )
}

/**
 * Minimalist viewfinder reticle (four elegant corner framing brackets in center).
 */
private fun DrawScope.drawMinimalistViewfinder(
    canvasWidth: Float,
    canvasHeight: Float,
    pulseAlpha: Float
) {
    val centerX = canvasWidth / 2f
    val centerY = canvasHeight / 2f - 40.dp.toPx() // Slightly above center for camera feel
    val boxSize = 200.dp.toPx()
    val half = boxSize / 2f
    val bracketLen = 28.dp.toPx()
    val strokeWidth = 1.75.dp.toPx()
    val frameColor = Color.White.copy(alpha = pulseAlpha)

    val left = centerX - half
    val right = centerX + half
    val top = centerY - half
    val bottom = centerY + half

    val path = Path().apply {
        // Top-Left
        moveTo(left, top + bracketLen)
        lineTo(left, top)
        lineTo(left + bracketLen, top)

        // Top-Right
        moveTo(right - bracketLen, top)
        lineTo(right, top)
        lineTo(right, top + bracketLen)

        // Bottom-Left
        moveTo(left, bottom - bracketLen)
        lineTo(left, bottom)
        lineTo(left + bracketLen, bottom)

        // Bottom-Right
        moveTo(right - bracketLen, bottom)
        lineTo(right, bottom)
        lineTo(right, bottom - bracketLen)
    }

    drawPath(
        path = path,
        color = frameColor,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    // Center subtle cross-dot
    drawCircle(
        color = Color.White.copy(alpha = pulseAlpha * 0.7f),
        radius = 2.5.dp.toPx(),
        center = Offset(centerX, centerY)
    )

    // Minimal guide prompt
    val promptText = "Point at any object"
    val promptPaint = NativePaint().apply {
        color = android.graphics.Color.WHITE
        alpha = (pulseAlpha * 200).toInt().coerceIn(60, 200)
        textSize = 12.sp.toPx()
        typeface = NativeTypeface.create("sans-serif", NativeTypeface.NORMAL)
        textAlign = NativePaint.Align.CENTER
        isAntiAlias = true
    }

    drawContext.canvas.nativeCanvas.drawText(
        promptText,
        centerX,
        bottom + 28.dp.toPx(),
        promptPaint
    )
}
