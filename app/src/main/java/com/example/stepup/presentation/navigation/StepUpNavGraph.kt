package com.example.stepup.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.stepup.presentation.screens.ProgressScreen
import com.example.stepup.presentation.screens.TaskFormScreen
import com.example.stepup.presentation.screens.TasksScreen
import com.example.stepup.presentation.state.TaskFormUiState
import com.example.stepup.presentation.state.TaskStatusFilter
import com.example.stepup.presentation.state.TaskUiState

/** Coordina solo el cambio de pantallas; no conoce ViewModel ni DataStore. */
@Composable
fun StepUpNavGraph(
    uiState: TaskUiState,
    formState: TaskFormUiState,
    onToggleTask: (String) -> Unit,
    onStatusFilterChange: (TaskStatusFilter) -> Unit,
    onCategoryFilterChange: (String?) -> Unit,
    onDeleteTask: (String) -> Unit,
    onStartNewTask: () -> Unit,
    onStartEditingTask: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onPriorityChange: (Int) -> Unit,
    onEffortChange: (Int) -> Unit,
    onMinutesChange: (String) -> Unit,
    onSave: () -> Unit,
    onClearError: () -> Unit
) {
    var screen by rememberSaveable { mutableStateOf("tasks") }

    BackHandler(enabled = screen != "tasks") {
        screen = "tasks"
    }

    LaunchedEffect(formState.savedSuccessfully, screen) {
        if (screen == "form" && formState.savedSuccessfully) {
            screen = "tasks"
        }
    }

    when (screen) {
        "form" -> TaskFormScreen(
            formState = formState,
            hasError = uiState.hasError,
            onTitleChange = onTitleChange,
            onCategoryChange = onCategoryChange,
            onPriorityChange = onPriorityChange,
            onEffortChange = onEffortChange,
            onMinutesChange = onMinutesChange,
            onSave = onSave,
            onCancel = { screen = "tasks" },
            onClearError = onClearError
        )

        "progress" -> ProgressScreen(
            uiState = uiState,
            onTasksClick = { screen = "tasks" }
        )

        else -> TasksScreen(
            uiState = uiState,
            onToggleTask = onToggleTask,
            onStatusFilterChange = onStatusFilterChange,
            onCategoryFilterChange = onCategoryFilterChange,
            onDeleteTask = onDeleteTask,
            onAddTask = {
                onStartNewTask()
                screen = "form"
            },
            onEditTask = { taskId ->
                onStartEditingTask(taskId)
                screen = "form"
            },
            onProgressClick = { screen = "progress" },
            onClearError = onClearError
        )
    }
}
