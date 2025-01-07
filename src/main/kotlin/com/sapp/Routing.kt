package com.sapp

import com.sapp.model.TaskRepository
import com.sapp.model.tasksAsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }

    routing {
        staticResources("/content", "mycontent")

        get("/") {
            call.respondText("Hello World!")
        }

        get("/projectName") {
            call.respondText("ktor-sample")
        }

        get("/error-test") {
            throw IllegalStateException("Too Busy")
        }

        get("/tasks") {
            val tasks = TaskRepository.allTasks()
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = tasks.tasksAsTable()
            )
        }
    }
}
