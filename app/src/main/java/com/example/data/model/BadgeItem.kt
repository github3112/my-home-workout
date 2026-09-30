package com.example.data.model

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean,
    val progress: Int,
    val maxProgress: Int,
    val periodType: String = "All-Time" // Weekly, Monthly, All-Time
)
