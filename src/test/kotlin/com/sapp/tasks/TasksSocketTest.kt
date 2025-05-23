package com.sapp.tasks

import com.sapp.plugins.configureRouting
import com.sapp.plugins.configureSerialization
import com.sapp.plugins.configureSockets
import com.sapp.model.FakeTaskRepository
import com.sapp.model.Priority
import com.sapp.model.Task
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class TasksSocketTest {
    @Test
    fun testRoot() = testApplication {
        application {
            val repository = FakeTaskRepository()
            configureRouting(repository)
            configureSerialization(repository)
            configureSockets(repository)
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(WebSockets) {
                contentConverter =
                    KotlinxWebsocketSerializationConverter(Json)
            }
        }

        val expectedTasks = listOf(
            Task("cleaning", "Clean the house", Priority.LOW),
            Task("gardening", "Mow the lawn", Priority.MEDIUM),
            Task("shopping", "Buy the groceries", Priority.HIGH),
            Task("painting", "Paint the fence", Priority.MEDIUM)
        )
        var actualTasks = emptyList<Task>()

        client.webSocket("/tasks") {
            consumeTasksAsFlow().collect { allTasks ->
                actualTasks = allTasks
            }
        }

        assertEquals(expectedTasks.size, actualTasks.size)
        expectedTasks.forEachIndexed { index, task ->
            assertEquals(task, actualTasks[index])
        }
    }

    private fun DefaultClientWebSocketSession.consumeTasksAsFlow() = incoming
        .consumeAsFlow()
        .map {
            converter!!.deserialize<Task>(it)
        }
        .scan(emptyList<Task>()) { list, task ->
            list + task
        }
}