package com.sapp.model

data class Task(
    val name: String,
    val description: String,
    val priority: Priority
)

enum class Priority {
    LOW, MEDIUM, HIGH, VITAL
}

fun Task.taskAsRow() = """
    <tr>
        <td>$name</td><td>$description</td><td>$priority</td>
    </tr>
    """.trimIndent()

fun List<Task>.tasksAsTable() = this.joinToString(
    prefix = "<table rules=\"all\">",
    postfix = "</table>",
    separator = "\n",
    transform = Task::taskAsRow
)