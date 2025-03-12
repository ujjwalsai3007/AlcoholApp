package com.example.alcoholapp.data.repository

import com.example.alcoholapp.data.ApiService
import com.example.alcoholapp.domain.model.HomeResponse
import com.example.alcoholapp.domain.model.Product

class HomeRepository(private val apiService: ApiService) {
    suspend fun getHomeData(): HomeResponse {
        return apiService.getHomeData()
    }

    suspend fun getCategoryProducts(categoryId: String): List<Product> {
        return try {
            // Get the products from the HomeData that match the category
            val homeData = getHomeData()
            homeData.categoryProducts[categoryId] ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
