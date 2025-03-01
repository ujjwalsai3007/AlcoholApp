package com.example.routes

import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.springframework.util.RouteMatcher

fun RouteMatcher.Route.homeRoutes() {
    get("/api/homedata") {
        try {
            val homeData = HomeData(
                featuredProducts = mockProducts.take(3),
                categories = listOf(
                    Category(
                        id = "1",
                        name = "Beer",
                        imageUrl = "https://picsum.photos/400/300?random=10"
                    ),
                    Category(
                        id = "2",
                        name = "Wine",
                        imageUrl = "https://picsum.photos/400/300?random=11"
                    ),
                    Category(
                        id = "3",
                        name = "Vodka",
                        imageUrl = "https://picsum.photos/400/300?random=12"
                    ),
                    Category(
                        id = "4",
                        name = "Whiskey",
                        imageUrl = "https://picsum.photos/400/300?random=13"
                    ),
                    Category(
                        id = "5",
                        name = "Sparkler",
                        imageUrl = "https://picsum.photos/400/300?random=14"
                    ),
                    Category(
                        id = "6",
                        name = "Rum",
                        imageUrl = "https://picsum.photos/400/300?random=15"
                    ),
                    Category(
                        id = "7",
                        name = "Cider",
                        imageUrl = "https://picsum.photos/400/300?random=16"
                    )
                ),
                banners = listOf(
                    Banner(
                        id = "1",
                        imageUrl = "https://picsum.photos/800/400?random=20",
                        targetUrl = "/promotion/summer-sale"
                    ),
                    Banner(
                        id = "2",
                        imageUrl = "https://picsum.photos/800/400?random=21",
                        targetUrl = "/promotion/new-arrivals"
                    )
                )
            )
            call.respond(homeData)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, e.message ?: "An error occurred")
        }
    }
}