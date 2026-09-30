package com.example.data.repository

import com.example.data.local.WorkoutDao
import com.example.data.model.CustomWorkoutEntity
import com.example.data.model.ExerciseEntity
import com.example.data.model.MealPlanItem
import com.example.data.model.SettingEntity
import com.example.data.model.UserProgressEntity
import com.example.data.model.WorkoutEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class WorkoutRepository(private val dao: WorkoutDao) {

    val allWorkouts: Flow<List<WorkoutEntity>> = dao.getAllWorkouts()
    val allProgress: Flow<List<UserProgressEntity>> = dao.getAllProgress()
    val recentProgress: Flow<List<UserProgressEntity>> = dao.getRecentProgress(5)
    val customWorkouts: Flow<List<CustomWorkoutEntity>> = dao.getAllCustomWorkouts()
    val distinctExercises: Flow<List<ExerciseEntity>> = dao.getAllDistinctExercises()

    val totalWorkoutsCount: Flow<Int> = dao.getCompletedWorkoutsCount()
    val totalCaloriesBurned: Flow<Int?> = dao.getTotalCaloriesBurned()
    val totalDurationSeconds: Flow<Int?> = dao.getTotalDurationSeconds()

    fun getWorkoutsByCategory(category: String): Flow<List<WorkoutEntity>> {
        return if (category == "All") {
            dao.getAllWorkouts()
        } else {
            dao.getWorkoutsByCategory(category)
        }
    }

    suspend fun getWorkoutById(id: Int): WorkoutEntity? {
        return dao.getWorkoutById(id)
    }

    fun getExercisesForWorkout(workoutId: Int): Flow<List<ExerciseEntity>> {
        return dao.getExercisesForWorkout(workoutId)
    }

    suspend fun getExercisesForWorkoutSync(workoutId: Int): List<ExerciseEntity> {
        return dao.getExercisesForWorkoutSync(workoutId)
    }

    suspend fun saveProgress(progress: UserProgressEntity): Long {
        return dao.insertProgress(progress)
    }

    suspend fun getSetting(key: String, defaultValue: String): String {
        return dao.getSetting(key) ?: defaultValue
    }

    fun observeSetting(key: String): Flow<String?> {
        return dao.observeSetting(key)
    }

    suspend fun setSetting(key: String, value: String) {
        dao.setSetting(SettingEntity(key, value))
    }

    suspend fun addCustomWorkout(workout: CustomWorkoutEntity): Long {
        return dao.insertCustomWorkout(workout)
    }

    suspend fun togglePinCustomWorkout(id: Int, currentPinned: Boolean) {
        dao.updatePinStatus(id, !currentPinned)
    }

    suspend fun getCustomWorkoutById(id: Int): CustomWorkoutEntity? {
        return dao.getCustomWorkoutById(id)
    }

    suspend fun insertWorkout(workout: WorkoutEntity): Long {
        return dao.insertWorkout(workout)
    }

    suspend fun deleteCustomWorkout(id: Int) {
        dao.deleteCustomWorkout(id)
    }

    // Offline Healthy Fitness Meal Guide
    fun getOfflineMealPlans(): List<MealPlanItem> {
        return listOf(
            MealPlanItem(
                id = 1,
                title = "Oatmeal Banana Energy Bowl",
                category = "Pre-Workout",
                calories = 340,
                proteinGrams = 14,
                carbsGrams = 58,
                fatsGrams = 6,
                prepTimeMinutes = 10,
                description = "Complex carbohydrates and natural potassium to sustain energy during bodyweight workouts.",
                ingredients = listOf(
                    "1/2 cup rolled oats",
                    "1 cup unsweetened almond milk or water",
                    "1 ripe banana sliced",
                    "1 tbsp chia seeds",
                    "1 tsp raw honey or cinnamon"
                ),
                tips = "Consume 45–60 minutes before your workout for steady blood glucose without stomach heaviness."
            ),
            MealPlanItem(
                id = 2,
                title = "Grilled Chicken & Quinoa Recovery",
                category = "Post-Workout",
                calories = 480,
                proteinGrams = 42,
                carbsGrams = 45,
                fatsGrams = 11,
                prepTimeMinutes = 20,
                description = "Optimal 3:1 protein-to-carb recovery plate to repair muscle tissue and replenish glycogen stores.",
                ingredients = listOf(
                    "150g grilled chicken breast or tofu",
                    "1 cup cooked fluffy quinoa",
                    "1 cup steamed broccoli and bell peppers",
                    "1 tsp extra virgin olive oil",
                    "Pinch of sea salt and lemon juice"
                ),
                tips = "Eat within 45 minutes after finishing intense push-ups or core training."
            ),
            MealPlanItem(
                id = 3,
                title = "Greek Yogurt Berry Protein Parfait",
                category = "High-Protein",
                calories = 290,
                proteinGrams = 24,
                carbsGrams = 28,
                fatsGrams = 5,
                prepTimeMinutes = 5,
                description = "High casein and whey protein ratio to support overnight muscle recovery and satiety.",
                ingredients = listOf(
                    "200g plain nonfat Greek yogurt",
                    "1/2 cup fresh blueberries or strawberries",
                    "1 tbsp crushed walnuts or almonds",
                    "Dash of cinnamon powder"
                ),
                tips = "Great as a high-protein breakfast or an evening snack before sleep."
            ),
            MealPlanItem(
                id = 4,
                title = "Avocado Egg Sourdough Toast",
                category = "Breakfast",
                calories = 360,
                proteinGrams = 18,
                carbsGrams = 32,
                fatsGrams = 16,
                prepTimeMinutes = 12,
                description = "Healthy monounsaturated fats and choline for hormone balance and cognitive alertness.",
                ingredients = listOf(
                    "1 slice whole grain sourdough bread",
                    "2 poached or hardboiled eggs",
                    "1/2 medium mashed avocado",
                    "Chili flakes and sea salt"
                ),
                tips = "Add baby spinach or cherry tomatoes on top for extra micronutrients."
            ),
            MealPlanItem(
                id = 5,
                title = "Salmon with Sweet Potato Mash",
                category = "Dinner",
                calories = 520,
                proteinGrams = 38,
                carbsGrams = 40,
                fatsGrams = 18,
                prepTimeMinutes = 25,
                description = "Omega-3 rich wild salmon to combat post-workout inflammation and accelerate joint recovery.",
                ingredients = listOf(
                    "140g baked salmon fillet",
                    "1 medium baked sweet potato",
                    "Steamed asparagus or green beans",
                    "Fresh dill and squeeze of lime"
                ),
                tips = "Omega-3 fatty acids reduce muscle soreness (DOMS) after heavy leg days."
            ),
            MealPlanItem(
                id = 6,
                title = "Electrolyte Citrus Hydration Booster",
                category = "Hydration",
                calories = 45,
                proteinGrams = 1,
                carbsGrams = 11,
                fatsGrams = 0,
                prepTimeMinutes = 3,
                description = "Natural home electrolyte replenishment without artificial dyes or processed sugars.",
                ingredients = listOf(
                    "500ml chilled filtered water",
                    "Juice of 1/2 fresh lemon and 1/2 orange",
                    "1/8 tsp pink Himalayan salt",
                    "1 tsp raw honey"
                ),
                tips = "Sip during and after sweaty core or HIIT sessions to restore hydration balance."
            )
        )
    }
}
