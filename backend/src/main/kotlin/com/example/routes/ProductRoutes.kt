package com.example.routes

import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productRoutes() {
    // Get product details by ID
    get("/products/{id}") {
        try {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            val product = getProductById(id)
            if (product != null) {
                call.respond(product)
            } else {
                call.respond(HttpStatusCode.NotFound, "Product not found")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request")
        }
    }

    // Get products by category
    get("/products/category/{categoryId}") {
        try {
            val categoryId = call.parameters["categoryId"] ?: throw IllegalArgumentException("Invalid category ID")
            val products = getProductsByCategory(categoryId)
            call.respond(products)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request")
        }
    }
}

// Simulated database functions
private fun getProductById(id: String): Product? {
    return mockProducts.find { it.id == id }
}

private fun getProductsByCategory(categoryId: String): List<Product> {
    return when(categoryId) {
        "1" -> mockProducts.filter { it.name.contains("Beer", ignoreCase = true) }
        "2" -> mockProducts.filter { it.name.contains("Wine", ignoreCase = true) }
        "3" -> mockProducts.filter { it.name.contains("Vodka", ignoreCase = true) }
        "4" -> mockProducts.filter { it.name.contains("Whiskey", ignoreCase = true) }
        "5" -> mockProducts.filter { it.name.contains("Sparkler", ignoreCase = true) }
        "6" -> mockProducts.filter { it.name.contains("Rum", ignoreCase = true) }
        "7" -> mockProducts.filter { it.name.contains("Cider", ignoreCase = true) }
        else -> emptyList()
    }
}

// Mock product data
private val mockProducts = listOf(
    Product(
        id = "1",
        name = "Premium Whiskey Gold Label",
        description = "Smooth aged whiskey with rich caramel notes",
        price = 59.99,
        imageUrl = "https://picsum.photos/400/300?random=1"
    ),
    Product(
        id = "2",
        name = "Craft Beer Collection",
        description = "Assorted premium craft beers",
        price = 24.99,
        imageUrl = "https://picsum.photos/400/300?random=2"
    ),
    Product(
        id = "3",
        name = "Red Wine Reserve",
        description = "Full-bodied red wine with oak finish",
        price = 34.99,
        imageUrl = "https://picsum.photos/400/300?random=3"
    ),
    Product(
        id = "4",
        name = "Premium Vodka Crystal",
        description = "Triple-distilled premium vodka",
        price = 45.99,
        imageUrl = "https://picsum.photos/400/300?random=4"
    ),
    Product(
        id = "5",
        name = "Sparkling Wine Rose",
        description = "Elegant sparkling wine with fruity notes",
        price = 29.99,
        imageUrl = "https://picsum.photos/400/300?random=5"
    ),
    Product(
        id = "6",
        name = "Dark Rum Special",
        description = "Aged dark rum with tropical flavors",
        price = 39.99,
        imageUrl = "https://picsum.photos/400/300?random=6"
    ),
    Product(
        id = "7",
        name = "Apple Cider Premium",
        description = "Crisp apple cider with natural sweetness",
        price = 19.99,
        imageUrl = "https://picsum.photos/400/300?random=7"
    )
)