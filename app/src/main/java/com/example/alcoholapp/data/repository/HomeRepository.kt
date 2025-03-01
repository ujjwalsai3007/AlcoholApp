package com.example.alcoholapp.data.repository

import com.example.alcoholapp.data.ApiService
import com.example.alcoholapp.domain.model.HomeResponse

class HomeRepository(private val apiService: ApiService) {
    suspend fun getHomeData(): HomeResponse {
        return apiService.getHomeData()
    }
}
