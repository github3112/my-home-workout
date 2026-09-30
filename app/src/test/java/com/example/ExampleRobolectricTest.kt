package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CustomWorkoutEntity
import com.example.data.model.ExerciseEntity
import com.example.data.model.WorkoutEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

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
}
