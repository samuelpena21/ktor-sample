package com.sapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val name: String,
    val description: String,
    val priority: Priority
)

enum class Priority {
    LOW, MEDIUM, HIGH, VITAL
}