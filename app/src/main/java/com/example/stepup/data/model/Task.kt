package com.example.stepup.data.model

data class Task(
    val id: String,
    val title: String,
    val category: String,
    val priority: Int,
    val effort: Int,
    val estimatedMinutes: Int,
    val isCompleted: Boolean = false
)

