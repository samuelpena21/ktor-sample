package com.sapp

import com.sapp.model.FakeTaskRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = FakeTaskRepository()

    configureSerialization(repository)
    configureTemplating(repository)
    configureRouting(repository)
    configureSockets(repository)
}
