package com.sapp

import com.sapp.model.Priority
import com.sapp.model.Task
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    routing {
        get("/html-thymeleaf") {
            call.respond(ThymeleafContent("index", mapOf("user" to ThymeleafUser(1, "user1"))))
        }

        get("/tasks") {
            val tasks = listOf(
                Task("cleaning", "Clean the house", Priority.LOW),
                Task("gardening", "Mow the lawn", Priority.MEDIUM),
                Task("shopping", "Buy the groceries", Priority.HIGH),
                Task("painting", "Paint the fence", Priority.MEDIUM)
            )
            call.respond(ThymeleafContent("all-tasks", mapOf("tasks" to tasks)))
        }
    }
}

data class ThymeleafUser(val id: Int, val name: String)
