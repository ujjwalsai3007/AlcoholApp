package com.example.alcoholapp.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureStatic() {
    routing {
        static("/static") {
            resources("static")
            default("static/index.html")
        }
    }
}