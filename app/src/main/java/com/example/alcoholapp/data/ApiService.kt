package com.example.alcoholapp.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.HttpTimeout
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.request.get
import com.example.alcoholapp.domain.model.HomeResponse
import com.example.alcoholapp.domain.model.Category
import com.example.alcoholapp.domain.model.Banner
import com.example.alcoholapp.domain.model.Product

class ApiService {
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
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }
    }

    suspend fun getHomeData(): HomeResponse {
        return try {
            client.get("http://10.0.2.2:8080/api/homedata").body()
        } catch (e: Exception) {
            // Return mock data with placeholder images
            HomeResponse(
                categories = listOf(
                    Category("1", "Beer", "https://picsum.photos/200?random=1"),
                    Category("2", "Wine", "https://picsum.photos/200?random=2"),
                    Category("3", "Vodka", "https://picsum.photos/200?random=3"),
                    Category("4", "Whiskey", "https://picsum.photos/200?random=4"),
                    Category("5", "Sparkler", "https://picsum.photos/200?random=5"),
                    Category("6", "Rum", "https://picsum.photos/200?random=6"),
                    Category("7", "Cider", "https://picsum.photos/200?random=7"),
                    Category("8", "More", "https://picsum.photos/200?random=8")
                ),
                banners = listOf(
                    Banner(
                        "1",
                        "https://picsum.photos/800/400?random=20",
                        "https://example.com/promo/1"
                    ),
                    Banner(
                        "2",
                        "https://picsum.photos/800/400?random=21",
                        "https://example.com/promo/2"
                    )
                ),
                featuredProducts = listOf(
                    Product(
                        1,
                        "Premium Beer",
                        12.99,
                        "https://picsum.photos/300/300?random=30",
                        "Premium quality beer"
                    ),
                    Product(
                        2,
                        "Red Wine",
                        24.99,
                        "https://picsum.photos/300/300?random=31",
                        "Aged red wine"
                    ),
                    Product(
                        3,
                        "Vodka Premium",
                        29.99,
                        "https://picsum.photos/300/300?random=32",
                        "Triple distilled vodka"
                    )
                )
            )
        }
    }
}