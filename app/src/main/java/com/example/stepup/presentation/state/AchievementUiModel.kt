package com.example.stepup.presentation.state

enum class AchievementType {
    FIRST_STEP,
    CONSISTENCY,
    SPECIALIST
}

data class AchievementUiModel(
    val type: AchievementType,
    val isUnlocked: Boolean,
    val progress: Int,
    val target: Int
)

