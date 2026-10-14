package com.example.stepup.data.repository

import com.example.stepup.data.local.SeedDataSource
import com.example.stepup.data.local.TaskPreferencesDataSource
import com.example.stepup.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalTaskRepository(
    private val seedDataSource: SeedDataSource,
    private val preferencesDataSource:
    TaskPreferencesDataSource
) : TaskRepository {

    override val tasks: Flow<List<Task>> =
        preferencesDataSource.storedTasks
            .map { savedTasks ->
                savedTasks ?: seedDataSource.loadTasks()
            }
            .flowOn(Dispatchers.IO)

    override suspend fun addTask(task: Task) {
        modifyTasks { current ->
            require(current.none { it.id == task.id }) {
                "La tarea ya existe"
            }
            current + task
        }
    }

    override suspend fun updateTask(task: Task) {
        modifyTasks { current ->
            require(current.any { it.id == task.id }) {
                "La tarea no existe"
            }

            current.map {
                if (it.id == task.id) task else it
            }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        modifyTasks { current ->
            current.filterNot { it.id == taskId }
        }
    }

    private suspend fun modifyTasks(
        transform: (List<Task>) -> List<Task>
    ) {
        withContext(Dispatchers.IO) {
            val seedTasks = seedDataSource.loadTasks()

            preferencesDataSource.updateTasks(
                seedTasks = seedTasks,
                transform = transform
            )
        }
    }
}
