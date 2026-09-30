package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CustomWorkoutEntity
import com.example.data.model.ExerciseEntity
import com.example.data.model.SettingEntity
import com.example.data.model.UserProgressEntity
import com.example.data.model.WorkoutEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        WorkoutEntity::class,
        ExerciseEntity::class,
        UserProgressEntity::class,
        SettingEntity::class,
        CustomWorkoutEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "home_workout.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        seedDefaultData(database.workoutDao())
                    }
                }
            }
        }

        suspend fun seedDefaultData(dao: WorkoutDao) {
            // 1. Initial default settings
            dao.setSetting(SettingEntity("sound_enabled", "true"))
            dao.setSetting(SettingEntity("voice_guidance", "true"))
            dao.setSetting(SettingEntity("rest_duration_sec", "25"))
            dao.setSetting(SettingEntity("countdown_sec", "5"))
            dao.setSetting(SettingEntity("water_intake_glasses", "4"))
            dao.setSetting(SettingEntity("daily_water_target", "8"))
            dao.setSetting(SettingEntity("user_weight_kg", "68.5"))
            dao.setSetting(SettingEntity("user_height_cm", "175"))
            dao.setSetting(SettingEntity("weekly_goal_days", "4"))

            // 2. Default Workout Plans
            val workouts = listOf(
                WorkoutEntity(
                    id = 1,
                    originalId = 1,
                    title = "7-Minute Full Body Sprint",
                    description = "High-intensity full-body routine designed to jumpstart metabolism and burn calories fast without equipment.",
                    tags = "fullbody,sprint,hiit,fatburn",
                    setCount = 1,
                    repCount = 12,
                    period = "S60",
                    category = "Full Body",
                    difficulty = "Medium",
                    estimatedCalories = 110,
                    durationSeconds = 420
                ),
                WorkoutEntity(
                    id = 2,
                    originalId = 10,
                    title = "Six Pack Core Sculpt",
                    description = "Target all areas of your core: upper abs, lower belly, and obliques for a rock-solid, defined midsection.",
                    tags = "abs,core,sixpack",
                    setCount = 1,
                    repCount = 15,
                    period = "30s",
                    category = "Abs",
                    difficulty = "Easy",
                    estimatedCalories = 85,
                    durationSeconds = 360
                ),
                WorkoutEntity(
                    id = 3,
                    originalId = 20,
                    title = "Abs Hardcore Blast",
                    description = "Challenging core workout with sustained isometric holds and explosive contraction to shred belly fat.",
                    tags = "abs,hardcore,burn",
                    setCount = 1,
                    repCount = 20,
                    period = "45s",
                    category = "Abs",
                    difficulty = "Hard",
                    estimatedCalories = 140,
                    durationSeconds = 540
                ),
                WorkoutEntity(
                    id = 4,
                    originalId = 30,
                    title = "Chest & Triceps Beginner",
                    description = "Build upper-body pushing power, chest definition, and tricep tone using progressive push-up variations.",
                    tags = "chest,triceps,pushups,arms",
                    setCount = 1,
                    repCount = 12,
                    period = "35s",
                    category = "Chest",
                    difficulty = "Easy",
                    estimatedCalories = 95,
                    durationSeconds = 380
                ),
                WorkoutEntity(
                    id = 5,
                    originalId = 40,
                    title = "Chest Mastery & Power",
                    description = "Intense push-up combinations that recruit upper, middle, and lower pecs for massive chest volume.",
                    tags = "chest,strength,hypertrophy",
                    setCount = 2,
                    repCount = 15,
                    period = "45s",
                    category = "Chest",
                    difficulty = "Hard",
                    estimatedCalories = 160,
                    durationSeconds = 600
                ),
                WorkoutEntity(
                    id = 6,
                    originalId = 55,
                    title = "Arm Definition & Biceps-Triceps",
                    description = "Sculpt lean and toned arms with diamond presses, dips, arm circles, and tension holds.",
                    tags = "arms,biceps,triceps,tone",
                    setCount = 1,
                    repCount = 14,
                    period = "35s",
                    category = "Arms",
                    difficulty = "Medium",
                    estimatedCalories = 105,
                    durationSeconds = 420
                ),
                WorkoutEntity(
                    id = 7,
                    originalId = 85,
                    title = "Legs & Glutes Power",
                    description = "Strengthen quads, hamstrings, calves, and glutes with bodyweight squats, lunges, and calf pulses.",
                    tags = "legs,glutes,quads,squats",
                    setCount = 1,
                    repCount = 16,
                    period = "40s",
                    category = "Legs",
                    difficulty = "Medium",
                    estimatedCalories = 135,
                    durationSeconds = 480
                ),
                WorkoutEntity(
                    id = 8,
                    originalId = 120,
                    title = "Shoulder & Upper Back Posture",
                    description = "Open tight chest muscles, strengthen upper back rhomboids and rear delts to fix posture and relieve strain.",
                    tags = "shoulder,back,posture,relief",
                    setCount = 1,
                    repCount = 12,
                    period = "30s",
                    category = "Shoulder",
                    difficulty = "Easy",
                    estimatedCalories = 80,
                    durationSeconds = 350
                ),
                WorkoutEntity(
                    id = 9,
                    originalId = 155,
                    title = "Back & Core Alignment",
                    description = "Strengthen the spine, lower back, and deep stabilizing muscles to prevent back pain and enhance stability.",
                    tags = "back,lats,pain_relief,stability",
                    setCount = 1,
                    repCount = 12,
                    period = "35s",
                    category = "Back",
                    difficulty = "Medium",
                    estimatedCalories = 90,
                    durationSeconds = 380
                ),
                WorkoutEntity(
                    id = 10,
                    originalId = 200,
                    title = "Push-Up & Upper Body Mastery",
                    description = "Comprehensive 13-exercise push-up and chest challenge: jumping jacks, arm circles, shoulder stretches, staggered, rotation, diamond, box, Spiderman, decline, incline, and cool down stretches.",
                    tags = "chest,pushups,arms,shoulders,challenge",
                    setCount = 1,
                    repCount = 16,
                    period = "S60",
                    category = "Chest",
                    difficulty = "Hard",
                    estimatedCalories = 210,
                    durationSeconds = 660
                )
            )
            dao.insertWorkouts(workouts)

            // 3. Exercises for each workout
            val exercises = mutableListOf<ExerciseEntity>()

            // Workout 1: 7-Minute Full Body Sprint (7 exercises)
            exercises.add(ExerciseEntity(workoutId = 1, originalId = "act_101", name = "Jumping Jacks", description = "Jump your feet out wide while clapping arms overhead, then jump back to starting position. Maintain light rhythm.", targetMuscle = "Full Body & Cardio", durationSeconds = 30, reps = 0, animationType = "jumping_jacks", intensity = "Medium", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 1, originalId = "act_102", name = "Classic Push-Ups", description = "Keep body rigid in a straight line, lower chest until elbows reach 90 degrees, press powerfully back up.", targetMuscle = "Chest & Triceps", durationSeconds = 30, reps = 12, animationType = "pushups", intensity = "Medium", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 1, originalId = "act_103", name = "Air Squats", description = "Stand with feet shoulder-width apart, lower hips down and back as if sitting in an imaginary chair, then drive through heels.", targetMuscle = "Quads & Glutes", durationSeconds = 30, reps = 15, animationType = "squats", intensity = "Medium", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 1, originalId = "act_104", name = "High Knees", description = "Jog in place rapidly lifting knees up to hip height with chest lifted and core engaged.", targetMuscle = "Cardio & Hip Flexors", durationSeconds = 30, reps = 0, animationType = "high_knees", intensity = "Hard", orderIndex = 4))
            exercises.add(ExerciseEntity(workoutId = 1, originalId = "act_105", name = "Abdominal Crunches", description = "Lie on back with knees bent, curl shoulders toward hips squeezing upper abdominals without pulling on neck.", targetMuscle = "Upper Abs", durationSeconds = 30, reps = 15, animationType = "crunches", intensity = "Easy", orderIndex = 5))
            exercises.add(ExerciseEntity(workoutId = 1, originalId = "act_106", name = "Walking Lunges", description = "Step forward with one leg, bend both knees to 90 degrees, return and alternate legs steadily.", targetMuscle = "Legs & Glutes", durationSeconds = 30, reps = 14, animationType = "lunges", intensity = "Medium", orderIndex = 6))
            exercises.add(ExerciseEntity(workoutId = 1, originalId = "act_107", name = "Forearm Plank", description = "Hold a straight-line bridge supported on forearms and toes. Breathe steadily and brace abs tightly.", targetMuscle = "Deep Core & Abs", durationSeconds = 35, reps = 0, animationType = "plank", intensity = "Medium", orderIndex = 7))

            // Workout 2: Six Pack Core Sculpt (6 exercises)
            exercises.add(ExerciseEntity(workoutId = 2, originalId = "act_201", name = "Abdominal Crunches", description = "Focus on rib-to-hip contraction. Keep chin off your chest and exhale as you crunch up.", targetMuscle = "Upper Abs", durationSeconds = 30, reps = 16, animationType = "crunches", intensity = "Easy", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 2, originalId = "act_202", name = "Bicycle Crunches", description = "Rotate torso bringing elbow toward opposite knee while extending other leg straight. Switch side-to-side.", targetMuscle = "Obliques & Core", durationSeconds = 30, reps = 18, animationType = "bicycle_crunches", intensity = "Medium", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 2, originalId = "act_203", name = "Lying Leg Raises", description = "Keep lower back pressed down, elevate legs upward to vertical, then lower them slowly without touching the floor.", targetMuscle = "Lower Abs", durationSeconds = 30, reps = 12, animationType = "leg_raises", intensity = "Medium", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 2, originalId = "act_204", name = "Mountain Climbers", description = "From high plank position, drive alternating knees toward chest in a rhythmic running cadence.", targetMuscle = "Abs & Stamina", durationSeconds = 30, reps = 0, animationType = "mountain_climbers", intensity = "Medium", orderIndex = 4))
            exercises.add(ExerciseEntity(workoutId = 2, originalId = "act_205", name = "Forearm Plank", description = "Maintain a flat spine, squeeze glutes, pull belly button inward toward spine.", targetMuscle = "Core Stability", durationSeconds = 35, reps = 0, animationType = "plank", intensity = "Medium", orderIndex = 5))
            exercises.add(ExerciseEntity(workoutId = 2, originalId = "act_206", name = "Cobra Stretch", description = "Lie prone, press hands into mat to gently lift chest and stretch abdominal wall. Breathe deeply.", targetMuscle = "Abdominal Release", durationSeconds = 25, reps = 0, animationType = "cobra_stretch", intensity = "Easy", orderIndex = 6))

            // Workout 3: Abs Hardcore Blast (6 exercises)
            exercises.add(ExerciseEntity(workoutId = 3, originalId = "act_301", name = "Mountain Climbers", description = "Fast cadence, keep hips low, drive knees right under your chest.", targetMuscle = "Core & Cardio", durationSeconds = 40, reps = 0, animationType = "mountain_climbers", intensity = "Hard", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 3, originalId = "act_302", name = "Lying Leg Raises", description = "Control the eccentric descent, keep lower back flat against the floor.", targetMuscle = "Lower Abs", durationSeconds = 35, reps = 16, animationType = "leg_raises", intensity = "Hard", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 3, originalId = "act_303", name = "Bicycle Crunches", description = "Maximize torso rotation, pause 1 second on each peak contraction.", targetMuscle = "Obliques", durationSeconds = 40, reps = 24, animationType = "bicycle_crunches", intensity = "Hard", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 3, originalId = "act_304", name = "Forearm Plank", description = "Endurance core hold. Do not let lower back sag.", targetMuscle = "Core & Stabilizers", durationSeconds = 50, reps = 0, animationType = "plank", intensity = "Hard", orderIndex = 4))
            exercises.add(ExerciseEntity(workoutId = 3, originalId = "act_305", name = "Abdominal Crunches", description = "High rep burnout to exhaust upper rectus abdominis.", targetMuscle = "Upper Abs", durationSeconds = 35, reps = 20, animationType = "crunches", intensity = "Medium", orderIndex = 5))
            exercises.add(ExerciseEntity(workoutId = 3, originalId = "act_306", name = "Cobra Stretch", description = "Relax and lengthen the abdominal muscles while arching spine gently.", targetMuscle = "Cool Down", durationSeconds = 30, reps = 0, animationType = "cobra_stretch", intensity = "Easy", orderIndex = 6))

            // Workout 4: Chest & Triceps Beginner (5 exercises)
            exercises.add(ExerciseEntity(workoutId = 4, originalId = "act_401", name = "Jumping Jacks", description = "Warm up shoulder joints and raise heart rate.", targetMuscle = "Warmup", durationSeconds = 30, reps = 0, animationType = "jumping_jacks", intensity = "Easy", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 4, originalId = "act_402", name = "Classic Push-Ups", description = "Shoulder-width hand placement, keep elbows tucked at 45 degrees.", targetMuscle = "Chest & Shoulders", durationSeconds = 30, reps = 10, animationType = "pushups", intensity = "Medium", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 4, originalId = "act_403", name = "Wide Arm Push-Ups", description = "Hands placed wider than shoulder width to emphasize outer chest fibers.", targetMuscle = "Pecs", durationSeconds = 30, reps = 10, animationType = "pushups", intensity = "Medium", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 4, originalId = "act_404", name = "Chair Tricep Dips", description = "Rest hands on chair or edge of couch, lower hips downward bending elbows, press up through palms.", targetMuscle = "Triceps", durationSeconds = 30, reps = 12, animationType = "tricep_dips", intensity = "Medium", orderIndex = 4))
            exercises.add(ExerciseEntity(workoutId = 4, originalId = "act_405", name = "Cobra Stretch", description = "Stretch chest and front delts while breathing smoothly.", targetMuscle = "Chest Stretch", durationSeconds = 25, reps = 0, animationType = "cobra_stretch", intensity = "Easy", orderIndex = 5))

            // Workout 5: Chest Mastery & Power (6 exercises)
            exercises.add(ExerciseEntity(workoutId = 5, originalId = "act_501", name = "Classic Push-Ups", description = "Strict cadence with 2-second descent and explosive push.", targetMuscle = "Pecs & Triceps", durationSeconds = 35, reps = 15, animationType = "pushups", intensity = "Hard", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 5, originalId = "act_502", name = "Diamond Push-Ups", description = "Place thumbs and index fingers together to form a triangle under your chest.", targetMuscle = "Triceps & Inner Pecs", durationSeconds = 30, reps = 10, animationType = "pushups", intensity = "Hard", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 5, originalId = "act_503", name = "Wide Arm Push-Ups", description = "Maximize pec stretch at bottom of repetition.", targetMuscle = "Outer Chest", durationSeconds = 35, reps = 12, animationType = "pushups", intensity = "Hard", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 5, originalId = "act_504", name = "Chair Tricep Dips", description = "Full range of motion, squeeze triceps at top extension.", targetMuscle = "Triceps", durationSeconds = 30, reps = 15, animationType = "tricep_dips", intensity = "Medium", orderIndex = 4))
            exercises.add(ExerciseEntity(workoutId = 5, originalId = "act_505", name = "Forearm Plank", description = "Maintain tension throughout pectoral and serratus anterior muscles.", targetMuscle = "Core & Chest Stability", durationSeconds = 40, reps = 0, animationType = "plank", intensity = "Medium", orderIndex = 5))
            exercises.add(ExerciseEntity(workoutId = 5, originalId = "act_506", name = "Cobra Stretch", description = "Deep chest and shoulder flexibility release.", targetMuscle = "Release", durationSeconds = 25, reps = 0, animationType = "cobra_stretch", intensity = "Easy", orderIndex = 6))

            // Workout 6: Arm Definition (5 exercises)
            exercises.add(ExerciseEntity(workoutId = 6, originalId = "act_601", name = "Arm Circles", description = "Extend arms straight to sides, make controlled small rotations forward then backward.", targetMuscle = "Shoulders & Biceps", durationSeconds = 30, reps = 0, animationType = "arm_circles", intensity = "Easy", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 6, originalId = "act_602", name = "Diamond Push-Ups", description = "Close-grip push-up isolating the triceps brachii.", targetMuscle = "Triceps", durationSeconds = 30, reps = 10, animationType = "pushups", intensity = "Hard", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 6, originalId = "act_603", name = "Chair Tricep Dips", description = "Control descent, do not flare shoulders forward.", targetMuscle = "Triceps", durationSeconds = 30, reps = 14, animationType = "tricep_dips", intensity = "Medium", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 6, originalId = "act_604", name = "Classic Push-Ups", description = "Smooth rhythmic pushing for arm and shoulder endurance.", targetMuscle = "Arms & Chest", durationSeconds = 30, reps = 12, animationType = "pushups", intensity = "Medium", orderIndex = 4))
            exercises.add(ExerciseEntity(workoutId = 6, originalId = "act_605", name = "Forearm Plank", description = "Isometric shoulder and arm stabilization.", targetMuscle = "Forearms & Core", durationSeconds = 30, reps = 0, animationType = "plank", intensity = "Medium", orderIndex = 5))

            // Workout 7: Legs & Glutes Power (5 exercises)
            exercises.add(ExerciseEntity(workoutId = 7, originalId = "act_701", name = "Air Squats", description = "Deep squats keeping weight back on heels, knees tracking over toes.", targetMuscle = "Quads & Glutes", durationSeconds = 35, reps = 18, animationType = "squats", intensity = "Medium", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 7, originalId = "act_702", name = "Walking Lunges", description = "Long strides, sink back knee toward floor, drive forward through front heel.", targetMuscle = "Hamstrings & Glutes", durationSeconds = 35, reps = 16, animationType = "lunges", intensity = "Medium", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 7, originalId = "act_703", name = "High Knees", description = "Explosive triple extension of ankle, knee, and hip.", targetMuscle = "Calves & Cardio", durationSeconds = 30, reps = 0, animationType = "high_knees", intensity = "Hard", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 7, originalId = "act_704", name = "Air Squats", description = "Tempo squats with 2-second hold at parallel depth.", targetMuscle = "Leg Burnout", durationSeconds = 30, reps = 15, animationType = "squats", intensity = "Medium", orderIndex = 4))
            exercises.add(ExerciseEntity(workoutId = 7, originalId = "act_705", name = "Jumping Jacks", description = "Light bounce to shake out lactic acid and cool down leg muscles.", targetMuscle = "Calves & Cool Down", durationSeconds = 25, reps = 0, animationType = "jumping_jacks", intensity = "Easy", orderIndex = 5))

            // Workout 8: Shoulder & Upper Back Posture (4 exercises)
            exercises.add(ExerciseEntity(workoutId = 8, originalId = "act_801", name = "Arm Circles", description = "Wide circular rotations activating anterior, lateral, and posterior deltoids.", targetMuscle = "Deltoids", durationSeconds = 35, reps = 0, animationType = "arm_circles", intensity = "Easy", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 8, originalId = "act_802", name = "Classic Push-Ups", description = "Focus on protracting and retracting shoulder blades with good control.", targetMuscle = "Shoulders & Serratus", durationSeconds = 30, reps = 10, animationType = "pushups", intensity = "Medium", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 8, originalId = "act_803", name = "Forearm Plank", description = "Press actively through forearms to push floor away and activate scapular muscles.", targetMuscle = "Upper Back & Core", durationSeconds = 35, reps = 0, animationType = "plank", intensity = "Medium", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 8, originalId = "act_804", name = "Cobra Stretch", description = "Gentle thoracic extension to counteract slouching.", targetMuscle = "Upper Spine Stretch", durationSeconds = 30, reps = 0, animationType = "cobra_stretch", intensity = "Easy", orderIndex = 4))

            // Workout 9: Back & Core Alignment (4 exercises)
            exercises.add(ExerciseEntity(workoutId = 9, originalId = "act_901", name = "Forearm Plank", description = "Neutral spine hold to reinforce lumbar stability.", targetMuscle = "Core & Erector Spinae", durationSeconds = 40, reps = 0, animationType = "plank", intensity = "Medium", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 9, originalId = "act_902", name = "Lying Leg Raises", description = "Stabilize lumbar region by pressing pelvis into floor throughout movement.", targetMuscle = "Lower Core & Back", durationSeconds = 30, reps = 12, animationType = "leg_raises", intensity = "Medium", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 9, originalId = "act_903", name = "Cobra Stretch", description = "Stretches anterior chain and decompresses spinal discs.", targetMuscle = "Spinal Decompression", durationSeconds = 35, reps = 0, animationType = "cobra_stretch", intensity = "Easy", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 9, originalId = "act_904", name = "Mountain Climbers", description = "Rhythmic hip flexion while maintaining solid upper back posture.", targetMuscle = "Posterior Chain", durationSeconds = 30, reps = 0, animationType = "mountain_climbers", intensity = "Medium", orderIndex = 4))

            // Workout 10: Push-Up & Upper Body Mastery (13 exercises)
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1001", name = "Jumping Jack", description = "Jump feet wide while raising arms overhead, maintaining steady cadence.", targetMuscle = "Warmup & Full Body", durationSeconds = 30, reps = 30, animationType = "jumping_jacks", intensity = "Medium", orderIndex = 1))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1002", name = "Arm Circles", description = "Extend arms straight out to sides and draw controlled circles.", targetMuscle = "Shoulders & Rotator Cuff", durationSeconds = 25, reps = 20, animationType = "arm_circles", intensity = "Easy", orderIndex = 2))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1003", name = "Shoulder Stretch", description = "Pull one arm across chest with opposite hand, holding steady stretch.", targetMuscle = "Deltoid & Upper Back", durationSeconds = 30, reps = 0, animationType = "shoulder_stretch", intensity = "Easy", orderIndex = 3))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1004", name = "Staggered Pushup", description = "Place one hand forward and other hand backward, press firmly through palms.", targetMuscle = "Chest & Core", durationSeconds = 35, reps = 16, animationType = "staggered_pushup", intensity = "Hard", orderIndex = 4))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1005", name = "Pushup and Rotation", description = "Perform standard push-up, then rotate torso into a side T-plank with arm reaching upward.", targetMuscle = "Chest, Shoulders & Obliques", durationSeconds = 35, reps = 12, animationType = "pushup_and_rotation", intensity = "Hard", orderIndex = 5))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1006", name = "Diamond Pushup", description = "Form a triangle diamond with hands under your chest and keep elbows close.", targetMuscle = "Triceps & Inner Pecs", durationSeconds = 35, reps = 16, animationType = "diamond_pushup", intensity = "Hard", orderIndex = 6))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1007", name = "Box Pushup", description = "Kneel with hips directly above knees in box position, lower chest with control.", targetMuscle = "Chest & Triceps", durationSeconds = 30, reps = 12, animationType = "box_pushup", intensity = "Medium", orderIndex = 7))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1008", name = "Spiderman Pushup", description = "As you lower your chest, bring one knee out to the side toward your elbow.", targetMuscle = "Chest & Obliques", durationSeconds = 40, reps = 20, animationType = "spiderman_pushup", intensity = "Hard", orderIndex = 8))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1009", name = "Decline Pushup", description = "Elevate feet on bench or platform, lower chest to floor and push up.", targetMuscle = "Upper Chest & Shoulders", durationSeconds = 35, reps = 12, animationType = "decline_pushup", intensity = "Hard", orderIndex = 9))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1010", name = "Incline Pushup", description = "Place hands on elevated platform, maintain straight rigid core and press up.", targetMuscle = "Lower Chest & Arms", durationSeconds = 30, reps = 12, animationType = "incline_pushup", intensity = "Medium", orderIndex = 10))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1011", name = "Shoulder Stretch", description = "Release tension in anterior and lateral deltoids with cross-body arm hold.", targetMuscle = "Deltoids", durationSeconds = 20, reps = 0, animationType = "shoulder_stretch", intensity = "Easy", orderIndex = 11))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1012", name = "Cobra Stretch", description = "Lie prone, press hands down to lift chest and expand ribcage.", targetMuscle = "Chest & Abdominals", durationSeconds = 20, reps = 0, animationType = "cobra_stretch", intensity = "Easy", orderIndex = 12))
            exercises.add(ExerciseEntity(workoutId = 10, originalId = "act_1013", name = "Chest Stretch", description = "Clasp hands behind back, roll shoulders back and breathe deeply to expand chest.", targetMuscle = "Pectoral & Biceps", durationSeconds = 20, reps = 20, animationType = "chest_stretch", intensity = "Easy", orderIndex = 13))

            dao.insertExercises(exercises)

            // Seed initial sample workout completion in progress for realistic stats
            val now = System.currentTimeMillis()
            val dayMillis = 86400000L
            dao.insertProgress(
                UserProgressEntity(
                    date = now - (dayMillis * 2),
                    workoutId = 1,
                    workoutTitle = "7-Minute Full Body Sprint",
                    exercisesCompleted = 7,
                    exercisesTotal = 7,
                    durationSeconds = 420,
                    caloriesBurned = 110,
                    feelingFeedback = "Sprint (S60)"
                )
            )
            dao.insertProgress(
                UserProgressEntity(
                    date = now - dayMillis,
                    workoutId = 2,
                    workoutTitle = "Six Pack Core Sculpt",
                    exercisesCompleted = 6,
                    exercisesTotal = 6,
                    durationSeconds = 360,
                    caloriesBurned = 85,
                    feelingFeedback = "Challenging"
                )
            )

            // Seed initial sample pinned custom routine
            dao.insertCustomWorkout(
                CustomWorkoutEntity(
                    id = 1,
                    title = "My Daily Core & Push Blast",
                    category = "Full Body",
                    difficulty = "Medium",
                    exercisesJson = "Jumping Jack (x30), Classic Push-Ups (x15), Forearm Plank (35s)",
                    isPinned = true,
                    workoutId = 1
                )
            )
        }
    }
}
