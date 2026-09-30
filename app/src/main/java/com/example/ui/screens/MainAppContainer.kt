package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.WorkoutViewModel
import com.example.ui.theme.WorkoutOrange

@Composable
fun MainAppContainer(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    var hasSeenSplash by remember { mutableStateOf(false) }

    val currentTab by viewModel.currentTab.collectAsState()
    val activeWorkoutDetail by viewModel.selectedWorkoutForDetail.collectAsState()
    val activeSession by viewModel.activeSession.collectAsState()
    val finishedResult by viewModel.finishedResult.collectAsState()
    val isCreatingCustom by viewModel.isCreatingCustomWorkout.collectAsState()
    val inspectingExercise by viewModel.inspectingExercise.collectAsState()

    if (!hasSeenSplash) {
        SplashScreen(
            onContinue = { hasSeenSplash = true }
        )
        return
    }

    // Modal exercise info dialog
    if (inspectingExercise != null) {
        ExerciseInfoDialog(
            exercise = inspectingExercise!!,
            onDismiss = { viewModel.closeExerciseInfo() }
        )
    }

    // Modal Result Screen when a workout finishes
    if (finishedResult != null) {
        ExerciseResultScreen(
            result = finishedResult!!,
            viewModel = viewModel
        )
        return
    }

    // Active Exercise Player Screen
    if (activeSession != null) {
        ExercisePlayerScreen(
            session = activeSession!!,
            viewModel = viewModel
        )
        return
    }

    // Custom Workout Creator
    if (isCreatingCustom) {
        CustomWorkoutScreen(viewModel = viewModel)
        return
    }

    // Workout Detail Screen
    if (activeWorkoutDetail != null) {
        WorkoutDetailScreen(
            workout = activeWorkoutDetail!!,
            viewModel = viewModel
        )
        return
    }

    // Main App with Bottom Navigation
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF131720),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1B202C),
                contentColor = Color.White,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.TRAINING,
                    onClick = { viewModel.selectTab(AppTab.TRAINING) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.TRAINING) Icons.Filled.FitnessCenter else Icons.Outlined.FitnessCenter,
                            contentDescription = "Training"
                        )
                    },
                    label = {
                        Text(
                            text = "Training",
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.TRAINING) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = WorkoutOrange,
                        indicatorColor = WorkoutOrange,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_training")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.MEALS,
                    onClick = { viewModel.selectTab(AppTab.MEALS) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.MEALS) Icons.Filled.Restaurant else Icons.Outlined.Restaurant,
                            contentDescription = "Meals"
                        )
                    },
                    label = {
                        Text(
                            text = "Meals",
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.MEALS) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = WorkoutOrange,
                        indicatorColor = WorkoutOrange,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_meals")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.REPORT,
                    onClick = { viewModel.selectTab(AppTab.REPORT) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.REPORT) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                            contentDescription = "Report"
                        )
                    },
                    label = {
                        Text(
                            text = "Report",
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.REPORT) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = WorkoutOrange,
                        indicatorColor = WorkoutOrange,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_report")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.SETTINGS,
                    onClick = { viewModel.selectTab(AppTab.SETTINGS) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    },
                    label = {
                        Text(
                            text = "Settings",
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.SETTINGS) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = WorkoutOrange,
                        indicatorColor = WorkoutOrange,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_settings")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_crossfade"
            ) { tab ->
                when (tab) {
                    AppTab.TRAINING -> TrainingTab(viewModel = viewModel)
                    AppTab.MEALS -> MealsTab(viewModel = viewModel)
                    AppTab.REPORT -> ReportTab(viewModel = viewModel)
                    AppTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}
