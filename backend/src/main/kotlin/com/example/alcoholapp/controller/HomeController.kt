package com.example.alcoholapp.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.alcoholapp.domain.model.HomeResponse
import com.example.alcoholapp.domain.model.Banner
import com.example.alcoholapp.domain.model.Category
import com.example.alcoholapp.domain.model.Product

@RestController
@RequestMapping("/api")
class HomeController {

    @GetMapping("/homedata")
    fun getHomeData(): HomeResponse {
        // Sample data for demonstration
        val banners = listOf(
            Banner(1, "Banner 1", "https://example.com/banner1.jpg"),
            Banner(2, "Banner 2", "https://example.com/banner2.jpg")
        )

        val categories = listOf(
            Category(1, "Wine", "https://example.com/wine-icon.png"),
            Category(2, "Beer", "https://example.com/beer-icon.png"),
            Category(3, "Spirits", "https://example.com/spirits-icon.png")
        )

        val featuredProducts = listOf(
            Product(1, "Red Wine", "Premium Red Wine", 29.99, "https://example.com/red-wine.jpg"),
            Product(2, "Craft Beer", "Special Craft Beer", 9.99, "https://example.com/craft-beer.jpg")
        )

        return HomeResponse(banners, categories, featuredProducts)
    }
}