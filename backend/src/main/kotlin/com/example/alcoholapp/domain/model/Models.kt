package com.example.alcoholapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val categories: List<Category> = emptyList(),
    val banners: List<Banner> = emptyList(),
    val brands: List<Brand> = emptyList(),
    val limitedEditionProducts: List<Product> = emptyList()
)

@Serializable
data class Banner(
    val id: String,
    val imageUrl: String,
    val targetUrl: String
)

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
    val categoryId: String
)