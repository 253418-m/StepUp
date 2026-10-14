package com.example.stepup.presentation.state

import com.example.stepup.data.model.Task

enum class ScreenStatus {
    LOADING, CONTENT, EMPTY, ERROR
}

enum class TaskStatusFilter {
    ALL, PENDING, COMPLETED
}

data class TaskUiState(
    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val tasks: List<Task> = emptyList(),
    val visibleTasks: List<Task> = emptyList(),

    val statusFilter: TaskStatusFilter = TaskStatusFilter.ALL,
    val categoryFilter: String? = null,

    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val pendingTasks: Int = 0,
    val completionPercent: Int = 0,

    val totalXp: Int = 0,
    val level: Int = 1,
    val xpInLevel: Int = 0,

    val achievements: List<AchievementUiModel> = emptyList(),
    val hasError: Boolean = false
) {
    val completedCount: Int get() = completedTasks
    val pendingCount: Int get() = pendingTasks
    val progress: Float get() = completionPercent / 100f
}

