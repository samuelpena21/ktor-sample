package com.sapp

import com.sapp.model.Priority
import com.sapp.model.Task
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

fun Application.configureSockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/tasks") {
            val tasks = listOf(
                Task("cleaning", "Clean the house", Priority.LOW),
                Task("gardening", "Mow the lawn", Priority.MEDIUM),
                Task("shopping", "Buy the groceries", Priority.HIGH),
                Task("painting", "Paint the fence", Priority.MEDIUM)
            )

            for (task in tasks) {
                sendSerialized(task)
                delay(1000)
            }

            close(CloseReason(CloseReason.Codes.NORMAL, "All done"))
        }
    }
}