package com.example.alcoholapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val banners: List<Banner>,
    val categories: List<Category>,
    val featuredProducts: List<Product>
)

@Serializable
data class Banner(
    val id: Int,
    val title: String,
    val imageUrl: String
)

@Serializable
data class Category(
    val id: Int,
    val name: String,
    val iconUrl: String
)

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)