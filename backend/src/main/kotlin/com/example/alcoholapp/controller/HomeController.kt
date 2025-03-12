package com.example.alcoholapp.controller

import com.example.alcoholapp.model.HomeResponse
import com.example.alcoholapp.repository.HomeRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureHomeRouting() {
    val homeRepository = HomeRepository()

    routing {
        get("/api/home") {
            try {
                val homeData = homeRepository.getHomeData()
                call.respond(HttpStatusCode.OK, homeData)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred"))
                )
            }
        }
    }
}