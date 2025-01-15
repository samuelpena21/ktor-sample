package com.sapp

import com.sapp.model.PostgresTaskRepository
import com.sapp.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = PostgresTaskRepository()

    configureDatabases()
    configureSerialization(repository)
    configureTemplating(repository)
    configureRouting(repository)
    configureSockets(repository)
}
