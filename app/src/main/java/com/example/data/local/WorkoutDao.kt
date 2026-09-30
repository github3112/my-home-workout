package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CustomWorkoutEntity
import com.example.data.model.ExerciseEntity
import com.example.data.model.SettingEntity
import com.example.data.model.UserProgressEntity
import com.example.data.model.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // Workouts
    @Query("SELECT * FROM workouts ORDER BY id ASC")
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE category = :category ORDER BY id ASC")
    fun getWorkoutsByCategory(category: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE id = :id LIMIT 1")
    suspend fun getWorkoutById(id: Int): WorkoutEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    // Exercises
    @Query("SELECT * FROM exercises WHERE workout_id = :workoutId ORDER BY order_index ASC")
    fun getExercisesForWorkout(workoutId: Int): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE workout_id = :workoutId ORDER BY order_index ASC")
    suspend fun getExercisesForWorkoutSync(workoutId: Int): List<ExerciseEntity>

    @Query("SELECT * FROM exercises GROUP BY name ORDER BY id ASC")
    fun getAllDistinctExercises(): Flow<List<ExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    // Progress
    @Query("SELECT * FROM user_progress ORDER BY date DESC")
    fun getAllProgress(): Flow<List<UserProgressEntity>>

    @Query("SELECT * FROM user_progress ORDER BY date DESC LIMIT :limit")
    fun getRecentProgress(limit: Int): Flow<List<UserProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgressEntity): Long

    @Query("SELECT COUNT(*) FROM user_progress")
    fun getCompletedWorkoutsCount(): Flow<Int>

    @Query("SELECT SUM(calories_burned) FROM user_progress")
    fun getTotalCaloriesBurned(): Flow<Int?>

    @Query("SELECT SUM(duration_seconds) FROM user_progress")
    fun getTotalDurationSeconds(): Flow<Int?>

    // Settings
    @Query("SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    suspend fun getSetting(key: String): String?

    @Query("SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    fun observeSetting(key: String): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSetting(setting: SettingEntity)

    // Custom Workouts
    @Query("SELECT * FROM custom_workouts ORDER BY is_pinned DESC, created_at DESC")
    fun getAllCustomWorkouts(): Flow<List<CustomWorkoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomWorkout(customWorkout: CustomWorkoutEntity): Long

    @Query("UPDATE custom_workouts SET is_pinned = :isPinned WHERE id = :id")
    suspend fun updatePinStatus(id: Int, isPinned: Boolean)

    @Query("SELECT * FROM custom_workouts WHERE id = :id LIMIT 1")
    suspend fun getCustomWorkoutById(id: Int): CustomWorkoutEntity?

    @Query("DELETE FROM custom_workouts WHERE id = :id")
    suspend fun deleteCustomWorkout(id: Int)
}
