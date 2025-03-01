package com.example.alcoholapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val categories: List<Category> = emptyList(),
    val banners: List<Banner> = emptyList(),
    val featuredProducts: List<Product> = emptyList()
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
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val description: String
)