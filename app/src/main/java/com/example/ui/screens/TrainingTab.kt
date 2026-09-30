package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomWorkoutEntity
import com.example.ui.WorkoutViewModel
import com.example.ui.components.StreakWidgetView
import com.example.ui.components.WorkoutCard
import com.example.ui.theme.FitnessCyan
import com.example.ui.theme.WorkoutOrange

@Composable
fun TrainingTab(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val workouts by viewModel.filteredWorkouts.collectAsState()
    val allWorkoutsList by viewModel.allWorkouts.collectAsState()
    val customWorkouts by viewModel.customWorkouts.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsState()

    val pinnedRoutines = customWorkouts.filter { it.isPinned }
    val unpinnedRoutines = customWorkouts.filter { !it.isPinned }

    val categories = listOf("All", "Abs", "Chest", "Arms", "Legs", "Shoulder", "Back", "Full Body")
    val difficulties = listOf("All", "Easy", "Medium", "Hard")

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TRAINING",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Offline bodyweight workouts",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF262D3D)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Active streak",
                                tint = WorkoutOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "2 Days Active",
                                color = WorkoutOrange,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Streak Widget
            item {
                StreakWidgetView(
                    completedThisWeek = listOf(true, true, false, false, false, false, false),
                    currentStreakDays = 2
                )
            }

            // 7-Minute Quick Sprint Action Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            val sprint = allWorkoutsList.firstOrNull { it.id == 1 }
                            if (sprint != null) {
                                viewModel.openWorkoutDetail(sprint)
                            }
                        }
                        .testTag("sprint_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFE65100),
                                        Color(0xFFFFA04A)
                                    )
                                )
                            )
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.Black.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = "FAST BURN • S60 SPRINT",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "7-Minute Full Body Sprint",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )

                                Text(
                                    text = "7 high-intensity movements • 110 kcal",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Start Sprint",
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // PINNED ROUTINES SECTION (Pinned custom workouts at top)
            if (pinnedRoutines.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Pinned",
                                tint = WorkoutOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "PINNED ROUTINES (${pinnedRoutines.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = WorkoutOrange,
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = "Pinned to top",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                items(pinnedRoutines, key = { "pinned_${it.id}" }) { custom ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2436)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, WorkoutOrange),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pinned_routine_${custom.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = WorkoutOrange
                                    ) {
                                        Text(
                                            text = "PINNED",
                                            color = Color.Black,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = WorkoutOrange.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = custom.category.uppercase(),
                                            color = WorkoutOrange,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = custom.title,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    fontSize = 17.sp
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = custom.exercisesJson,
                                    fontSize = 11.sp,
                                    color = Color(0xFFCBD5E1),
                                    maxLines = 2
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Unpin action button
                                IconButton(
                                    onClick = { viewModel.togglePinCustomWorkout(custom) },
                                    modifier = Modifier.testTag("unpin_btn_${custom.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PushPin,
                                        contentDescription = "Unpin",
                                        tint = WorkoutOrange
                                    )
                                }

                                // Play action button
                                Surface(
                                    shape = CircleShape,
                                    color = WorkoutOrange,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable { viewModel.startCustomWorkout(custom) }
                                        .testTag("start_pinned_btn_${custom.id}")
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Play",
                                            tint = Color.Black,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Category Filter Chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "MUSCLE GROUP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { category ->
                            val isSelected = selectedCategory.equals(category, ignoreCase = true)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) WorkoutOrange else Color(0xFF242A38),
                                modifier = Modifier
                                    .clickable { viewModel.setCategory(category) }
                                    .testTag("category_chip_$category")
                            ) {
                                Text(
                                    text = category,
                                    color = if (isSelected) Color.Black else Color(0xFFCBD5E1),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Difficulty Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LEVEL:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )

                    difficulties.forEach { diff ->
                        val isSelected = selectedDifficulty.equals(diff, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFF333D52) else Color(0xFF1E232F),
                            modifier = Modifier
                                .clickable { viewModel.setDifficulty(diff) }
                                .testTag("difficulty_chip_$diff")
                        ) {
                            Text(
                                text = diff,
                                color = if (isSelected) WorkoutOrange else Color(0xFF94A3B8),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Standard Workouts List
            item {
                Text(
                    text = "WORKOUT PLANS (${workouts.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.sp
                )
            }

            items(workouts, key = { it.id }) { workout ->
                WorkoutCard(
                    workout = workout,
                    onClick = { viewModel.openWorkoutDetail(workout) }
                )
            }

            // Unpinned Custom Routines Section
            if (unpinnedRoutines.isNotEmpty()) {
                item {
                    Text(
                        text = "CUSTOM ROUTINES (${unpinnedRoutines.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )
                }

                items(unpinnedRoutines, key = { "custom_${it.id}" }) { custom ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF222836)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = custom.title,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "${custom.category} • ${custom.difficulty}",
                                    fontSize = 12.sp,
                                    color = WorkoutOrange
                                )
                                Text(
                                    text = custom.exercisesJson,
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8),
                                    maxLines = 1
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Pin action button to pin to top
                                IconButton(
                                    onClick = { viewModel.togglePinCustomWorkout(custom) },
                                    modifier = Modifier.testTag("pin_btn_${custom.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PushPin,
                                        contentDescription = "Pin to top",
                                        tint = Color(0xFF94A3B8)
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.startCustomWorkout(custom) },
                                    modifier = Modifier.testTag("start_custom_btn_${custom.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Start Custom Routine",
                                        tint = WorkoutOrange
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.deleteCustomWorkout(custom.id) },
                                    modifier = Modifier.testTag("delete_custom_btn_${custom.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Custom Routine",
                                        tint = Color(0xFFEF5350)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating action button to create custom workout
        FloatingActionButton(
            onClick = { viewModel.openCustomWorkoutCreator() },
            containerColor = WorkoutOrange,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp, end = 20.dp)
                .testTag("create_custom_workout_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Custom Workout"
            )
        }
    }
}
