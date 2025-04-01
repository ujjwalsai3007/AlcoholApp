package com.example.alcoholapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alcoholapp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartManager {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems.asStateFlow()
    
    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()
    
    fun addToCart(product: Product, quantity: Int = 1) {
        Log.d(TAG, "Adding to cart: ${product.name} (${product.id}), quantity: $quantity")
        
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.product.id == product.id }
        
        if (existingItemIndex >= 0) {
            val existingItem = currentItems[existingItemIndex]
            Log.d(TAG, "Item already in cart. Current quantity: ${existingItem.quantity}")
            currentItems[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + quantity)
            Log.d(TAG, "Updated quantity: ${existingItem.quantity + quantity}")
        } else {
            Log.d(TAG, "Adding new item to cart")
            currentItems.add(CartItem(product, quantity))
        }
        
        _cartItems.value = currentItems
        updateCartSummary()
        Log.d(TAG, "Cart now has ${_totalItems.value} items, total price: ${_totalPrice.value}")
    }
    
    fun removeFromCart(productId: String) {
        Log.d(TAG, "Removing item from cart: $productId")
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeIf { it.product.id == productId }
        _cartItems.value = currentItems
        updateCartSummary()
        Log.d(TAG, "Cart now has ${_totalItems.value} items, total price: ${_totalPrice.value}")
    }
    
    fun updateQuantity(productId: String, quantity: Int) {
        Log.d(TAG, "Updating item quantity: $productId, new quantity: $quantity")
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        
        val currentItems = _cartItems.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.product.id == productId }
        
        if (itemIndex >= 0) {
            currentItems[itemIndex] = currentItems[itemIndex].copy(quantity = quantity)
            _cartItems.value = currentItems
            updateCartSummary()
            Log.d(TAG, "Cart now has ${_totalItems.value} items, total price: ${_totalPrice.value}")
        }
    }
    
    fun clearCart() {
        Log.d(TAG, "Clearing cart")
        _cartItems.value = emptyList()
        updateCartSummary()
    }
    
    fun getCartItemForProduct(productId: String): CartItem? {
        return _cartItems.value.find { it.product.id == productId }
    }
    
    private fun updateCartSummary() {
        _totalItems.value = _cartItems.value.sumOf { it.quantity }
        _totalPrice.value = _cartItems.value.sumOf { it.product.price * it.quantity }
    }
    
    companion object {
        private const val TAG = "CartManager"
        
        // Singleton pattern
        private var instance: CartManager? = null
        
        fun getInstance(): CartManager {
            return instance ?: synchronized(this) {
                instance ?: CartManager().also { instance = it }
            }
        }
    }
}

data class CartItem(
    val product: Product,
    val quantity: Int
) 