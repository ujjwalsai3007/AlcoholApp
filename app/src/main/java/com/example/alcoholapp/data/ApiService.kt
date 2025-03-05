package com.example.alcoholapp.data

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import com.example.alcoholapp.domain.model.HomeResponse
import com.example.alcoholapp.domain.model.Category
import com.example.alcoholapp.domain.model.Banner
import com.example.alcoholapp.domain.model.Product
import com.example.alcoholapp.domain.model.Brand
import kotlinx.serialization.Serializable

@Serializable
data class CategoryProductsResponse(
    val products: List<Product>
)

class ApiService {
    private val baseUrl = "http://10.0.2.2:8081"
    
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    suspend fun getHomeData(): HomeResponse {
        return try {
            println("Fetching home data from $baseUrl/home")
            client.get("$baseUrl/home").body()
        } catch (e: Exception) {
            println("Error fetching home data: ${e.message}")
            throw e
        }
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        return try {
            println("Fetching products for category: $category")
            client.get("$baseUrl/products/$category").body()
        } catch (e: Exception) {
            println("Error fetching products for category $category: ${e.message}")
            throw e
        }
    }
}