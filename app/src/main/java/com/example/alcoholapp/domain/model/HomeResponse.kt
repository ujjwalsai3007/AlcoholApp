package com.example.alcoholapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val categories: List<Category> = emptyList(),
    val banners: List<Banner> = emptyList(),
    val brands: List<Brand> = emptyList(),
    val limitedEditionProducts: List<Product> = emptyList(),
    val categoryProducts: Map<String, List<Product>> = emptyMap()
)

@Serializable
data class Category(
    val id: String,
    val name: String,
    val imageUrl: String
)

@Serializable
data class Banner(
    val id: String,
    val imageUrl: String,
    val targetUrl: String
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
    val rating: Double = 0.0,
    val reviewCount: Int = 0
)

@Serializable
data class Brand(
    val id: String,
    val name: String,
    val imageUrl: String
)