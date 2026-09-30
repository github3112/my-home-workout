package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.FitnessGreen
import com.example.ui.theme.WorkoutOrange

@Composable
fun MuscleHighlightView(
    targetCategory: String,
    modifier: Modifier = Modifier
) {
    val highlightColor = WorkoutOrange
    val neutralBody = Color(0xFF475569)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E2430))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val cx = width / 2f

            val category = targetCategory.lowercase()
            val isChest = category.contains("chest") || category.contains("full")
            val isAbs = category.contains("abs") || category.contains("core") || category.contains("full")
            val isArms = category.contains("arm") || category.contains("tricep") || category.contains("bicep") || category.contains("full")
            val isShoulder = category.contains("shoulder") || category.contains("full")
            val isLegs = category.contains("leg") || category.contains("quad") || category.contains("glute") || category.contains("full")
            val isBack = category.contains("back")

            // Head
            val headRadius = height * 0.08f
            val headY = height * 0.12f
            drawCircle(
                color = neutralBody,
                radius = headRadius,
                center = Offset(cx, headY)
            )

            // Neck
            val neckY = headY + headRadius
            val shoulderY = neckY + height * 0.04f

            // Shoulders
            val shoulderWidth = width * 0.42f
            drawRoundRect(
                color = if (isShoulder) highlightColor else neutralBody,
                topLeft = Offset(cx - shoulderWidth / 2f, shoulderY - height * 0.02f),
                size = Size(shoulderWidth, height * 0.06f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
            )

            // Chest
            val chestY = shoulderY + height * 0.04f
            val chestWidth = width * 0.32f
            drawRoundRect(
                color = if (isChest) highlightColor else neutralBody,
                topLeft = Offset(cx - chestWidth / 2f, chestY),
                size = Size(chestWidth, height * 0.10f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
            )

            // Arms (Biceps / Forearms)
            val armWidth = width * 0.09f
            val leftArmX = cx - shoulderWidth / 2f - armWidth * 0.8f
            val rightArmX = cx + shoulderWidth / 2f - armWidth * 0.2f
            drawRoundRect(
                color = if (isArms) highlightColor else neutralBody,
                topLeft = Offset(leftArmX, chestY),
                size = Size(armWidth, height * 0.22f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )
            drawRoundRect(
                color = if (isArms) highlightColor else neutralBody,
                topLeft = Offset(rightArmX, chestY),
                size = Size(armWidth, height * 0.22f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )

            // Abs / Core
            val absY = chestY + height * 0.11f
            val absWidth = width * 0.26f
            drawRoundRect(
                color = if (isAbs) highlightColor else neutralBody,
                topLeft = Offset(cx - absWidth / 2f, absY),
                size = Size(absWidth, height * 0.12f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )

            // Pelvis
            val pelvisY = absY + height * 0.12f
            val pelvisWidth = width * 0.28f
            drawRoundRect(
                color = if (isBack) highlightColor else neutralBody,
                topLeft = Offset(cx - pelvisWidth / 2f, pelvisY),
                size = Size(pelvisWidth, height * 0.06f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
            )

            // Legs (Thighs)
            val legWidth = width * 0.11f
            val legGap = width * 0.04f
            val thighY = pelvisY + height * 0.06f
            drawRoundRect(
                color = if (isLegs) highlightColor else neutralBody,
                topLeft = Offset(cx - legGap / 2f - legWidth, thighY),
                size = Size(legWidth, height * 0.18f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )
            drawRoundRect(
                color = if (isLegs) highlightColor else neutralBody,
                topLeft = Offset(cx + legGap / 2f, thighY),
                size = Size(legWidth, height * 0.18f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )

            // Calves
            val calfY = thighY + height * 0.19f
            drawRoundRect(
                color = if (isLegs) highlightColor else neutralBody,
                topLeft = Offset(cx - legGap / 2f - legWidth * 0.9f, calfY),
                size = Size(legWidth * 0.9f, height * 0.15f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
            )
            drawRoundRect(
                color = if (isLegs) highlightColor else neutralBody,
                topLeft = Offset(cx + legGap / 2f, calfY),
                size = Size(legWidth * 0.9f, height * 0.15f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
            )
        }
    }
}
