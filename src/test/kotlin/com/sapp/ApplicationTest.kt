package com.sapp

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello World!", response.bodyAsText())

    }

    @Test
    fun testProjectName() = testApplication {
        application { module() }
        val response = client.get("/projectName")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("ktor-sample", response.bodyAsText())
    }
}