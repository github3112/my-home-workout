package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProgressEntity
import com.example.ui.WorkoutViewModel
import com.example.ui.components.StreakWidgetView
import com.example.ui.theme.FitnessCyan
import com.example.ui.theme.FitnessGreen
import com.example.ui.theme.FitnessRed
import com.example.ui.theme.WorkoutOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReportTab(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val totalWorkouts by viewModel.totalWorkoutsCount.collectAsState()
    val totalCalories by viewModel.totalCalories.collectAsState()
    val totalDuration by viewModel.totalDuration.collectAsState()
    val allHistory by viewModel.allProgress.collectAsState()
    val userWeight by viewModel.userWeightKg.collectAsState()
    val userHeight by viewModel.userHeightCm.collectAsState()

    // Calculate BMI
    val heightInMeters = userHeight / 100f
    val bmi = if (heightInMeters > 0) userWeight / (heightInMeters * heightInMeters) else 22.0f
    val bmiCategory = when {
        bmi < 18.5f -> "Underweight"
        bmi < 25.0f -> "Normal weight"
        bmi < 30.0f -> "Overweight"
        else -> "High BMI"
    }
    val bmiColor = when {
        bmi in 18.5f..24.9f -> FitnessGreen
        bmi < 18.5f -> FitnessCyan
        else -> FitnessRed
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "PROGRESS & REPORT",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Weekly goals, streaks and workout history",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Streak Widget
        item {
            StreakWidgetView(
                completedThisWeek = listOf(true, true, false, false, false, false, false),
                currentStreakDays = 2
            )
        }

        // Total Summary Metric Badges
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricSummaryCard(
                    icon = Icons.Default.FitnessCenter,
                    iconColor = WorkoutOrange,
                    value = "$totalWorkouts",
                    label = "Workouts",
                    modifier = Modifier.weight(1f)
                )

                MetricSummaryCard(
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = FitnessRed,
                    value = "${totalCalories ?: 0}",
                    label = "Total kcal",
                    modifier = Modifier.weight(1f)
                )

                val mins = (totalDuration ?: 0) / 60
                MetricSummaryCard(
                    icon = Icons.Default.Schedule,
                    iconColor = FitnessCyan,
                    value = "$mins",
                    label = "Minutes",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Body Metrics & BMI Calculator Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF222836)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = WorkoutOrange.copy(alpha = 0.2f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.MonitorWeight,
                                        contentDescription = "Weight",
                                        tint = WorkoutOrange,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "BMI & Weight Health",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = bmiColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = bmiCategory,
                                color = bmiColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = String.format(Locale.US, "%.1f", bmi),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "BMI Index (Healthy: 18.5 - 24.9)",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        // Weight adjustment controls
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    if (userWeight > 35f) viewModel.userWeightKg.value = userWeight - 0.5f
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFF2D3547), CircleShape)
                                    .testTag("decrease_weight_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                text = "${String.format(Locale.US, "%.1f", userWeight)} kg",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = WorkoutOrange
                            )

                            IconButton(
                                onClick = {
                                    if (userWeight < 200f) viewModel.userWeightKg.value = userWeight + 0.5f
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(WorkoutOrange, CircleShape)
                                    .testTag("increase_weight_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recent Workout History
        item {
            Text(
                text = "WORKOUT HISTORY (${allHistory.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8),
                letterSpacing = 1.sp
            )
        }

        if (allHistory.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF222836)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No workouts recorded yet. Start training today!",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        } else {
            items(allHistory, key = { it.id }) { item ->
                HistoryItemCard(item = item)
            }
        }
    }
}

@Composable
private fun MetricSummaryCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF222836)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
private fun HistoryItemCard(item: UserProgressEntity) {
    val formatter = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
    val dateStr = formatter.format(Date(item.date))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF222836)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("history_item_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.workoutTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = dateStr,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "${item.durationSeconds / 60}m ${item.durationSeconds % 60}s",
                        fontSize = 12.sp,
                        color = WorkoutOrange,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${item.caloriesBurned} kcal",
                        fontSize = 12.sp,
                        color = FitnessRed,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${item.exercisesCompleted} moves",
                        fontSize = 12.sp,
                        color = FitnessCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = WorkoutOrange.copy(alpha = 0.15f)
            ) {
                Text(
                    text = item.feelingFeedback,
                    color = WorkoutOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
