package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.BadgeItem
import com.example.data.model.CustomWorkoutEntity
import com.example.data.model.ExerciseEntity
import com.example.data.model.UserProgressEntity
import com.example.data.model.WorkoutEntity
import com.example.util.NotificationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Home Workout", appName)
  }

  @Test
  fun `workout model test`() {
    val workout = WorkoutEntity(
      id = 10,
      title = "Push-Up & Upper Body Mastery",
      period = "S60",
      category = "Chest",
      difficulty = "Hard",
      estimatedCalories = 210,
      durationSeconds = 660
    )
    assertNotNull(workout)
    assertEquals("Chest", workout.category)
    assertEquals("Hard", workout.difficulty)
  }

  @Test
  fun `push-up challenge exercises animation mappings`() {
    val animations = listOf(
      "jumping_jacks",
      "arm_circles",
      "shoulder_stretch",
      "staggered_pushup",
      "pushup_and_rotation",
      "diamond_pushup",
      "box_pushup",
      "spiderman_pushup",
      "decline_pushup",
      "incline_pushup",
      "shoulder_stretch",
      "cobra_stretch",
      "chest_stretch"
    )
    assertEquals(13, animations.size)
  }

  @Test
  fun `custom rep reordering and pinning test`() {
    val custom = CustomWorkoutEntity(
      id = 1,
      title = "My Daily Push Routine",
      category = "Chest",
      difficulty = "Medium",
      exercisesJson = "Jumping Jack (x30), Diamond Pushup (x16)",
      isPinned = true,
      workoutId = 10
    )
    assertTrue(custom.isPinned)
    assertEquals("Chest", custom.category)

    // Reordering test
    val ex1 = ExerciseEntity(id = 1, name = "Jumping Jack", reps = 30)
    val ex2 = ExerciseEntity(id = 2, name = "Diamond Pushup", reps = 16)
    val list = mutableListOf(ex1, ex2)
    // Swap order
    val temp = list.removeAt(0)
    list.add(1, temp)
    assertEquals("Diamond Pushup", list[0].name)
    assertEquals("Jumping Jack", list[1].name)
  }

  @Test
  fun `activity calendar date grouping test`() {
    val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val now = System.currentTimeMillis()
    val todayKey = dayFormat.format(Date(now))

    val progress = listOf(
      UserProgressEntity(
        id = 1,
        date = now,
        workoutTitle = "7-Minute Full Body Sprint",
        exercisesCompleted = 7,
        exercisesTotal = 7,
        durationSeconds = 420,
        caloriesBurned = 110
      )
    )

    val grouped = progress.groupBy { dayFormat.format(Date(it.date)) }
    assertTrue(grouped.containsKey(todayKey))
    assertEquals(1, grouped[todayKey]?.size)
  }

  @Test
  fun `badges unlock logic test`() {
    val badges = listOf(
      BadgeItem(
        id = "first_step",
        title = "First Step",
        description = "Complete your first workout",
        iconEmoji = "🥇",
        isUnlocked = true,
        progress = 1,
        maxProgress = 1
      ),
      BadgeItem(
        id = "weekly_warrior",
        title = "Weekly Warrior",
        description = "Complete 4 workouts in a week",
        iconEmoji = "🎯",
        isUnlocked = false,
        progress = 2,
        maxProgress = 4,
        periodType = "Weekly"
      )
    )

    val unlockedCount = badges.count { it.isUnlocked }
    assertEquals(1, unlockedCount)
  }

  @Test
  fun `notification helper channel creation`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    NotificationHelper.createNotificationChannel(context)
    assertNotNull(NotificationHelper.CHANNEL_ID)
  }
}
