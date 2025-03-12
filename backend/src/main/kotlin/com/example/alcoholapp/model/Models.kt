package com.example.alcoholapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String,
    val imageUrl: String
)

@Serializable
data class Brand(
    val id: String,
    val name: String,
    val imageUrl: String
)

@Serializable
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val categoryId: String,
    val inStock: Boolean = true,
    val quantity: Int = 0,  // For cart functionality
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val isLimitedEdition: Boolean = false
)

@Serializable
data class Banner(
    val id: String,
    val imageUrl: String,
    val targetUrl: String
)

@Serializable
data class HomeResponse(
    val categories: List<Category>,
    val brands: List<Brand>,
    val banners: List<Banner>,
    val limitedEditionProducts: List<Product>,
    val categoryProducts: Map<String, List<Product>> = emptyMap()
)