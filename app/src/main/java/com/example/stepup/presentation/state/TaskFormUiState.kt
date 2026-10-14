package com.example.stepup.presentation.state

enum class FormError {
    REQUIRED_TITLE,
    TITLE_TOO_LONG,
    INVALID_DURATION
}

data class TaskFormUiState(
    val editingTaskId: String? = null,
    val title: String = "",
    val category: String = "Estudio",
    val priority: Int = 2,
    val effort: Int = 2,
    val estimatedMinutesInput: String = "",

    val titleError: FormError? = null,
    val durationError: FormError? = null,

    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false
)
