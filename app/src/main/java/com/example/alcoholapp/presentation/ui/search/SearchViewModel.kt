package com.example.alcoholapp.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.alcoholapp.data.ApiService
import com.example.alcoholapp.data.CartManager
import com.example.alcoholapp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val apiService: ApiService
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> = _allProducts.asStateFlow()
    
    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())
    val filteredProducts: StateFlow<List<Product>> = _filteredProducts.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error.asStateFlow()
    
    private val cartManager = CartManager.getInstance()
    
    init {
        fetchAllProducts()
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterProducts()
    }
    
    private fun filterProducts() {
        val query = _searchQuery.value.trim().lowercase()
        if (query.isEmpty()) {
            _filteredProducts.value = _allProducts.value
        } else {
            _filteredProducts.value = _allProducts.value.filter {
                it.name.lowercase().contains(query) || 
                it.description.lowercase().contains(query)
            }
        }
    }
    
    private fun fetchAllProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""
            
            try {
                // Fetch home data which contains products
                val homeData = apiService.getHomeData()
                
                // Collect all products from different categories
                val products = mutableListOf<Product>()
                products.addAll(homeData.limitedEditionProducts)
                
                // Add products from categories if they exist in the response
                homeData.categoryProducts.values.forEach { categoryProducts ->
                    products.addAll(categoryProducts)
                }
                
                // Fetch additional products for each category
                homeData.categories.forEach { category ->
                    try {
                        val categoryProducts = apiService.getProductsByCategory(category.id)
                        products.addAll(categoryProducts)
                    } catch (e: Exception) {
                        // Continue with other categories if one fails
                    }
                }
                
                // Remove duplicates based on product id
                _allProducts.value = products.distinctBy { it.id }
                _filteredProducts.value = _allProducts.value
                
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

class SearchViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
