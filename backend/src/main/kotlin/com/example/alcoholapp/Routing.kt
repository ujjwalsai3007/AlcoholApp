package com.example.alcoholapp

import com.example.alcoholapp.model.*
import com.example.alcoholapp.data.HomeDataService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.HttpStatusCode

fun Application.configureRouting() {
    routing {
        // Health check endpoint
        get("/health") {
            call.respondText("Server is running!")
        }

        // Home endpoint
        get("/api/home") {
            try {
                val homeService = HomeDataService()
                val homeData = homeService.getHomeData()
                call.respond(homeData)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred"))
                )
            }
        }

        // Products routes
        route("/api/products") {
            get {
                // TODO: Implement get all products
                call.respondText("List of products")
            }
            get("/{id}") {
                // TODO: Implement get product by id
                call.respondText("Product details")
            }
        }

        // Categories routes
        route("/api/categories") {
            get {
                // TODO: Implement get all categories
                call.respondText("List of categories")
            }
        }

        // Orders routes
        route("/api/orders") {
            get {
                // TODO: Implement get all orders
                call.respondText("List of orders")
            }
            post {
                // TODO: Implement create order
                call.respondText("Order created")
            }
        }
    }
}