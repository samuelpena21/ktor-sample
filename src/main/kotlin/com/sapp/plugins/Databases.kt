package com.sapp.plugins

import com.sapp.db.TaskTable
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases(config: ApplicationConfig) {
    val url = config.property("storage.jdbcURL").getString()
    val driver = config.property("storage.driverClassName").getString()
    val user = config.property("storage.user").getString()
    val password = config.property("storage.password").getString()

    val db = Database.connect(
        url = url,
        driver = driver,
        user = user,
        password = password
    )

    // Create the table(s) if they don't exist
    transaction(db) {
        SchemaUtils.create(TaskTable)
    }

}