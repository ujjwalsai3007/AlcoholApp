package com.example.alcoholapp.presentation.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcoholapp.data.ApiService
import com.example.alcoholapp.data.CartManager
import com.example.alcoholapp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val apiService: ApiService) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error.asStateFlow()
    
    private val cartManager = CartManager.getInstance()
    
    fun loadProducts(categoryName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""
            
            try {
                val response = apiService.getProductsByCategory(categoryName)
                _products.value = response
            } catch (e: Exception) {
                _error.value = "Failed to load products: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addToCart(product: Product) {
        cartManager.addToCart(product)
    }
}