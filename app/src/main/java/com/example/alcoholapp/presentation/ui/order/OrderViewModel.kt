package com.example.alcoholapp.presentation.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alcoholapp.data.CartItem
import com.example.alcoholapp.data.CartManager
import com.example.alcoholapp.domain.model.Product
import kotlinx.coroutines.flow.StateFlow

class OrderViewModel : ViewModel() {
    private val cartManager = CartManager.getInstance()
    
    val cartItems: StateFlow<List<CartItem>> = cartManager.cartItems
    val totalItems: StateFlow<Int> = cartManager.totalItems
    val totalPrice: StateFlow<Double> = cartManager.totalPrice
    
    fun updateQuantity(productId: String, quantity: Int) {
        cartManager.updateQuantity(productId, quantity)
    }
    
    fun removeFromCart(productId: String) {
        cartManager.removeFromCart(productId)
    }
    
    fun clearCart() {
        cartManager.clearCart()
    }
    
    fun checkout() {
        // In a real app, this would process the order
        // For now, just clear the cart to simulate completion
        cartManager.clearCart()
    }
}

class OrderViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 