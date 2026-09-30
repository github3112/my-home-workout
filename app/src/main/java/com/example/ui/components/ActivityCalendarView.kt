package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProgressEntity
import com.example.ui.theme.FitnessCyan
import com.example.ui.theme.FitnessGreen
import com.example.ui.theme.FitnessRed
import com.example.ui.theme.WorkoutOrange
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ActivityCalendarView(
    progressList: List<UserProgressEntity>,
    modifier: Modifier = Modifier
) {
    var calendarMonthOffset by remember { mutableStateOf(0) } // 0 = current month, -1 = last month, +1 = next month

    val displayCalendar = remember(calendarMonthOffset) {
        Calendar.getInstance().apply {
            add(Calendar.MONTH, calendarMonthOffset)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val monthName = remember(displayCalendar) {
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(displayCalendar.time)
    }

    val todayCalendar = remember { Calendar.getInstance() }
    val todayYear = todayCalendar.get(Calendar.YEAR)
    val todayMonth = todayCalendar.get(Calendar.MONTH)
    val todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH)

    // Calculate days in month
    val daysInMonth = displayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    // Day of week for 1st day (Calendar.SUNDAY = 1, MONDAY = 2...)
    val firstDayOfWeek = displayCalendar.get(Calendar.DAY_OF_WEEK)
    // Convert to Monday-first index (0 = Mon, 6 = Sun)
    val startOffset = (firstDayOfWeek + 5) % 7

    // Group progress by YYYY-MM-DD
    val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val progressByDay = remember(progressList) {
        progressList.groupBy { dayFormat.format(Date(it.date)) }
    }

    var selectedDateString by remember {
        mutableStateOf(dayFormat.format(todayCalendar.time))
    }

    // Monthly summary stats
    val curYear = displayCalendar.get(Calendar.YEAR)
    val curMonth = displayCalendar.get(Calendar.MONTH)
    val monthlyProgress = remember(progressList, curYear, curMonth) {
        progressList.filter {
            val c = Calendar.getInstance().apply { timeInMillis = it.date }
            c.get(Calendar.YEAR) == curYear && c.get(Calendar.MONTH) == curMonth
        }
    }
    val monthlyActiveDays = remember(monthlyProgress) {
        monthlyProgress.map { dayFormat.format(Date(it.date)) }.distinct().size
    }
    val monthlyCalories = remember(monthlyProgress) {
        monthlyProgress.sumOf { it.caloriesBurned }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF222836)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("activity_calendar_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Month Navigation Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendar",
                        tint = WorkoutOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = monthName.uppercase(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { calendarMonthOffset -= 1 },
                        modifier = Modifier.size(32.dp).testTag("cal_prev_month")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Month",
                            tint = Color(0xFF94A3B8)
                        )
                    }

                    if (calendarMonthOffset != 0) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF2D3547),
                            modifier = Modifier.clickable { calendarMonthOffset = 0 }
                        ) {
                            Text(
                                text = "Today",
                                fontSize = 11.sp,
                                color = WorkoutOrange,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { calendarMonthOffset += 1 },
                        modifier = Modifier.size(32.dp).testTag("cal_next_month")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Month",
                            tint = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Month summary pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF191F2C), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "${monthlyProgress.size} Workouts",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = WorkoutOrange
                )
                Text(
                    text = "$monthlyActiveDays Active Days",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FitnessCyan
                )
                Text(
                    text = "$monthlyCalories kcal",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FitnessGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Day of Week Labels (Mon - Sun)
            val weekDays = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
            Row(modifier = Modifier.fillMaxWidth()) {
                weekDays.forEach { dayName ->
                    Text(
                        text = dayName,
                        color = Color(0xFF64748B),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Days Grid
            val totalCells = startOffset + daysInMonth
            val totalRows = (totalCells + 6) / 7

            for (row in 0 until totalRows) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNum = cellIndex - startOffset + 1

                        if (dayNum in 1..daysInMonth) {
                            val checkCal = Calendar.getInstance().apply {
                                set(curYear, curMonth, dayNum)
                            }
                            val dateKey = dayFormat.format(checkCal.time)
                            val daySessions = progressByDay[dateKey] ?: emptyList()
                            val hasActivity = daySessions.isNotEmpty()
                            val isToday = (curYear == todayYear && curMonth == todayMonth && dayNum == todayDay)
                            val isSelected = selectedDateString == dateKey

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> WorkoutOrange
                                            hasActivity -> Color(0xFFE65100).copy(alpha = 0.35f)
                                            isToday -> Color(0xFF2C3549)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .border(
                                        width = if (isToday && !isSelected) 1.5.dp else if (hasActivity && !isSelected) 1.dp else 0.dp,
                                        color = if (isToday && !isSelected) FitnessCyan else if (hasActivity && !isSelected) WorkoutOrange.copy(alpha = 0.6f) else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedDateString = dateKey },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "$dayNum",
                                        fontSize = 13.sp,
                                        fontWeight = if (hasActivity || isToday || isSelected) FontWeight.Black else FontWeight.Normal,
                                        color = when {
                                            isSelected -> Color.Black
                                            hasActivity -> WorkoutOrange
                                            isToday -> FitnessCyan
                                            else -> Color.White
                                        }
                                    )

                                    if (hasActivity && !isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .background(WorkoutOrange, CircleShape)
                                        )
                                    }
                                }
                            }
                        } else {
                            // Blank filler
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Selected Day Activities List
            val selectedSessions = progressByDay[selectedDateString] ?: emptyList()

            val displaySelectedDate = remember(selectedDateString) {
                try {
                    val date = dayFormat.parse(selectedDateString)
                    SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault()).format(date ?: Date())
                } catch (_: Exception) {
                    selectedDateString
                }
            }

            Text(
                text = "ACTIVITIES ON $displaySelectedDate",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (selectedSessions.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1A202D),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "No Activities",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "No workouts on this date. Select highlighted days to view sessions!",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    selectedSessions.forEach { session ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF2B2133),
                            border = androidx.compose.foundation.BorderStroke(1.dp, WorkoutOrange.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = WorkoutOrange,
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.FitnessCenter,
                                                contentDescription = "Workout",
                                                tint = Color.Black,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            text = session.workoutTitle,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${session.exercisesCompleted}/${session.exercisesTotal} moves • ${session.feelingFeedback}",
                                            fontSize = 11.sp,
                                            color = Color(0xFFCBD5E1)
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${session.caloriesBurned} kcal",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = WorkoutOrange
                                    )
                                    Text(
                                        text = "${session.durationSeconds / 60}m ${session.durationSeconds % 60}s",
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
