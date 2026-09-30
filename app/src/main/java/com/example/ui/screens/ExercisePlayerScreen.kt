package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ActiveWorkoutSession
import com.example.ui.PlayerState
import com.example.ui.WorkoutViewModel
import com.example.ui.components.ExerciseVisualizer
import com.example.ui.components.RestTimerOverlay
import com.example.ui.theme.FitnessCyan
import com.example.ui.theme.FitnessGreen
import com.example.ui.theme.FitnessRed
import com.example.ui.theme.WorkoutOrange

@Composable
fun ExercisePlayerScreen(
    session: ActiveWorkoutSession,
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    var showQuitDialog by remember { mutableStateOf(false) }
    val soundEnabled by viewModel.soundEnabled.collectAsState()

    BackHandler {
        viewModel.pauseWorkout()
        showQuitDialog = true
    }

    if (showQuitDialog) {
        AlertDialog(
            onDismissRequest = {
                showQuitDialog = false
                viewModel.resumeWorkout()
            },
            title = {
                Text(text = "Quit Workout?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Do you want to stop this workout session? Your progress will not be saved.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showQuitDialog = false
                        viewModel.stopAndExitWorkout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FitnessRed)
                ) {
                    Text("Quit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showQuitDialog = false
                        viewModel.resumeWorkout()
                    }
                ) {
                    Text("Resume")
                }
            },
            containerColor = Color(0xFF222836),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCBD5E1)
        )
    }

    val currentExercise = session.exercises.getOrNull(session.currentExerciseIndex)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF131720))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Control Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewModel.pauseWorkout()
                            showQuitDialog = true
                        },
                        modifier = Modifier.testTag("player_quit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Quit",
                            tint = Color.White
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = session.workout.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${session.currentExerciseIndex + 1} / ${session.exercises.size}",
                            fontSize = 12.sp,
                            color = WorkoutOrange,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(
                        onClick = { viewModel.soundEnabled.value = !soundEnabled },
                        modifier = Modifier.testTag("player_sound_toggle")
                    ) {
                        Icon(
                            imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Sound Toggle",
                            tint = if (soundEnabled) WorkoutOrange else Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Overall Workout Progress Bar
                val progressFraction = (session.currentExerciseIndex.toFloat() / session.exercises.size.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = WorkoutOrange,
                    trackColor = Color(0xFF262D3D)
                )
            }

            // Center: Animated Canvas Visualizer
            if (currentExercise != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ExerciseVisualizer(
                        animationType = currentExercise.animationType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        isPlaying = session.playerState == PlayerState.EXERCISING
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = currentExercise.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = WorkoutOrange.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = currentExercise.targetMuscle,
                                color = WorkoutOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF262D3D)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Calories",
                                    tint = FitnessRed,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "${session.estimatedCaloriesBurned} kcal",
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Section: Countdown / Reps & Playback Controls
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentExercise != null) {
                    if (currentExercise.reps > 0) {
                        // Reps countdown/counter
                        Text(
                            text = "x${currentExercise.reps}",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = WorkoutOrange
                        )
                        Text(
                            text = "${session.exerciseSecondsRemaining}s time pacing",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    } else {
                        // Duration countdown
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(110.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val stroke = 8.dp.toPx()
                                drawCircle(
                                    color = Color(0xFF262D3D),
                                    style = Stroke(width = stroke)
                                )
                                val maxDuration = currentExercise.durationSeconds.coerceAtLeast(1).toFloat()
                                val sweep = (session.exerciseSecondsRemaining.toFloat() / maxDuration).coerceIn(0f, 1f) * 360f
                                drawArc(
                                    color = WorkoutOrange,
                                    startAngle = -90f,
                                    sweepAngle = sweep,
                                    useCenter = false,
                                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                                )
                            }

                            Text(
                                text = "${session.exerciseSecondsRemaining}s",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Media Controls: Previous, Play/Pause, Skip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.previousExercise() },
                        enabled = session.currentExerciseIndex > 0,
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("player_prev_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous Exercise",
                            tint = if (session.currentExerciseIndex > 0) Color.White else Color(0xFF475569),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Main Play/Pause Button
                    Surface(
                        shape = CircleShape,
                        color = WorkoutOrange,
                        modifier = Modifier.size(68.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (session.playerState == PlayerState.PAUSED) {
                                    viewModel.resumeWorkout()
                                } else {
                                    viewModel.pauseWorkout()
                                }
                            },
                            modifier = Modifier.testTag("player_pause_resume_button")
                        ) {
                            Icon(
                                imageVector = if (session.playerState == PlayerState.PAUSED) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (session.playerState == PlayerState.PAUSED) "Resume" else "Pause",
                                tint = Color.Black,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.skipExercise() },
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("player_skip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Skip Exercise",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        // Overlay: Initial 5-second countdown
        if (session.playerState == PlayerState.COUNTDOWN) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF11141B).copy(alpha = 0.95f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "GET READY",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = WorkoutOrange,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${session.countdownRemaining}",
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (currentExercise != null) {
                        Text(
                            text = currentExercise.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = currentExercise.targetMuscle,
                            fontSize = 14.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // Overlay: Rest Timer between sets / exercises
        if (session.playerState == PlayerState.RESTING) {
            val nextExercise = session.exercises.getOrNull(session.currentExerciseIndex)
            RestTimerOverlay(
                secondsRemaining = session.restSecondsRemaining,
                nextExercise = nextExercise,
                onAdd20Sec = { viewModel.addRestSeconds(20) },
                onSkipRest = { viewModel.skipRest() }
            )
        }
    }
}
