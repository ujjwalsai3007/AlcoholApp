package com.example.alcoholapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcoholapp.data.CartManager
import com.example.alcoholapp.data.repository.HomeRepository
import com.example.alcoholapp.domain.model.HomeResponse
import com.example.alcoholapp.domain.model.Category
import com.example.alcoholapp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
    private val _homeData = MutableLiveData<HomeResponse>()
    val homeData: LiveData<HomeResponse> = _homeData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    private val _categoryProducts = MutableStateFlow<List<Product>>(emptyList())
    val categoryProducts: StateFlow<List<Product>> = _categoryProducts

    private val preloadedCategories = mutableSetOf<String>()
    private val categoryProductsCache = mutableMapOf<String, List<Product>>()
    
    private val cartManager = CartManager.getInstance()

    fun getCategoryProducts(categoryId: String): List<Product> {
        return categoryProductsCache[categoryId] ?: emptyList()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""
            try {
                val result = repository.getHomeData()
                _homeData.value = result
                // Preload first category's products
                result.categories.firstOrNull()?.let { 
                    preloadCategoryProducts(it)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun preloadCategoryProducts(category: Category) {
        if (category.id in preloadedCategories) return
        
        viewModelScope.launch {
            try {
                val products = repository.getCategoryProducts(category.id)
                categoryProductsCache[category.id] = products
                preloadedCategories.add(category.id)
            } catch (e: Exception) {
                // Silent fail for preloading
            }
        }
    }

    fun isCategoryPreloaded(category: Category): Boolean {
        return category.id in preloadedCategories
    }

    fun selectCategory(category: Category) {
        viewModelScope.launch {
            try {
                _selectedCategory.value = category
                _isLoading.value = true
                if (!isCategoryPreloaded(category)) {
                    val products = repository.getCategoryProducts(category.id)
                    println("DEBUG: Fetched products for category ${category.name}: ${products.size} items")
                    products.forEach { product ->
                        println("DEBUG: Product ${product.name} - Image URL: ${product.imageUrl}")
                    }
                    categoryProductsCache[category.id] = products
                    preloadedCategories.add(category.id)
                }
                _categoryProducts.value = categoryProductsCache[category.id] ?: emptyList()
                println("DEBUG: Setting category products: ${_categoryProducts.value.size} items")
                _isLoading.value = false
            } catch (e: Exception) {
                println("DEBUG: Error loading category products: ${e.message}")
                _error.value = e.message ?: "Failed to load category products"
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedCategory() {
        _selectedCategory.value = null
        _categoryProducts.value = emptyList()
    }
    
    fun addToCart(product: Product) {
        cartManager.addToCart(product)
    }

    fun getCartQuantity(productId: String): Int {
        return cartManager.getCartItemForProduct(productId)?.quantity ?: 0
    }

    fun isProductInCart(productId: String): Boolean {
        return cartManager.getCartItemForProduct(productId) != null
    }
}