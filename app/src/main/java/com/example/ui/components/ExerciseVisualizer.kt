package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.ui.theme.FitnessCyan
import com.example.ui.theme.FitnessGreen
import com.example.ui.theme.WorkoutOrange
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ExerciseVisualizer(
    animationType: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    accentColor: Color = WorkoutOrange
) {
    val infiniteTransition = rememberInfiniteTransition(label = "exercise_anim")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isPlaying) 1600 else 1000000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "anim_progress"
    )

    // Smooth sinusoidal wave between 0.0f and 1.0f
    val cycle = ((sin(progress.toDouble() * 2.0 * PI - PI / 2.0) + 1.0) / 2.0).toFloat()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF262D3D),
                        Color(0xFF161922)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val cx = width / 2f
            val cy = height / 2f

            // Floor guideline
            val floorY = height * 0.82f
            drawLine(
                color = Color.White.copy(alpha = 0.12f),
                start = Offset(width * 0.15f, floorY),
                end = Offset(width * 0.85f, floorY),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            val headRadius = width * 0.075f
            val bodyColor = Color.White
            val limbColor = Color(0xFFE2E8F0)
            val strokeWidthVal = 7.dp.toPx()

            when (animationType.lowercase()) {
                "jumping_jacks", "jumping_jack" -> {
                    val spread = cycle * 0.28f
                    val armAngle = (cycle * 1.8f).toDouble()

                    val headCenter = Offset(cx, cy - height * 0.25f - cycle * height * 0.05f)
                    val neck = Offset(cx, headCenter.y + headRadius)
                    val pelvis = Offset(cx, cy + height * 0.08f - cycle * height * 0.05f)

                    drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
                    drawLine(color = bodyColor, start = neck, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val leftFoot = Offset(cx - width * (0.08f + spread), floorY - (1f - cycle) * height * 0.02f)
                    val rightFoot = Offset(cx + width * (0.08f + spread), floorY - (1f - cycle) * height * 0.02f)
                    val leftKnee = Offset(cx - width * (0.05f + spread * 0.6f), (pelvis.y + leftFoot.y) / 2f)
                    val rightKnee = Offset(cx + width * (0.05f + spread * 0.6f), (pelvis.y + rightFoot.y) / 2f)

                    drawLine(color = limbColor, start = pelvis, end = leftKnee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = leftKnee, end = leftFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = pelvis, end = rightKnee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = rightKnee, end = rightFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val armLength = width * 0.28f
                    val leftHand = Offset(
                        (cx - armLength * cos(armAngle - 0.4)).toFloat(),
                        (neck.y - armLength * sin(armAngle - 0.4)).toFloat()
                    )
                    val rightHand = Offset(
                        (cx + armLength * cos(armAngle - 0.4)).toFloat(),
                        (neck.y - armLength * sin(armAngle - 0.4)).toFloat()
                    )
                    drawLine(color = accentColor, start = neck, end = leftHand, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = neck, end = rightHand, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                "arm_circles" -> {
                    val head = Offset(cx, cy - height * 0.22f)
                    val neck = Offset(cx, head.y + headRadius)
                    val pelvis = Offset(cx, cy + height * 0.14f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = neck, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val leftFoot = Offset(cx - width * 0.12f, floorY)
                    val rightFoot = Offset(cx + width * 0.12f, floorY)
                    drawLine(color = limbColor, start = pelvis, end = leftFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = pelvis, end = rightFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val circleAngle = (progress.toDouble() * 2.0 * PI)
                    val armLength = width * 0.26f
                    val orbitR = 14.dp.toPx()

                    val leftArm = Offset(
                        (cx - armLength + cos(circleAngle) * orbitR).toFloat(),
                        (neck.y + sin(circleAngle) * orbitR).toFloat()
                    )
                    val rightArm = Offset(
                        (cx + armLength + cos(circleAngle) * orbitR).toFloat(),
                        (neck.y + sin(circleAngle) * orbitR).toFloat()
                    )

                    drawLine(color = accentColor, start = neck, end = leftArm, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = neck, end = rightArm, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                "shoulder_stretch" -> {
                    // Standing figure pulling arm horizontally across chest
                    val breath = cycle * 0.04f
                    val head = Offset(cx, cy - height * 0.24f)
                    val neck = Offset(cx, head.y + headRadius)
                    val pelvis = Offset(cx, cy + height * 0.12f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = neck, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val leftFoot = Offset(cx - width * 0.12f, floorY)
                    val rightFoot = Offset(cx + width * 0.12f, floorY)
                    drawLine(color = limbColor, start = pelvis, end = leftFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = pelvis, end = rightFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Stretched arm across chest (Right arm pulled to left)
                    val rightShoulder = Offset(cx + width * 0.14f, neck.y + height * 0.02f)
                    val leftElbowTarget = Offset(cx - width * 0.18f + breath * width, neck.y + height * 0.06f)
                    drawLine(color = accentColor, start = rightShoulder, end = leftElbowTarget, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Hooking arm (Left arm holding it close)
                    val leftShoulder = Offset(cx - width * 0.14f, neck.y + height * 0.02f)
                    val hookElbow = Offset(cx - width * 0.12f, neck.y + height * 0.14f)
                    val hookHand = Offset(cx - width * 0.02f, neck.y + height * 0.04f)
                    drawLine(color = limbColor, start = leftShoulder, end = hookElbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = hookElbow, end = hookHand, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Shoulder highlight
                    drawCircle(
                        color = accentColor.copy(alpha = 0.45f + cycle * 0.4f),
                        radius = 12.dp.toPx(),
                        center = rightShoulder
                    )
                }

                "staggered_pushup" -> {
                    // Pushup with one hand forward and one hand backward
                    val pushProgress = cycle
                    val feet = Offset(cx - width * 0.35f, floorY - 6.dp.toPx())

                    // Front hand forward, back hand pulled backward
                    val handFront = Offset(cx + width * 0.28f, floorY - 4.dp.toPx())
                    val handBack = Offset(cx + width * 0.12f, floorY - 4.dp.toPx())

                    val shoulderY = (floorY - height * 0.28f) + pushProgress * height * 0.16f
                    val shoulderFront = Offset(cx + width * 0.24f, shoulderY)
                    val shoulderBack = Offset(cx + width * 0.16f, shoulderY)
                    val head = Offset(shoulderFront.x + headRadius * 1.4f, shoulderY - headRadius * 0.3f)
                    val hip = Offset(cx - width * 0.08f, (feet.y + shoulderY) / 2f + (1f - pushProgress) * height * 0.02f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulderFront, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = hip, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Staggered arm lines
                    val elbowFront = Offset(handFront.x - width * 0.08f * pushProgress, (shoulderFront.y + handFront.y) / 2f)
                    val elbowBack = Offset(handBack.x - width * 0.12f * pushProgress, (shoulderBack.y + handBack.y) / 2f)
                    drawLine(color = accentColor, start = shoulderFront, end = elbowFront, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = elbowFront, end = handFront, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = shoulderBack, end = elbowBack, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = elbowBack, end = handBack, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Chest activation glow
                    drawCircle(
                        color = accentColor.copy(alpha = 0.4f + cycle * 0.4f),
                        radius = 12.dp.toPx(),
                        center = shoulderFront
                    )
                }

                "pushup_and_rotation" -> {
                    // Pushup then side-plank T-rotation
                    val phase = progress * 2.0 // 0 to 1 = pushup, 1 to 2 = rotation
                    if (phase < 1.0) {
                        // Pushup phase
                        val pCycle = ((sin(phase * 2.0 * PI - PI / 2.0) + 1.0) / 2.0).toFloat()
                        val feet = Offset(cx - width * 0.35f, floorY - 6.dp.toPx())
                        val hands = Offset(cx + width * 0.22f, floorY - 4.dp.toPx())
                        val shoulderY = (floorY - height * 0.28f) + pCycle * height * 0.16f
                        val shoulder = Offset(hands.x, shoulderY)
                        val head = Offset(shoulder.x + headRadius * 1.5f, shoulder.y - headRadius * 0.4f)
                        val hip = Offset(cx - width * 0.08f, (feet.y + shoulder.y) / 2f)

                        drawCircle(color = bodyColor, radius = headRadius, center = head)
                        drawLine(color = bodyColor, start = shoulder, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                        drawLine(color = limbColor, start = hip, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                        val elbow = Offset(hands.x - width * 0.12f * pCycle, (shoulder.y + hands.y) / 2f)
                        drawLine(color = accentColor, start = shoulder, end = elbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                        drawLine(color = accentColor, start = elbow, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    } else {
                        // Rotation phase (Side T-plank)
                        val rotProgress = ((sin((phase - 1.0) * PI)).toFloat())
                        val feet = Offset(cx - width * 0.35f, floorY - 6.dp.toPx())
                        val groundHand = Offset(cx + width * 0.15f, floorY - 4.dp.toPx())
                        val shoulderY = floorY - height * 0.28f
                        val shoulder = Offset(groundHand.x, shoulderY)
                        val head = Offset(shoulder.x + headRadius * 1.3f, shoulder.y - headRadius * 0.5f)
                        val hip = Offset(cx - width * 0.1f, floorY - height * 0.20f)

                        drawCircle(color = bodyColor, radius = headRadius, center = head)
                        drawLine(color = bodyColor, start = shoulder, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                        drawLine(color = limbColor, start = hip, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                        drawLine(color = limbColor, start = shoulder, end = groundHand, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                        // Arm reaching up to sky
                        val skyHand = Offset(shoulder.x - width * 0.05f * rotProgress, shoulder.y - height * 0.28f * rotProgress)
                        drawLine(color = accentColor, start = shoulder, end = skyHand, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                        drawCircle(color = accentColor, radius = 6.dp.toPx(), center = skyHand)
                    }
                }

                "diamond_pushup" -> {
                    // Close hands diamond pushup
                    val pushProgress = cycle
                    val feet = Offset(cx - width * 0.35f, floorY - 6.dp.toPx())
                    val hands = Offset(cx + width * 0.20f, floorY - 4.dp.toPx())

                    val shoulderY = (floorY - height * 0.28f) + pushProgress * height * 0.16f
                    val shoulder = Offset(hands.x, shoulderY)
                    val head = Offset(shoulder.x + headRadius * 1.5f, shoulder.y - headRadius * 0.4f)
                    val hip = Offset(cx - width * 0.08f, (feet.y + shoulder.y) / 2f + (1f - pushProgress) * height * 0.02f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulder, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = hip, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Elbows tucked close to body
                    val elbow = Offset(hands.x - width * 0.06f, (shoulder.y + hands.y) / 2f + height * 0.02f * (1f - pushProgress))
                    drawLine(color = accentColor, start = shoulder, end = elbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = elbow, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Diamond hand symbol on floor
                    drawCircle(color = accentColor, radius = 8.dp.toPx(), center = hands)
                    // Tricep contraction highlight
                    drawCircle(color = accentColor.copy(alpha = 0.4f + cycle * 0.5f), radius = 10.dp.toPx(), center = elbow)
                }

                "box_pushup" -> {
                    // Pushup on hands and knees (or hands on box)
                    val pushProgress = cycle
                    val knees = Offset(cx - width * 0.20f, floorY - 6.dp.toPx())
                    val feet = Offset(cx - width * 0.34f, floorY - height * 0.08f)
                    val hands = Offset(cx + width * 0.18f, floorY - 4.dp.toPx())

                    val shoulderY = (floorY - height * 0.24f) + pushProgress * height * 0.14f
                    val shoulder = Offset(hands.x, shoulderY)
                    val head = Offset(shoulder.x + headRadius * 1.4f, shoulder.y - headRadius * 0.3f)
                    val hip = Offset(knees.x + width * 0.04f, floorY - height * 0.18f + pushProgress * height * 0.06f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulder, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = hip, end = knees, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = knees, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val elbow = Offset(hands.x - width * 0.10f * pushProgress, (shoulder.y + hands.y) / 2f)
                    drawLine(color = accentColor, start = shoulder, end = elbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = elbow, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                "spiderman_pushup" -> {
                    // As chest descends, knee drives forward toward elbow
                    val pushProgress = cycle
                    val feet = Offset(cx - width * 0.35f, floorY - 6.dp.toPx())
                    val hands = Offset(cx + width * 0.22f, floorY - 4.dp.toPx())

                    val shoulderY = (floorY - height * 0.28f) + pushProgress * height * 0.16f
                    val shoulder = Offset(hands.x, shoulderY)
                    val head = Offset(shoulder.x + headRadius * 1.5f, shoulder.y - headRadius * 0.4f)
                    val hip = Offset(cx - width * 0.08f, (feet.y + shoulder.y) / 2f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulder, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Straight back leg
                    drawLine(color = limbColor, start = hip, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Spiderman knee driving outward & upward toward elbow
                    val spiderKnee = Offset(
                        hip.x + width * 0.18f * pushProgress,
                        floorY - height * 0.12f - pushProgress * height * 0.08f
                    )
                    val spiderFoot = Offset(spiderKnee.x - width * 0.06f, floorY - 6.dp.toPx())
                    drawLine(color = accentColor, start = hip, end = spiderKnee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = spiderKnee, end = spiderFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Arms
                    val elbow = Offset(hands.x - width * 0.12f * pushProgress, (shoulder.y + hands.y) / 2f)
                    drawLine(color = limbColor, start = shoulder, end = elbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = elbow, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                "decline_pushup" -> {
                    // Feet elevated on box/bench
                    val pushProgress = cycle
                    val benchWidth = width * 0.20f
                    val benchHeight = height * 0.18f

                    // Draw elevated bench
                    drawRect(
                        color = Color(0xFF333D52),
                        topLeft = Offset(cx - width * 0.42f, floorY - benchHeight),
                        size = Size(benchWidth, benchHeight)
                    )

                    val feet = Offset(cx - width * 0.32f, floorY - benchHeight - 4.dp.toPx())
                    val hands = Offset(cx + width * 0.20f, floorY - 4.dp.toPx())

                    val shoulderY = (floorY - height * 0.22f) + pushProgress * height * 0.14f
                    val shoulder = Offset(hands.x, shoulderY)
                    val head = Offset(shoulder.x + headRadius * 1.5f, shoulder.y - headRadius * 0.4f)
                    val hip = Offset(cx - width * 0.08f, (feet.y + shoulder.y) / 2f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulder, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = hip, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val elbow = Offset(hands.x - width * 0.12f * pushProgress, (shoulder.y + hands.y) / 2f)
                    drawLine(color = accentColor, start = shoulder, end = elbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = elbow, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Upper chest glow
                    drawCircle(
                        color = accentColor.copy(alpha = 0.45f + cycle * 0.45f),
                        radius = 12.dp.toPx(),
                        center = shoulder
                    )
                }

                "incline_pushup" -> {
                    // Hands elevated on box/bench
                    val pushProgress = cycle
                    val benchWidth = width * 0.22f
                    val benchHeight = height * 0.20f

                    // Draw platform under hands
                    drawRect(
                        color = Color(0xFF333D52),
                        topLeft = Offset(cx + width * 0.10f, floorY - benchHeight),
                        size = Size(benchWidth, benchHeight)
                    )

                    val feet = Offset(cx - width * 0.35f, floorY - 6.dp.toPx())
                    val hands = Offset(cx + width * 0.18f, floorY - benchHeight - 4.dp.toPx())

                    val shoulderY = (floorY - benchHeight - height * 0.18f) + pushProgress * height * 0.12f
                    val shoulder = Offset(hands.x, shoulderY)
                    val head = Offset(shoulder.x + headRadius * 1.4f, shoulder.y - headRadius * 0.3f)
                    val hip = Offset(cx - width * 0.10f, (feet.y + shoulder.y) / 2f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulder, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = hip, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val elbow = Offset(hands.x - width * 0.10f * pushProgress, (shoulder.y + hands.y) / 2f)
                    drawLine(color = accentColor, start = shoulder, end = elbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = elbow, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                "chest_stretch" -> {
                    // Standing, hands clasped behind back, chest open
                    val expand = cycle * 0.05f
                    val head = Offset(cx, cy - height * 0.25f - expand * height)
                    val neck = Offset(cx, head.y + headRadius)
                    val chest = Offset(cx + width * 0.04f + expand * width, cy - height * 0.05f)
                    val pelvis = Offset(cx, cy + height * 0.12f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = neck, end = chest, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = chest, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val leftFoot = Offset(cx - width * 0.12f, floorY)
                    val rightFoot = Offset(cx + width * 0.12f, floorY)
                    drawLine(color = limbColor, start = pelvis, end = leftFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = pelvis, end = rightFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Arms clasped behind back
                    val shoulder = Offset(cx - width * 0.06f, neck.y + height * 0.02f)
                    val claspedHands = Offset(cx - width * 0.16f - expand * width, cy + height * 0.06f)
                    drawLine(color = accentColor, start = shoulder, end = claspedHands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    // Chest expansion glow
                    drawCircle(
                        color = accentColor.copy(alpha = 0.35f + cycle * 0.45f),
                        radius = (14f + cycle * 8f).dp.toPx(),
                        center = chest
                    )
                }

                "pushups", "classic_pushups", "wide_arm_pushups" -> {
                    val pushProgress = cycle
                    val feet = Offset(cx - width * 0.35f, floorY - 6.dp.toPx())
                    val hands = Offset(cx + width * 0.22f, floorY - 4.dp.toPx())

                    val shoulderY = (floorY - height * 0.28f) + pushProgress * height * 0.16f
                    val shoulder = Offset(hands.x, shoulderY)
                    val head = Offset(shoulder.x + headRadius * 1.5f, shoulder.y - headRadius * 0.4f)
                    val hip = Offset(
                        cx - width * 0.08f,
                        (feet.y + shoulder.y) / 2f + (1f - pushProgress) * height * 0.02f
                    )

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulder, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = hip, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val elbow = Offset(
                        hands.x - width * 0.12f * pushProgress,
                        (shoulder.y + hands.y) / 2f + height * 0.04f * (1f - pushProgress)
                    )
                    drawLine(color = accentColor, start = shoulder, end = elbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = elbow, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    drawCircle(
                        color = accentColor.copy(alpha = 0.4f + cycle * 0.5f),
                        radius = 10.dp.toPx(),
                        center = Offset(shoulder.x - 8.dp.toPx(), shoulder.y + 6.dp.toPx())
                    )
                }

                "squats" -> {
                    val squatDepth = cycle
                    val hipDrop = squatDepth * height * 0.22f
                    val headY = cy - height * 0.24f + hipDrop
                    val head = Offset(cx, headY)
                    val neck = Offset(cx, headY + headRadius)
                    val pelvis = Offset(cx - width * 0.04f * squatDepth, cy + height * 0.05f + hipDrop)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = neck, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val leftFoot = Offset(cx - width * 0.16f, floorY)
                    val rightFoot = Offset(cx + width * 0.16f, floorY)
                    val kneeY = floorY - height * 0.16f + hipDrop * 0.35f
                    val leftKnee = Offset(leftFoot.x - width * 0.08f * squatDepth, kneeY)
                    val rightKnee = Offset(rightFoot.x + width * 0.08f * squatDepth, kneeY)

                    drawLine(color = accentColor, start = pelvis, end = leftKnee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = leftKnee, end = leftFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = pelvis, end = rightKnee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = rightKnee, end = rightFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val hands = Offset(cx + width * 0.26f, neck.y + height * 0.02f)
                    drawLine(color = limbColor, start = neck, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                "plank" -> {
                    val floatOffset = (sin(progress.toDouble() * 2.0 * PI) * 3.0).toFloat()
                    val feet = Offset(cx - width * 0.36f, floorY - 6.dp.toPx())
                    val elbows = Offset(cx + width * 0.24f, floorY - 4.dp.toPx())
                    val shoulder = Offset(elbows.x, floorY - height * 0.18f + floatOffset)
                    val hip = Offset(cx - width * 0.08f, floorY - height * 0.16f + floatOffset * 0.7f)
                    val head = Offset(shoulder.x + headRadius * 1.5f, shoulder.y - headRadius * 0.2f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulder, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = hip, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = shoulder, end = elbows, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val hands = Offset(elbows.x + width * 0.12f, floorY - 4.dp.toPx())
                    drawLine(color = limbColor, start = elbows, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    drawCircle(
                        color = accentColor.copy(alpha = 0.3f + cycle * 0.4f),
                        radius = (14f + cycle * 8f).dp.toPx(),
                        center = hip
                    )
                }

                "crunches", "bicycle_crunches" -> {
                    val curl = cycle
                    val pelvis = Offset(cx - width * 0.1f, floorY - 8.dp.toPx())
                    val knees = Offset(cx + width * 0.18f, floorY - height * 0.2f)
                    val feet = Offset(cx + width * 0.3f, floorY - 6.dp.toPx())

                    drawLine(color = limbColor, start = pelvis, end = knees, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = knees, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val shoulder = Offset(
                        cx - width * 0.28f + curl * width * 0.08f,
                        floorY - 14.dp.toPx() - curl * height * 0.14f
                    )
                    val head = Offset(shoulder.x - headRadius * 1.2f, shoulder.y - headRadius * 0.5f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = pelvis, end = shoulder, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val elbow = Offset(shoulder.x - width * 0.06f, shoulder.y - height * 0.06f)
                    drawLine(color = accentColor, start = shoulder, end = elbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = elbow, end = head, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    drawCircle(
                        color = accentColor.copy(alpha = 0.35f + curl * 0.45f),
                        radius = (12f + curl * 6f).dp.toPx(),
                        center = Offset((pelvis.x + shoulder.x) / 2f, (pelvis.y + shoulder.y) / 2f)
                    )
                }

                "leg_raises" -> {
                    val raiseAngle = (cycle * (PI.toFloat() / 2.2f)).toDouble()
                    val head = Offset(cx - width * 0.35f, floorY - headRadius)
                    val shoulder = Offset(cx - width * 0.24f, floorY - 10.dp.toPx())
                    val pelvis = Offset(cx - width * 0.02f, floorY - 10.dp.toPx())

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulder, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val legLength = width * 0.42f
                    val feet = Offset(
                        (pelvis.x + legLength * cos(raiseAngle)).toFloat(),
                        (pelvis.y - legLength * sin(raiseAngle)).toFloat()
                    )
                    drawLine(color = accentColor, start = pelvis, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    drawCircle(
                        color = accentColor.copy(alpha = 0.3f + cycle * 0.4f),
                        radius = 12.dp.toPx(),
                        center = pelvis
                    )
                }

                "lunges" -> {
                    val lungeDepth = cycle
                    val drop = lungeDepth * height * 0.16f
                    val head = Offset(cx, cy - height * 0.26f + drop)
                    val neck = Offset(cx, head.y + headRadius)
                    val pelvis = Offset(cx, cy + height * 0.06f + drop)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = neck, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val frontFoot = Offset(cx + width * 0.24f, floorY)
                    val frontKnee = Offset(cx + width * 0.18f, floorY - height * 0.14f + drop * 0.3f)
                    drawLine(color = accentColor, start = pelvis, end = frontKnee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = frontKnee, end = frontFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val backFoot = Offset(cx - width * 0.28f, floorY)
                    val backKnee = Offset(cx - width * 0.12f, floorY - height * 0.08f + drop * 0.5f)
                    drawLine(color = accentColor, start = pelvis, end = backKnee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = backKnee, end = backFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val leftHip = Offset(pelvis.x - width * 0.08f, pelvis.y)
                    val rightHip = Offset(pelvis.x + width * 0.08f, pelvis.y)
                    drawLine(color = limbColor, start = neck, end = leftHip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = neck, end = rightHip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                "high_knees", "mountain_climbers" -> {
                    val stepCycle = ((sin(progress.toDouble() * 4.0 * PI) + 1.0) / 2.0).toFloat()
                    val head = Offset(cx, cy - height * 0.24f - stepCycle * height * 0.03f)
                    val neck = Offset(cx, head.y + headRadius)
                    val pelvis = Offset(cx, cy + height * 0.08f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = neck, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val leftKnee = Offset(cx - width * 0.12f, pelvis.y - stepCycle * height * 0.18f)
                    val leftFoot = Offset(cx - width * 0.1f, leftKnee.y + height * 0.14f)
                    drawLine(color = accentColor, start = pelvis, end = leftKnee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = leftKnee, end = leftFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val rightKnee = Offset(cx + width * 0.08f, pelvis.y + (1f - stepCycle) * height * 0.1f)
                    val rightFoot = Offset(cx + width * 0.08f, floorY)
                    drawLine(color = accentColor, start = pelvis, end = rightKnee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = rightKnee, end = rightFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val leftHand = Offset(cx + width * 0.16f, neck.y + (1f - stepCycle) * height * 0.12f)
                    val rightHand = Offset(cx - width * 0.16f, neck.y + stepCycle * height * 0.12f)
                    drawLine(color = limbColor, start = neck, end = leftHand, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = neck, end = rightHand, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                "tricep_dips" -> {
                    val dipDepth = cycle
                    val drop = dipDepth * height * 0.16f

                    drawRect(
                        color = Color.White.copy(alpha = 0.2f),
                        topLeft = Offset(cx - width * 0.36f, floorY - height * 0.22f),
                        size = Size(width * 0.24f, height * 0.22f)
                    )

                    val hands = Offset(cx - width * 0.14f, floorY - height * 0.22f)
                    val shoulder = Offset(cx - width * 0.08f, floorY - height * 0.34f + drop)
                    val head = Offset(shoulder.x, shoulder.y - headRadius * 1.3f)
                    val pelvis = Offset(shoulder.x + width * 0.04f, floorY - height * 0.18f + drop)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = shoulder, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val elbow = Offset(hands.x - width * 0.08f * dipDepth, (shoulder.y + hands.y) / 2f)
                    drawLine(color = accentColor, start = shoulder, end = elbow, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = elbow, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val feet = Offset(cx + width * 0.26f, floorY - 4.dp.toPx())
                    drawLine(color = limbColor, start = pelvis, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                "cobra_stretch" -> {
                    val arch = cycle
                    val hands = Offset(cx - width * 0.06f, floorY - 4.dp.toPx())
                    val pelvis = Offset(cx - width * 0.04f, floorY - 8.dp.toPx())
                    val feet = Offset(cx - width * 0.38f, floorY - 4.dp.toPx())

                    drawLine(color = limbColor, start = pelvis, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)

                    val shoulder = Offset(
                        cx + width * 0.08f,
                        floorY - height * 0.16f - arch * height * 0.12f
                    )
                    val head = Offset(shoulder.x + headRadius * 1.3f, shoulder.y - headRadius * 0.5f)

                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    drawLine(color = bodyColor, start = pelvis, end = shoulder, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = shoulder, end = hands, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }

                else -> {
                    val head = Offset(cx, cy - height * 0.24f)
                    drawCircle(color = bodyColor, radius = headRadius, center = head)
                    val neck = Offset(cx, head.y + headRadius)
                    val pelvis = Offset(cx, cy + height * 0.1f)
                    drawLine(color = bodyColor, start = neck, end = pelvis, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    val leftFoot = Offset(cx - width * 0.14f, floorY)
                    val rightFoot = Offset(cx + width * 0.14f, floorY)
                    drawLine(color = limbColor, start = pelvis, end = leftFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = limbColor, start = pelvis, end = rightFoot, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }
            }
        }
    }
}
