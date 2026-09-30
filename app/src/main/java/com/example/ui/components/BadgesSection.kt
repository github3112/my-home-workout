package com.example.ui.components

import android.content.Context
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
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BadgeItem
import com.example.ui.theme.FitnessCyan
import com.example.ui.theme.FitnessGreen
import com.example.ui.theme.WorkoutOrange
import com.example.util.NotificationHelper

@Composable
fun BadgesSection(
    badges: List<BadgeItem>,
    weeklyWorkoutsCount: Int,
    monthlyWorkoutsCount: Int,
    weeklyCalories: Int,
    monthlyCalories: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeRecapDialog by remember { mutableStateOf<String?>(null) } // "weekly" or "monthly" or null
    var selectedBadgeDetail by remember { mutableStateOf<BadgeItem?>(null) }

    val unlockedCount = badges.count { it.isUnlocked }

    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Badges",
                    tint = WorkoutOrange,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "ACHIEVEMENT BADGES ($unlockedCount/${badges.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = WorkoutOrange,
                    letterSpacing = 1.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = WorkoutOrange.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "${(unlockedCount * 100) / badges.size}% Unlocked",
                    color = WorkoutOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // End of Week and End of Month Notification Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Weekly Recap Action Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2838)),
                border = androidx.compose.foundation.BorderStroke(1.dp, FitnessCyan.copy(alpha = 0.5f)),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        activeRecapDialog = "weekly"
                        NotificationHelper.sendAchievementNotification(
                            context = context,
                            notificationId = 1001,
                            title = "Weekly Fitness Recap! 🎯",
                            message = "You completed $weeklyWorkoutsCount workouts and burned $weeklyCalories kcal this week!",
                            bigText = "Amazing week! You completed $weeklyWorkoutsCount workouts, burned $weeklyCalories kcal, and kept your streak alive. Keep dominating next week!"
                        )
                    }
                    .testTag("weekly_recap_btn")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "WEEKLY RECAP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = FitnessCyan,
                            letterSpacing = 1.sp
                        )
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Notify",
                            tint = FitnessCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "$weeklyWorkoutsCount Workouts",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Text(
                        text = "Notify & View Summary",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            // Monthly Recap Action Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2234)),
                border = androidx.compose.foundation.BorderStroke(1.dp, WorkoutOrange.copy(alpha = 0.5f)),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        activeRecapDialog = "monthly"
                        NotificationHelper.sendAchievementNotification(
                            context = context,
                            notificationId = 1002,
                            title = "Monthly Milestone Recap! 🏆",
                            message = "Outstanding! $monthlyWorkoutsCount workouts completed and $monthlyCalories kcal burned this month!",
                            bigText = "Trophy worthy month! You conquered $monthlyWorkoutsCount workouts and burned $monthlyCalories total kcal. You earned the Monthly Titan milestone!"
                        )
                    }
                    .testTag("monthly_recap_btn")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MONTHLY RECAP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = WorkoutOrange,
                            letterSpacing = 1.sp
                        )
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = WorkoutOrange,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "$monthlyWorkoutsCount Workouts",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Text(
                        text = "Notify & View Trophy",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Badges Grid (2 columns)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val chunkedBadges = badges.chunked(2)
            chunkedBadges.forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEach { badge ->
                        BadgeCard(
                            badge = badge,
                            onClick = { selectedBadgeDetail = badge },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

    // Weekly Recap Celebration Dialog
    if (activeRecapDialog == "weekly") {
        AlertDialog(
            onDismissRequest = { activeRecapDialog = null },
            containerColor = Color(0xFF1E2838),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = "Celebration",
                        tint = FitnessCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "End of Week Recap! 🎯",
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "A notification has been sent to your device status bar!",
                        fontSize = 12.sp,
                        color = FitnessCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF141C29),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Workouts Completed: $weeklyWorkoutsCount", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Total Calories Burned: $weeklyCalories kcal", color = WorkoutOrange, fontWeight = FontWeight.Bold)
                            Text("Weekly Status: ${if (weeklyWorkoutsCount >= 4) "🎯 Target Met (Weekly Warrior!)" else "Consistent Progress"}", color = FitnessGreen)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { activeRecapDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = FitnessCyan, contentColor = Color.Black)
                ) {
                    Text("Awesome!", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Monthly Recap Celebration Dialog
    if (activeRecapDialog == "monthly") {
        AlertDialog(
            onDismissRequest = { activeRecapDialog = null },
            containerColor = Color(0xFF2C2234),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = WorkoutOrange,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Monthly Milestone! 🏆",
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "A monthly achievement notification has been dispatched!",
                        fontSize = 12.sp,
                        color = WorkoutOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1C1622),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Monthly Workouts: $monthlyWorkoutsCount sessions", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Total Monthly Burn: $monthlyCalories kcal", color = WorkoutOrange, fontWeight = FontWeight.Bold)
                            Text("Milestone Badge: 🌟 Monthly Titan Unlocked!", color = FitnessGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { activeRecapDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = WorkoutOrange, contentColor = Color.Black)
                ) {
                    Text("Celebrate!", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Badge Details Dialog
    selectedBadgeDetail?.let { badge ->
        AlertDialog(
            onDismissRequest = { selectedBadgeDetail = null },
            containerColor = Color(0xFF1E2432),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = badge.iconEmoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = badge.title,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 17.sp
                        )
                        Text(
                            text = if (badge.isUnlocked) "UNLOCKED" else "LOCKED",
                            fontWeight = FontWeight.Bold,
                            color = if (badge.isUnlocked) FitnessGreen else Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }
            },
            text = {
                Column {
                    Text(text = badge.description, color = Color(0xFFCBD5E1), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { (badge.progress.toFloat() / badge.maxProgress.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = if (badge.isUnlocked) WorkoutOrange else Color(0xFF64748B),
                        trackColor = Color(0xFF2C3446),
                        strokeCap = StrokeCap.Round
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Progress: ${badge.progress} / ${badge.maxProgress}",
                        fontSize = 12.sp,
                        color = WorkoutOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedBadgeDetail = null },
                    colors = ButtonDefaults.buttonColors(containerColor = WorkoutOrange, contentColor = Color.Black)
                ) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun BadgeCard(
    badge: BadgeItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked) Color(0xFF262030) else Color(0xFF1A1F2C)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (badge.isUnlocked) WorkoutOrange.copy(alpha = 0.5f) else Color(0xFF2C3446)
        ),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("badge_card_${badge.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = badge.iconEmoji,
                    fontSize = 26.sp
                )

                if (badge.isUnlocked) {
                    Surface(
                        shape = CircleShape,
                        color = FitnessGreen,
                        modifier = Modifier.size(18.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Unlocked",
                                tint = Color.Black,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = badge.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (badge.isUnlocked) Color.White else Color(0xFF94A3B8)
            )

            Text(
                text = badge.description,
                fontSize = 10.sp,
                color = Color(0xFF64748B),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (badge.progress.toFloat() / badge.maxProgress.toFloat()).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = if (badge.isUnlocked) WorkoutOrange else Color(0xFF475569),
                trackColor = Color(0xFF252D3D),
                strokeCap = StrokeCap.Round
            )
        }
    }
}
