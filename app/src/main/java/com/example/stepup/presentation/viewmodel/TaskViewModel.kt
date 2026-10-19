package com.example.stepup.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepup.data.model.Task
import com.example.stepup.data.repository.TaskRepository
import com.example.stepup.presentation.state.AchievementType
import com.example.stepup.presentation.state.AchievementUiModel
import com.example.stepup.presentation.state.FormError
import com.example.stepup.presentation.state.ScreenStatus
import com.example.stepup.presentation.state.TaskFormUiState
import com.example.stepup.presentation.state.TaskStatusFilter
import com.example.stepup.presentation.state.TaskUiState
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private val _formUiState = MutableStateFlow(TaskFormUiState())
    val formUiState: StateFlow<TaskFormUiState> = _formUiState.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            repository.tasks
                .catch { showError() }
                .collect { tasks ->
                    _uiState.update { current ->
                        buildUiState(
                            tasks = tasks,
                            statusFilter = current.statusFilter,
                            categoryFilter = current.categoryFilter
                        )
                    }
                }
        }
    }

    fun toggleTask(taskId: String) {
        val task = _uiState.value.tasks
            .firstOrNull { it.id == taskId } ?: return

        viewModelScope.launch {
            try {
                repository.updateTask(task.copy(isCompleted = !task.isCompleted))
            } catch (_: Exception) {
                showError()
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                repository.deleteTask(taskId)
            } catch (_: Exception) {
                showError()
            }
        }
    }

    fun setStatusFilter(filter: TaskStatusFilter) {
        _uiState.update { current ->
            buildUiState(current.tasks, filter, current.categoryFilter)
        }
    }

    fun setCategoryFilter(category: String?) {
        _uiState.update { current ->
            buildUiState(current.tasks, current.statusFilter, category)
        }
    }

    fun startNewTask() {
        _formUiState.value = TaskFormUiState()
    }

    fun startEditingTask(taskId: String) {
        val task = _uiState.value.tasks
            .firstOrNull { it.id == taskId } ?: return

        _formUiState.value = TaskFormUiState(
            editingTaskId = task.id,
            title = task.title,
            category = task.category,
            priority = task.priority,
            effort = task.effort,
            estimatedMinutesInput = task.estimatedMinutes.toString()
        )
    }

    fun updateTitle(value: String) {
        _formUiState.update { it.copy(title = value, titleError = null) }
    }

    fun updateCategory(value: String) {
        if (value in listOf("Estudio", "Personal", "Hogar")) {
            _formUiState.update { it.copy(category = value) }
        }
    }

    fun updatePriority(value: Int) {
        if (value in 1..3) {
            _formUiState.update { it.copy(priority = value) }
        }
    }

    fun updateEffort(value: Int) {
        if (value in 1..3) {
            _formUiState.update { it.copy(effort = value) }
        }
    }

    fun updateEstimatedMinutes(value: String) {
        _formUiState.update {
            it.copy(estimatedMinutesInput = value, durationError = null)
        }
    }

    fun saveTask() {
        val form = _formUiState.value
        if (form.isSaving) return

        val title = form.title.trim()
        val duration = form.estimatedMinutesInput.toIntOrNull()

        val titleError = when {
            title.isEmpty() -> FormError.REQUIRED_TITLE
            title.length < 3 -> FormError.TITLE_TOO_SHORT
            title.length > 60 -> FormError.TITLE_TOO_LONG
            else -> null
        }

        val durationError = if (duration == null || duration <= 0) {
            FormError.INVALID_DURATION
        } else {
            null
        }

        if (titleError != null || durationError != null) {
            _formUiState.update {
                it.copy(titleError = titleError, durationError = durationError)
            }
            return
        }

        val validDuration = requireNotNull(duration)
        _formUiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            try {
                val editingId = form.editingTaskId

                if (editingId == null) {
                    repository.addTask(
                        Task(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            category = form.category,
                            priority = form.priority,
                            effort = form.effort,
                            estimatedMinutes = validDuration,
                            isCompleted = false
                        )
                    )
                } else {
                    val existingTask = _uiState.value.tasks
                        .firstOrNull { it.id == editingId }

                    if (existingTask == null) {
                        showError()
                        _formUiState.update { it.copy(isSaving = false) }
                        return@launch
                    }

                    repository.updateTask(
                        existingTask.copy(
                            title = title,
                            category = form.category,
                            priority = form.priority,
                            effort = form.effort,
                            estimatedMinutes = validDuration
                        )
                    )
                }

                _formUiState.update {
                    it.copy(isSaving = false, savedSuccessfully = true)
                }
            } catch (_: Exception) {
                _formUiState.update { it.copy(isSaving = false) }
                showError()
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(
                hasError = false,
                screenStatus = if (it.totalTasks == 0) {
                    ScreenStatus.EMPTY
                } else {
                    ScreenStatus.CONTENT
                }
            )
        }
    }

    private fun calculateTaskXp(task: Task): Int {
        if (!task.isCompleted) return 0

        val durationBonus = when {
            task.estimatedMinutes < 30 -> 0
            task.estimatedMinutes < 60 -> 5
            else -> 10
        }

        return 10 + 5 * task.priority + 3 * task.effort + durationBonus
    }

    private fun buildUiState(
        tasks: List<Task>,
        statusFilter: TaskStatusFilter,
        categoryFilter: String?
    ): TaskUiState {
        val completed = tasks.count { it.isCompleted }
        val pending = tasks.size - completed
        val totalXp = tasks.sumOf { calculateTaskXp(it) }
        val level = 1 + totalXp / 100
        val xpInLevel = totalXp % 100
        val percentage = if (tasks.isEmpty()) 0 else completed * 100 / tasks.size

        val visibleTasks = tasks.filter { task ->
            val matchesStatus = when (statusFilter) {
                TaskStatusFilter.ALL -> true
                TaskStatusFilter.PENDING -> !task.isCompleted
                TaskStatusFilter.COMPLETED -> task.isCompleted
            }
            val matchesCategory = categoryFilter == null || task.category == categoryFilter
            matchesStatus && matchesCategory
        }

        val completedByCategory = tasks.filter { it.isCompleted }
            .groupingBy { it.category }
            .eachCount()
        val maxCategoryCompleted = completedByCategory.values.maxOrNull() ?: 0

        val achievements = listOf(
            AchievementUiModel(
                type = AchievementType.FIRST_STEP,
                isUnlocked = completed >= 1,
                progress = completed.coerceAtMost(1),
                target = 1
            ),
            AchievementUiModel(
                type = AchievementType.CONSISTENCY,
                isUnlocked = completed >= 5,
                progress = completed.coerceAtMost(5),
                target = 5
            ),
            AchievementUiModel(
                type = AchievementType.SPECIALIST,
                isUnlocked = maxCategoryCompleted >= 3,
                progress = maxCategoryCompleted.coerceAtMost(3),
                target = 3
            )
        )

        return TaskUiState(
            screenStatus = if (tasks.isEmpty()) ScreenStatus.EMPTY else ScreenStatus.CONTENT,
            tasks = tasks,
            visibleTasks = visibleTasks,
            statusFilter = statusFilter,
            categoryFilter = categoryFilter,
            totalTasks = tasks.size,
            completedTasks = completed,
            pendingTasks = pending,
            completionPercent = percentage,
            totalXp = totalXp,
            level = level,
            xpInLevel = xpInLevel,
            achievements = achievements
        )
    }

    private fun showError() {
        _uiState.update {
            it.copy(hasError = true, screenStatus = ScreenStatus.ERROR)
        }
    }
}

