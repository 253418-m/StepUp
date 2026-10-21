package com.example.stepup.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stepup.StepUpApplication
import com.example.stepup.presentation.navigation.StepUpNavGraph
import com.example.stepup.presentation.viewmodel.TaskViewModel

@Composable
fun StepUpApp() {
    val application = LocalContext.current.applicationContext as StepUpApplication
    val viewModel: TaskViewModel = viewModel(
        factory = application.container.taskViewModelFactory
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formUiState.collectAsStateWithLifecycle()

    StepUpNavGraph(
        uiState = uiState,
        formState = formState,
        onToggleTask = viewModel::toggleTask,
        onStatusFilterChange = viewModel::setStatusFilter,
        onCategoryFilterChange = viewModel::setCategoryFilter,
        onDeleteTask = viewModel::deleteTask,
        onStartNewTask = viewModel::startNewTask,
        onStartEditingTask = viewModel::startEditingTask,
        onTitleChange = viewModel::updateTitle,
        onCategoryChange = viewModel::updateCategory,
        onPriorityChange = viewModel::updatePriority,
        onEffortChange = viewModel::updateEffort,
        onMinutesChange = viewModel::updateEstimatedMinutes,
        onSave = viewModel::saveTask,
        onClearError = viewModel::clearError
    )
}

