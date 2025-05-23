package com.sapp.plugins

import com.sapp.model.Task
import com.sapp.model.TaskRepository
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureSockets(repository: TaskRepository) {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val sessions = Collections.synchronizedList<WebSocketServerSession>(ArrayList())

        webSocket("/tasks") {
            sendAllTasks(repository.allTasks())
            close(CloseReason(CloseReason.Codes.NORMAL, "All done"))
        }

        webSocket("/tasks2") {
            sessions.add(this)
            sendAllTasks(repository.allTasks())

            while (true) {
                val newTask = receiveDeserialized<Task>()
                repository.addTask(newTask)
                for (session in sessions) {
                    session.sendSerialized(newTask)
                }

            }
        }
    }
}

private suspend fun DefaultWebSocketServerSession.sendAllTasks(tasks: List<Task>) {
    for (task in tasks) {
        sendSerialized(task)
        delay(1000)
    }
}