package com.example.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "original_id")
    val originalId: Int = 0,
    val title: String,
    val description: String = "",
    val tags: String = "", // e.g. "abs,core,fatburn"
    @ColumnInfo(name = "set_count")
    val setCount: Int = 1,
    @ColumnInfo(name = "rep_count")
    val repCount: Int = 10,
    val period: String = "S60", // e.g. S60, 45s
    val category: String = "Abs", // Abs, Chest, Arms, Legs, Shoulder, Back, Full Body
    val difficulty: String = "Medium", // Easy, Medium, Hard
    @ColumnInfo(name = "estimated_calories")
    val estimatedCalories: Int = 120,
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int = 420,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "workout_id")
    val workoutId: Int = 0,
    @ColumnInfo(name = "original_id")
    val originalId: String = "",
    val name: String,
    val description: String = "",
    @ColumnInfo(name = "target_muscle")
    val targetMuscle: String = "",
    val equipment: String = "None (Bodyweight)",
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int = 30,
    val reps: Int = 0, // 0 if timed, else rep-based like 15
    @ColumnInfo(name = "animation_type")
    val animationType: String = "jumping_jacks", // jumping_jacks, pushups, squats, plank, crunches, lunges, etc.
    val intensity: String = "Medium",
    val period: String = "S60",
    @ColumnInfo(name = "order_index")
    val orderIndex: Int = 0
)

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "workout_id")
    val workoutId: Int = 0,
    @ColumnInfo(name = "workout_title")
    val workoutTitle: String = "",
    @ColumnInfo(name = "exercises_completed")
    val exercisesCompleted: Int = 0,
    @ColumnInfo(name = "exercises_total")
    val exercisesTotal: Int = 0,
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int = 0,
    @ColumnInfo(name = "calories_burned")
    val caloriesBurned: Int = 0,
    @ColumnInfo(name = "feeling_feedback")
    val feelingFeedback: String = "Done / OK" // Sprint (S60), Challenging, Slightly Hard, A Little Easy, Done / OK
)

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey
    val key: String,
    val value: String
)

@Entity(tableName = "custom_workouts")
data class CustomWorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: String = "Full Body",
    val difficulty: String = "Medium",
    @ColumnInfo(name = "exercises_json")
    val exercisesJson: String = "",
    @ColumnInfo(name = "is_pinned")
    val isPinned: Boolean = false,
    @ColumnInfo(name = "workout_id")
    val workoutId: Int = 0,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

data class MealPlanItem(
    val id: Int,
    val title: String,
    val category: String, // Pre-Workout, Post-Workout, High-Protein, Hydration, Breakfast, Dinner
    val calories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatsGrams: Int,
    val prepTimeMinutes: Int,
    val description: String,
    val ingredients: List<String>,
    val tips: String
)
