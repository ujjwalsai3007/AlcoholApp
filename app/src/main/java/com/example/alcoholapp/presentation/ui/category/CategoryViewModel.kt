package com.example.alcoholapp.presentation.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcoholapp.data.ApiService
import com.example.alcoholapp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val apiService: ApiService) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun loadProductsByCategory(categoryName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getProductsByCategory(categoryName)
                _products.value = response
                _error.value = ""
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load products"
                _products.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(product: Product) {
        // TODO: Implement cart functionality
        // This would typically update a cart repository or service
    }
}