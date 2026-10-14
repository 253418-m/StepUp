package com.example.stepup.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stepup.StepUpApplication
import com.example.stepup.presentation.screens.TasksScreen
import com.example.stepup.presentation.viewmodel.TaskViewModel

@Composable
fun StepUpApp() {

    val application =
        LocalContext.current.applicationContext
                as StepUpApplication

    val viewModel: TaskViewModel = viewModel(
        factory = application.container.taskViewModelFactory
    )

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    TasksScreen(
        uiState = uiState,
        onToggleTask = viewModel::toggleTask
    )
}
