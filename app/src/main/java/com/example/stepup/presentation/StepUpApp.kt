package com.example.stepup.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stepup.StepUpApplication
import com.example.stepup.presentation.screens.TaskFormScreen
import com.example.stepup.presentation.screens.TasksScreen
import com.example.stepup.presentation.viewmodel.TaskViewModel

@Composable
fun StepUpApp() {
    val application = LocalContext.current.applicationContext as StepUpApplication
    val viewModel: TaskViewModel = viewModel(
        factory = application.container.taskViewModelFactory
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formUiState.collectAsStateWithLifecycle()
    var formIsOpen by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = formIsOpen) {
        formIsOpen = false
    }

    LaunchedEffect(formState.savedSuccessfully, formIsOpen) {
        if (formIsOpen && formState.savedSuccessfully) {
            formIsOpen = false
        }
    }

    if (formIsOpen) {
        TaskFormScreen(
            formState = formState,
            hasError = uiState.hasError,
            onTitleChange = viewModel::updateTitle,
            onCategoryChange = viewModel::updateCategory,
            onPriorityChange = viewModel::updatePriority,
            onEffortChange = viewModel::updateEffort,
            onMinutesChange = viewModel::updateEstimatedMinutes,
            onSave = viewModel::saveTask,
            onCancel = { formIsOpen = false },
            onClearError = viewModel::clearError
        )
    } else {
        TasksScreen(
            uiState = uiState,
            onToggleTask = viewModel::toggleTask,
            onStatusFilterChange = viewModel::setStatusFilter,
            onCategoryFilterChange = viewModel::setCategoryFilter,
            onDeleteTask = viewModel::deleteTask,
            onAddTask = {
                viewModel.startNewTask()
                formIsOpen = true
            },
            onEditTask = { taskId ->
                viewModel.startEditingTask(taskId)
                formIsOpen = true
            },
            onClearError = viewModel::clearError
        )
    }
}
