package com.example.alcoholapp

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.http.content.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import kotlinx.serialization.json.Json
import com.example.alcoholapp.data.HomeDataService
import com.example.alcoholapp.data.ProductService

import com.example.alcoholapp.database.DatabaseFactory

fun main() {
    // Initialize database
    DatabaseFactory.init()
    
    embeddedServer(
        Netty,
        port = 8081,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    // Configure static file serving
    routing {
        static("/") {
            resources("static")
        }
    }

    // Install CORS to allow requests from Android app
    install(CORS) {
        anyHost()
        allowHeader("Content-Type")
        allowHeader(HttpHeaders.ContentDisposition)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
    }
    
    // Install Content Negotiation with JSON
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    routing {
        get("/api/home") {
            val homeDataService = HomeDataService()
            call.respond(homeDataService.getHomeData())
        }
        
        get("/api/products/{category}") {
            val category = call.parameters["category"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, 
                mapOf("error" to "Category parameter is required")
            )
            
            val productService = ProductService()
            call.respond(productService.getProductsByCategory(category))
        }
    }
    
    // Configure routing
    configureRouting()
}