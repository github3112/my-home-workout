package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BadgeItem
import com.example.data.model.CustomWorkoutEntity
import com.example.data.model.ExerciseEntity
import com.example.data.model.MealPlanItem
import com.example.data.model.UserProgressEntity
import com.example.data.model.WorkoutEntity
import com.example.data.repository.WorkoutRepository
import com.example.ui.components.SoundHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    TRAINING, MEALS, REPORT, SETTINGS
}

enum class PlayerState {
    COUNTDOWN,
    EXERCISING,
    RESTING,
    PAUSED,
    COMPLETED
}

data class ActiveWorkoutSession(
    val workout: WorkoutEntity,
    val exercises: List<ExerciseEntity>,
    val currentExerciseIndex: Int = 0,
    val playerState: PlayerState = PlayerState.COUNTDOWN,
    val countdownRemaining: Int = 5,
    val exerciseSecondsRemaining: Int = 30,
    val restSecondsRemaining: Int = 25,
    val totalSecondsElapsed: Int = 0,
    val completedExercisesCount: Int = 0,
    val estimatedCaloriesBurned: Int = 0
)

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WorkoutRepository
    val soundHelper: SoundHelper = SoundHelper(application)

    init {
        val db = AppDatabase.getInstance(application)
        repository = WorkoutRepository(db.workoutDao())
        viewModelScope.launch {
            AppDatabase.seedDefaultData(db.workoutDao())
        }
    }

    // App Navigation
    private val _currentTab = MutableStateFlow(AppTab.TRAINING)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _selectedWorkoutForDetail = MutableStateFlow<WorkoutEntity?>(null)
    val selectedWorkoutForDetail: StateFlow<WorkoutEntity?> = _selectedWorkoutForDetail.asStateFlow()

    private val _detailExercises = MutableStateFlow<List<ExerciseEntity>>(emptyList())
    val detailExercises: StateFlow<List<ExerciseEntity>> = _detailExercises.asStateFlow()

    private val _activeSession = MutableStateFlow<ActiveWorkoutSession?>(null)
    val activeSession: StateFlow<ActiveWorkoutSession?> = _activeSession.asStateFlow()

    private val _finishedResult = MutableStateFlow<UserProgressEntity?>(null)
    val finishedResult: StateFlow<UserProgressEntity?> = _finishedResult.asStateFlow()

    private val _inspectingExercise = MutableStateFlow<ExerciseEntity?>(null)
    val inspectingExercise: StateFlow<ExerciseEntity?> = _inspectingExercise.asStateFlow()

    private val _isCreatingCustomWorkout = MutableStateFlow(false)
    val isCreatingCustomWorkout: StateFlow<Boolean> = _isCreatingCustomWorkout.asStateFlow()

    // Filters
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedDifficulty = MutableStateFlow("All")
    val selectedDifficulty: StateFlow<String> = _selectedDifficulty.asStateFlow()

    // Database Flows
    val allWorkouts: StateFlow<List<WorkoutEntity>> = repository.allWorkouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredWorkouts: StateFlow<List<WorkoutEntity>> = combine(
        allWorkouts,
        _selectedCategory,
        _selectedDifficulty
    ) { workouts, cat, diff ->
        workouts.filter { w ->
            (cat == "All" || w.category.equals(cat, ignoreCase = true)) &&
            (diff == "All" || w.difficulty.equals(diff, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentProgress: StateFlow<List<UserProgressEntity>> = repository.recentProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProgress: StateFlow<List<UserProgressEntity>> = repository.allProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalWorkoutsCount: StateFlow<Int> = repository.totalWorkoutsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCalories: StateFlow<Int?> = repository.totalCaloriesBurned
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalDuration: StateFlow<Int?> = repository.totalDurationSeconds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val customWorkouts: StateFlow<List<CustomWorkoutEntity>> = repository.customWorkouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val libraryExercises: StateFlow<List<ExerciseEntity>> = repository.distinctExercises
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Meals
    val mealPlans: List<MealPlanItem> = repository.getOfflineMealPlans()

    // Hydration & Settings
    val waterGlasses = MutableStateFlow(4)
    val dailyWaterTarget = MutableStateFlow(8)
    val soundEnabled = MutableStateFlow(true)
    val voiceEnabled = MutableStateFlow(true)
    val keepScreenOn = MutableStateFlow(true)
    val restDurationSetting = MutableStateFlow(25)
    val countdownSetting = MutableStateFlow(5)
    val userWeightKg = MutableStateFlow(68.0f)
    val userHeightCm = MutableStateFlow(175.0f)

    // Badges & Achievements
    val badges: StateFlow<List<BadgeItem>> = allProgress.map { progressList ->
        val totalCount = progressList.size
        val totalSecs = progressList.sumOf { it.durationSeconds }
        val hasPushUpMaster = progressList.any { it.workoutTitle.contains("Push-Up", ignoreCase = true) }
        val now = System.currentTimeMillis()
        val weekMillis = 7 * 86400000L
        val monthMillis = 30 * 86400000L
        val weekCount = progressList.count { now - it.date <= weekMillis }
        val monthCount = progressList.count { now - it.date <= monthMillis }

        listOf(
            BadgeItem(
                id = "first_step",
                title = "First Step",
                description = "Complete your first workout",
                iconEmoji = "🥇",
                isUnlocked = totalCount >= 1,
                progress = totalCount.coerceAtMost(1),
                maxProgress = 1
            ),
            BadgeItem(
                id = "fire_starter",
                title = "Fire Starter",
                description = "Complete 3 workouts",
                iconEmoji = "🔥",
                isUnlocked = totalCount >= 3,
                progress = totalCount.coerceAtMost(3),
                maxProgress = 3
            ),
            BadgeItem(
                id = "weekly_warrior",
                title = "Weekly Warrior",
                description = "Complete 4 workouts in a week",
                iconEmoji = "🎯",
                isUnlocked = weekCount >= 4,
                progress = weekCount.coerceAtMost(4),
                maxProgress = 4,
                periodType = "Weekly"
            ),
            BadgeItem(
                id = "pushup_master",
                title = "Push-Up Master",
                description = "Conquer the 13-exercise Push-Up Challenge",
                iconEmoji = "🛡️",
                isUnlocked = hasPushUpMaster,
                progress = if (hasPushUpMaster) 1 else 0,
                maxProgress = 1
            ),
            BadgeItem(
                id = "monthly_titan",
                title = "Monthly Titan",
                description = "Complete 10 workouts in a month",
                iconEmoji = "🌟",
                isUnlocked = monthCount >= 10,
                progress = monthCount.coerceAtMost(10),
                maxProgress = 10,
                periodType = "Monthly"
            ),
            BadgeItem(
                id = "endurance_beast",
                title = "Endurance Beast",
                description = "Accumulate 15+ minutes of training",
                iconEmoji = "⏱️",
                isUnlocked = totalSecs >= 900,
                progress = (totalSecs / 60).coerceAtMost(15),
                maxProgress = 15
            ),
            BadgeItem(
                id = "hydration_hero",
                title = "Hydration Hero",
                description = "Drink 8 glasses of water in a day",
                iconEmoji = "💧",
                isUnlocked = waterGlasses.value >= 8,
                progress = waterGlasses.value.coerceAtMost(8),
                maxProgress = 8
            ),
            BadgeItem(
                id = "consistency_streak",
                title = "Century Burner",
                description = "Burn 200+ total calories",
                iconEmoji = "⚡",
                isUnlocked = (progressList.sumOf { it.caloriesBurned }) >= 200,
                progress = progressList.sumOf { it.caloriesBurned }.coerceAtMost(200),
                maxProgress = 200
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weeklyWorkoutsCount: StateFlow<Int> = allProgress.map { list ->
        val weekMillis = 7 * 86400000L
        val now = System.currentTimeMillis()
        list.count { now - it.date <= weekMillis }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    val monthlyWorkoutsCount: StateFlow<Int> = allProgress.map { list ->
        val monthMillis = 30 * 86400000L
        val now = System.currentTimeMillis()
        list.count { now - it.date <= monthMillis }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    val weeklyCalories: StateFlow<Int> = allProgress.map { list ->
        val weekMillis = 7 * 86400000L
        val now = System.currentTimeMillis()
        list.filter { now - it.date <= weekMillis }.sumOf { it.caloriesBurned }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 195)

    val monthlyCalories: StateFlow<Int> = allProgress.map { list ->
        val monthMillis = 30 * 86400000L
        val now = System.currentTimeMillis()
        list.filter { now - it.date <= monthMillis }.sumOf { it.caloriesBurned }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 195)

    // Player Timer Job
    private var timerJob: Job? = null

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setDifficulty(difficulty: String) {
        _selectedDifficulty.value = difficulty
    }

    fun openWorkoutDetail(workout: WorkoutEntity) {
        _selectedWorkoutForDetail.value = workout
        viewModelScope.launch {
            val list = repository.getExercisesForWorkoutSync(workout.id)
            _detailExercises.value = list
        }
    }

    fun closeWorkoutDetail() {
        _selectedWorkoutForDetail.value = null
        _detailExercises.value = emptyList()
    }

    fun openExerciseInfo(exercise: ExerciseEntity) {
        _inspectingExercise.value = exercise
    }

    fun closeExerciseInfo() {
        _inspectingExercise.value = null
    }

    fun openCustomWorkoutCreator() {
        _isCreatingCustomWorkout.value = true
    }

    fun closeCustomWorkoutCreator() {
        _isCreatingCustomWorkout.value = false
    }

    // Workout Player Logic
    fun startWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            val exercises = repository.getExercisesForWorkoutSync(workout.id)
            if (exercises.isEmpty()) return@launch

            val initExercise = exercises[0]
            val countdown = countdownSetting.value

            _activeSession.value = ActiveWorkoutSession(
                workout = workout,
                exercises = exercises,
                currentExerciseIndex = 0,
                playerState = PlayerState.COUNTDOWN,
                countdownRemaining = countdown,
                exerciseSecondsRemaining = if (initExercise.durationSeconds > 0) initExercise.durationSeconds else 30,
                restSecondsRemaining = restDurationSetting.value,
                totalSecondsElapsed = 0,
                completedExercisesCount = 0,
                estimatedCaloriesBurned = 0
            )

            soundHelper.speak("Get ready! First exercise is ${initExercise.name}", voiceEnabled.value)
            startSessionLoop()
        }
    }

    private fun startSessionLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val session = _activeSession.value ?: break

                when (session.playerState) {
                    PlayerState.COUNTDOWN -> {
                        val next = session.countdownRemaining - 1
                        if (next > 0) {
                            if (next <= 3) soundHelper.playBeep(soundEnabled.value)
                            _activeSession.value = session.copy(countdownRemaining = next)
                        } else {
                            // Start exercise
                            soundHelper.playDoubleBeep(soundEnabled.value)
                            soundHelper.speak("Go!", voiceEnabled.value)
                            val currentExercise = session.exercises[session.currentExerciseIndex]
                            val duration = if (currentExercise.durationSeconds > 0) currentExercise.durationSeconds else 30
                            _activeSession.value = session.copy(
                                playerState = PlayerState.EXERCISING,
                                exerciseSecondsRemaining = duration
                            )
                        }
                    }

                    PlayerState.EXERCISING -> {
                        val nextRemaining = session.exerciseSecondsRemaining - 1
                        val nextElapsed = session.totalSecondsElapsed + 1
                        val calories = (nextElapsed * (session.workout.estimatedCalories.toFloat() / session.workout.durationSeconds.coerceAtLeast(60))).toInt()

                        if (nextRemaining > 0) {
                            if (nextRemaining <= 3) soundHelper.playBeep(soundEnabled.value)
                            if (nextRemaining == 15) {
                                soundHelper.speak("Halfway there!", voiceEnabled.value)
                            }
                            _activeSession.value = session.copy(
                                exerciseSecondsRemaining = nextRemaining,
                                totalSecondsElapsed = nextElapsed,
                                estimatedCaloriesBurned = calories
                            )
                        } else {
                            // Completed current exercise
                            soundHelper.playDoubleBeep(soundEnabled.value)
                            val newCompleted = session.completedExercisesCount + 1

                            if (session.currentExerciseIndex + 1 < session.exercises.size) {
                                val nextIndex = session.currentExerciseIndex + 1
                                val nextExercise = session.exercises[nextIndex]
                                soundHelper.speak("Take a rest. Next: ${nextExercise.name}", voiceEnabled.value)
                                soundHelper.vibrateShort()

                                _activeSession.value = session.copy(
                                    playerState = PlayerState.RESTING,
                                    currentExerciseIndex = nextIndex,
                                    restSecondsRemaining = restDurationSetting.value,
                                    completedExercisesCount = newCompleted,
                                    totalSecondsElapsed = nextElapsed,
                                    estimatedCaloriesBurned = calories
                                )
                            } else {
                                // Entire workout completed!
                                finishWorkoutSession(session.copy(
                                    completedExercisesCount = newCompleted,
                                    totalSecondsElapsed = nextElapsed,
                                    estimatedCaloriesBurned = calories
                                ))
                            }
                        }
                    }

                    PlayerState.RESTING -> {
                        val nextRest = session.restSecondsRemaining - 1
                        val nextElapsed = session.totalSecondsElapsed + 1

                        if (nextRest > 0) {
                            if (nextRest <= 3) soundHelper.playBeep(soundEnabled.value)
                            _activeSession.value = session.copy(
                                restSecondsRemaining = nextRest,
                                totalSecondsElapsed = nextElapsed
                            )
                        } else {
                            // Rest ended -> Start next exercise
                            val nextExercise = session.exercises[session.currentExerciseIndex]
                            val duration = if (nextExercise.durationSeconds > 0) nextExercise.durationSeconds else 30
                            soundHelper.playDoubleBeep(soundEnabled.value)
                            soundHelper.speak("Start ${nextExercise.name}!", voiceEnabled.value)

                            _activeSession.value = session.copy(
                                playerState = PlayerState.EXERCISING,
                                exerciseSecondsRemaining = duration,
                                totalSecondsElapsed = nextElapsed
                            )
                        }
                    }

                    PlayerState.PAUSED, PlayerState.COMPLETED -> {
                        // Do nothing while paused
                    }
                }
            }
        }
    }

    fun pauseWorkout() {
        val s = _activeSession.value ?: return
        if (s.playerState == PlayerState.EXERCISING || s.playerState == PlayerState.RESTING) {
            _activeSession.value = s.copy(playerState = PlayerState.PAUSED)
        }
    }

    fun resumeWorkout() {
        val s = _activeSession.value ?: return
        if (s.playerState == PlayerState.PAUSED) {
            _activeSession.value = s.copy(playerState = PlayerState.EXERCISING)
        }
    }

    fun skipExercise() {
        val session = _activeSession.value ?: return
        if (session.currentExerciseIndex + 1 < session.exercises.size) {
            val nextIndex = session.currentExerciseIndex + 1
            val nextExercise = session.exercises[nextIndex]
            val duration = if (nextExercise.durationSeconds > 0) nextExercise.durationSeconds else 30
            _activeSession.value = session.copy(
                currentExerciseIndex = nextIndex,
                playerState = PlayerState.EXERCISING,
                exerciseSecondsRemaining = duration,
                completedExercisesCount = session.completedExercisesCount + 1
            )
            soundHelper.speak("Next: ${nextExercise.name}", voiceEnabled.value)
        } else {
            finishWorkoutSession(session)
        }
    }

    fun previousExercise() {
        val session = _activeSession.value ?: return
        if (session.currentExerciseIndex > 0) {
            val prevIndex = session.currentExerciseIndex - 1
            val prevExercise = session.exercises[prevIndex]
            val duration = if (prevExercise.durationSeconds > 0) prevExercise.durationSeconds else 30
            _activeSession.value = session.copy(
                currentExerciseIndex = prevIndex,
                playerState = PlayerState.EXERCISING,
                exerciseSecondsRemaining = duration
            )
            soundHelper.speak("Repeat: ${prevExercise.name}", voiceEnabled.value)
        }
    }

    fun addRestSeconds(seconds: Int = 20) {
        val session = _activeSession.value ?: return
        if (session.playerState == PlayerState.RESTING) {
            _activeSession.value = session.copy(
                restSecondsRemaining = session.restSecondsRemaining + seconds
            )
        }
    }

    fun skipRest() {
        val session = _activeSession.value ?: return
        if (session.playerState == PlayerState.RESTING) {
            val current = session.exercises[session.currentExerciseIndex]
            val duration = if (current.durationSeconds > 0) current.durationSeconds else 30
            soundHelper.speak("Let's go!", voiceEnabled.value)
            _activeSession.value = session.copy(
                playerState = PlayerState.EXERCISING,
                exerciseSecondsRemaining = duration
            )
        }
    }

    fun stopAndExitWorkout() {
        timerJob?.cancel()
        timerJob = null
        _activeSession.value = null
    }

    private fun finishWorkoutSession(session: ActiveWorkoutSession) {
        timerJob?.cancel()
        timerJob = null

        soundHelper.speak("Workout complete! Outstanding effort!", voiceEnabled.value)
        soundHelper.vibrateShort()

        val progress = UserProgressEntity(
            date = System.currentTimeMillis(),
            workoutId = session.workout.id,
            workoutTitle = session.workout.title,
            exercisesCompleted = session.completedExercisesCount,
            exercisesTotal = session.exercises.size,
            durationSeconds = session.totalSecondsElapsed.coerceAtLeast(30),
            caloriesBurned = session.estimatedCaloriesBurned.coerceAtLeast(15),
            feelingFeedback = "Done / OK"
        )

        _activeSession.value = session.copy(playerState = PlayerState.COMPLETED)
        _finishedResult.value = progress

        viewModelScope.launch {
            repository.saveProgress(progress)
        }
    }

    fun updateResultFeeling(feeling: String) {
        val current = _finishedResult.value ?: return
        val updated = current.copy(feelingFeedback = feeling)
        _finishedResult.value = updated
        viewModelScope.launch {
            repository.saveProgress(updated)
        }
    }

    fun closeResultScreen() {
        _finishedResult.value = null
        _activeSession.value = null
        _selectedWorkoutForDetail.value = null
    }

    // Water tracker
    fun addWaterGlass() {
        if (waterGlasses.value < 20) {
            waterGlasses.value += 1
            viewModelScope.launch {
                repository.setSetting("water_intake_glasses", waterGlasses.value.toString())
            }
        }
    }

    fun removeWaterGlass() {
        if (waterGlasses.value > 0) {
            waterGlasses.value -= 1
            viewModelScope.launch {
                repository.setSetting("water_intake_glasses", waterGlasses.value.toString())
            }
        }
    }

    fun setKeepScreenOn(enabled: Boolean) {
        keepScreenOn.value = enabled
        viewModelScope.launch {
            repository.setSetting("keep_screen_on", enabled.toString())
        }
    }

    fun saveCustomWorkout(
        title: String,
        category: String,
        difficulty: String,
        selectedExercises: List<ExerciseEntity>,
        isPinned: Boolean = false
    ) {
        viewModelScope.launch {
            val names = selectedExercises.joinToString(", ") {
                if (it.reps > 0) "${it.name} (x${it.reps})" else "${it.name} (${it.durationSeconds}s)"
            }

            // Insert as a playable WorkoutEntity in the database
            val workout = WorkoutEntity(
                id = 0,
                originalId = (1000 + System.currentTimeMillis() % 10000).toInt(),
                title = title.ifBlank { "My Custom Routine" },
                description = "Custom workout routine with ${selectedExercises.size} personalized exercises.",
                tags = "custom,$category",
                setCount = 1,
                repCount = selectedExercises.firstOrNull()?.reps ?: 12,
                period = "S60",
                category = category,
                difficulty = difficulty,
                estimatedCalories = (selectedExercises.size * 18).coerceAtLeast(40),
                durationSeconds = selectedExercises.sumOf { if (it.durationSeconds > 0) it.durationSeconds else 30 }
            )
            val workoutId = repository.insertWorkout(workout)

            // Insert child exercises preserving exact reordered sequence
            val mappedExercises = selectedExercises.mapIndexed { idx, ex ->
                ex.copy(id = 0, workoutId = workoutId.toInt(), orderIndex = idx + 1)
            }
            AppDatabase.getInstance(getApplication()).workoutDao().insertExercises(mappedExercises)

            val custom = CustomWorkoutEntity(
                title = title.ifBlank { "My Custom Routine" },
                category = category,
                difficulty = difficulty,
                exercisesJson = names,
                isPinned = isPinned,
                workoutId = workoutId.toInt()
            )
            repository.addCustomWorkout(custom)

            _isCreatingCustomWorkout.value = false
        }
    }

    fun togglePinCustomWorkout(custom: CustomWorkoutEntity) {
        viewModelScope.launch {
            repository.togglePinCustomWorkout(custom.id, custom.isPinned)
        }
    }

    fun startCustomWorkout(custom: CustomWorkoutEntity) {
        viewModelScope.launch {
            val workout = if (custom.workoutId > 0) {
                repository.getWorkoutById(custom.workoutId)
            } else {
                allWorkouts.value.firstOrNull { it.title == custom.title }
            }

            if (workout != null) {
                openWorkoutDetail(workout)
            } else {
                // Fallback: search or use first workout
                allWorkouts.value.firstOrNull()?.let { openWorkoutDetail(it) }
            }
        }
    }

    fun deleteCustomWorkout(id: Int) {
        viewModelScope.launch {
            repository.deleteCustomWorkout(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundHelper.release()
    }
}
