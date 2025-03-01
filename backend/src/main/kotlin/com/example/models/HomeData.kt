package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class HomeData(
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
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String
)