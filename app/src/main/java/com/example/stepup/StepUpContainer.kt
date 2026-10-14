package com.example.stepup

import android.content.Context
import com.example.stepup.data.local.SeedDataSource
import com.example.stepup.data.local.TaskJsonCodec
import com.example.stepup.data.local.TaskPreferencesDataSource
import com.example.stepup.data.repository.LocalTaskRepository
import com.example.stepup.data.repository.TaskRepository
import com.example.stepup.presentation.viewmodel.TaskViewModelFactory

class StepUpContainer(context: Context) {

    private val appContext = context.applicationContext

    private val codec = TaskJsonCodec()

    private val seedDataSource = SeedDataSource(
        assets = appContext.assets,
        codec = codec
    )

    private val preferencesDataSource =
        TaskPreferencesDataSource(
            context = appContext,
            codec = codec
        )

    val taskRepository: TaskRepository = LocalTaskRepository(
        seedDataSource = seedDataSource,
        preferencesDataSource = preferencesDataSource
    )

    val taskViewModelFactory = TaskViewModelFactory(
        repository = taskRepository
    )
}
