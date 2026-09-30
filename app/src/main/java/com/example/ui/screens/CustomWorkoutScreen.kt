package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExerciseEntity
import com.example.ui.WorkoutViewModel
import com.example.ui.components.ExerciseVisualizer
import com.example.ui.theme.FitnessCyan
import com.example.ui.theme.FitnessGreen
import com.example.ui.theme.FitnessRed
import com.example.ui.theme.WorkoutOrange

@Composable
fun CustomWorkoutScreen(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.closeCustomWorkoutCreator()
    }

    val distinctExercises by viewModel.libraryExercises.collectAsState()

    var routineTitle by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Full Body") }
    var selectedDifficulty by remember { mutableStateOf("Medium") }
    var pinToTop by remember { mutableStateOf(false) }

    val selectedExercises = remember { mutableStateListOf<ExerciseEntity>() }

    val categories = listOf("Full Body", "Abs", "Chest", "Arms", "Legs", "Shoulder")
    val difficulties = listOf("Easy", "Medium", "Hard")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF141822))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.closeCustomWorkoutCreator() },
                        modifier = Modifier.testTag("custom_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "CUSTOM ROUTINE BUILDER",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Input: Workout Title
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "ROUTINE NAME",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = routineTitle,
                        onValueChange = { routineTitle = it },
                        placeholder = { Text("e.g. My Custom Push & Core Blast", color = Color(0xFF64748B)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_title_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WorkoutOrange,
                            unfocusedBorderColor = Color(0xFF2C3446),
                            focusedContainerColor = Color(0xFF1E2432),
                            unfocusedContainerColor = Color(0xFF1E2432),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                }
            }

            // Category & Difficulty Selection
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "TARGET MUSCLE CATEGORY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.take(3).forEach { cat ->
                            val isSel = selectedCategory == cat
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) WorkoutOrange else Color(0xFF222836),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedCategory = cat }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) Color.Black else Color(0xFFCBD5E1)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.drop(3).forEach { cat ->
                            val isSel = selectedCategory == cat
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) WorkoutOrange else Color(0xFF222836),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedCategory = cat }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) Color.Black else Color(0xFFCBD5E1)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Pin to Top Setting
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2432)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Pin to top",
                                tint = if (pinToTop) WorkoutOrange else Color(0xFF94A3B8),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Pin Routine to Top",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Show prominently at the top of Training",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        Switch(
                            checked = pinToTop,
                            onCheckedChange = { pinToTop = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = WorkoutOrange
                            ),
                            modifier = Modifier.testTag("pin_routine_switch")
                        )
                    }
                }
            }

            // Reorderable Selected Exercises Section
            if (selectedExercises.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "WORKOUT SEQUENCE (${selectedExercises.size})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = WorkoutOrange,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "Reorder & Adjust Reps",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                itemsIndexed(selectedExercises) { index, exercise ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF262130)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WorkoutOrange.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .testTag("selected_exercise_$index")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Order index badge
                            Surface(
                                shape = CircleShape,
                                color = WorkoutOrange,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Mini preview
                            ExerciseVisualizer(
                                animationType = exercise.animationType,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                isPlaying = false
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            // Name & Rep Adjuster
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = exercise.name,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Reps / Time Adjuster: [-] count [+]
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            if (exercise.reps > 4) {
                                                selectedExercises[index] = exercise.copy(reps = exercise.reps - 2)
                                            } else if (exercise.durationSeconds > 15) {
                                                selectedExercises[index] = exercise.copy(durationSeconds = exercise.durationSeconds - 5)
                                            }
                                        },
                                        modifier = Modifier
                                            .size(26.dp)
                                            .background(Color(0xFF382E42), CircleShape)
                                            .testTag("rep_decrease_btn_$index")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Remove,
                                            contentDescription = "Decrease",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Text(
                                        text = if (exercise.reps > 0) "${exercise.reps} reps" else "${exercise.durationSeconds}s",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = WorkoutOrange
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    IconButton(
                                        onClick = {
                                            if (exercise.reps > 0) {
                                                selectedExercises[index] = exercise.copy(reps = exercise.reps + 2)
                                            } else {
                                                selectedExercises[index] = exercise.copy(durationSeconds = exercise.durationSeconds + 5)
                                            }
                                        },
                                        modifier = Modifier
                                            .size(26.dp)
                                            .background(WorkoutOrange, CircleShape)
                                            .testTag("rep_increase_btn_$index")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Increase",
                                            tint = Color.Black,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }

                            // Reorder Controls: Move Up & Move Down
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        if (index > 0) {
                                            val item = selectedExercises.removeAt(index)
                                            selectedExercises.add(index - 1, item)
                                        }
                                    },
                                    enabled = index > 0,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("move_up_btn_$index")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Move Up",
                                        tint = if (index > 0) Color.White else Color(0xFF475569)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (index < selectedExercises.size - 1) {
                                            val item = selectedExercises.removeAt(index)
                                            selectedExercises.add(index + 1, item)
                                        }
                                    },
                                    enabled = index < selectedExercises.size - 1,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("move_down_btn_$index")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Move Down",
                                        tint = if (index < selectedExercises.size - 1) Color.White else Color(0xFF475569)
                                    )
                                }

                                IconButton(
                                    onClick = { selectedExercises.removeAt(index) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("remove_exercise_btn_$index")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = FitnessRed
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Exercise Selection Library Header
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXERCISE LIBRARY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "Tap to add to sequence",
                        fontSize = 11.sp,
                        color = WorkoutOrange
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Distinct Exercises List
            items(distinctExercises, key = { it.id }) { ex ->
                val isAdded = selectedExercises.any { it.name == ex.name }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isAdded) Color(0xFF262130) else Color(0xFF1E2432)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                        .clickable {
                            // Add a new instance to selectedExercises
                            selectedExercises.add(ex)
                        }
                        .testTag("custom_select_${ex.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ExerciseVisualizer(
                            animationType = ex.animationType,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            isPlaying = false
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ex.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = ex.targetMuscle,
                                fontSize = 12.sp,
                                color = WorkoutOrange
                            )
                            Text(
                                text = if (ex.reps > 0) "${ex.reps} reps default" else "${ex.durationSeconds}s default",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = if (isAdded) WorkoutOrange else Color(0xFF2D3547),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isAdded) Icons.Default.Add else Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = if (isAdded) Color.Black else Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Sticky Bottom Save Button
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color(0xFF141822),
            shadowElevation = 8.dp
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        viewModel.saveCustomWorkout(
                            title = routineTitle,
                            category = selectedCategory,
                            difficulty = selectedDifficulty,
                            selectedExercises = selectedExercises.toList(),
                            isPinned = pinToTop
                        )
                    },
                    enabled = selectedExercises.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("custom_save_btn"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WorkoutOrange,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = if (selectedExercises.isEmpty()) "SELECT EXERCISES TO SEQUENCE" else "SAVE ROUTINE (${selectedExercises.size} REPS)",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
