package com.example.alcoholapp.presentation.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.alcoholapp.data.FirebaseAuthManager
import com.example.alcoholapp.data.CartManager
import com.example.alcoholapp.domain.model.Address
import com.example.alcoholapp.domain.model.OrderHistory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val context: Context) : ViewModel() {
    
    private val TAG = "ProfileViewModel"
    private val authManager = FirebaseAuthManager.getInstance()
    private val cartManager = CartManager.getInstance()
    
    // User profile state
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail
    
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName
    
    private val _phoneNumber = MutableStateFlow<String?>(null)
    val phoneNumber: StateFlow<String?> = _phoneNumber
    
    private val _isVerified = MutableStateFlow(false)
    val isVerified: StateFlow<Boolean> = _isVerified
    
    // Address state
    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses
    
    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> = _selectedAddress
    
    // Order history state
    private val _orderHistory = MutableStateFlow<List<OrderHistory>>(emptyList())
    val orderHistory: StateFlow<List<OrderHistory>> = _orderHistory
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        loadUserProfile()
        loadAddresses()
        loadOrderHistory()
    }
    
    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val user = authManager.getCurrentUser()
                if (user != null) {
                    _userEmail.value = user.email
                    _userName.value = user.displayName
                    _phoneNumber.value = user.phoneNumber
                    _isVerified.value = user.isEmailVerified
                    Log.d(TAG, "User profile loaded: ${user.email}")
                } else {
                    Log.d(TAG, "No user logged in")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user profile", e)
                _error.value = "Failed to load profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadAddresses() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // For demo purposes, we're using mock data
                delay(500) // Simulate network delay
                _addresses.value = listOf(
                    Address("1", "Home", "1633 Hampton Rd", "Toronto", "ON", "M4C 6Q2", true),
                    Address("2", "Work", "2200 Yonge St", "Toronto", "ON", "M4S 2C6", false),
                    Address("3", "Cottage", "45 Lake Shore Rd", "Huntsville", "ON", "P1H 0B8", false)
                )
                _selectedAddress.value = _addresses.value.find { it.isDefault }
                Log.d(TAG, "Addresses loaded: ${_addresses.value.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading addresses", e)
                _error.value = "Failed to load addresses: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadOrderHistory() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // For demo purposes, we're using mock data
                delay(800) // Simulate network delay
                _orderHistory.value = listOf(
                    OrderHistory("ORD10045", "2023-04-01", 86.97, 3, "Delivered"),
                    OrderHistory("ORD10032", "2023-03-15", 45.99, 1, "Delivered"),
                    OrderHistory("ORD10018", "2023-02-28", 132.45, 5, "Delivered"),
                    OrderHistory("ORD10005", "2023-02-14", 75.50, 2, "Cancelled")
                )
                Log.d(TAG, "Order history loaded: ${_orderHistory.value.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading order history", e)
                _error.value = "Failed to load order history: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateUserName(name: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // In a real app, this would call an API to update the name
                delay(500) // Simulate network delay
                _userName.value = name
                Log.d(TAG, "User name updated: $name")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating name", e)
                _error.value = "Failed to update name: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updatePhoneNumber(phone: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // In a real app, this would call an API to update the phone
                delay(500) // Simulate network delay
                _phoneNumber.value = phone
                Log.d(TAG, "Phone number updated: $phone")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating phone", e)
                _error.value = "Failed to update phone: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addAddress(address: Address) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // In a real app, this would call an API to add the address
                delay(500) // Simulate network delay
                val newAddress = address.copy(id = (addresses.value.size + 1).toString())
                _addresses.value = _addresses.value + newAddress
                
                if (newAddress.isDefault) {
                    // Update the default address
                    _addresses.value = _addresses.value.map { 
                        it.copy(isDefault = it.id == newAddress.id) 
                    }
                    _selectedAddress.value = newAddress
                }
                
                Log.d(TAG, "Address added: ${newAddress.id}")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding address", e)
                _error.value = "Failed to add address: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateAddress(address: Address) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // In a real app, this would call an API to update the address
                delay(500) // Simulate network delay
                _addresses.value = _addresses.value.map { 
                    if (it.id == address.id) address else it 
                }
                
                if (address.isDefault) {
                    // Update the default address
                    _addresses.value = _addresses.value.map { 
                        it.copy(isDefault = it.id == address.id) 
                    }
                    _selectedAddress.value = address
                }
                
                Log.d(TAG, "Address updated: ${address.id}")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating address", e)
                _error.value = "Failed to update address: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // In a real app, this would call an API to delete the address
                delay(500) // Simulate network delay
                
                val addressToDelete = _addresses.value.find { it.id == addressId }
                _addresses.value = _addresses.value.filter { it.id != addressId }
                
                // If the deleted address was the default one, set a new default
                if (addressToDelete?.isDefault == true && _addresses.value.isNotEmpty()) {
                    val newDefault = _addresses.value.first()
                    _addresses.value = _addresses.value.map { 
                        it.copy(isDefault = it.id == newDefault.id) 
                    }
                    _selectedAddress.value = newDefault
                }
                
                Log.d(TAG, "Address deleted: $addressId")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting address", e)
                _error.value = "Failed to delete address: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun setDefaultAddress(addressId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // In a real app, this would call an API to set the default address
                delay(500) // Simulate network delay
                
                _addresses.value = _addresses.value.map { 
                    it.copy(isDefault = it.id == addressId) 
                }
                _selectedAddress.value = _addresses.value.find { it.id == addressId }
                
                Log.d(TAG, "Default address set: $addressId")
            } catch (e: Exception) {
                Log.e(TAG, "Error setting default address", e)
                _error.value = "Failed to set default address: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun signOut() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                authManager.signOut()
                cartManager.clearCart()
                _userEmail.value = null
                _userName.value = null
                _phoneNumber.value = null
                _isVerified.value = false
                Log.d(TAG, "User signed out")
            } catch (e: Exception) {
                Log.e(TAG, "Error signing out", e)
                _error.value = "Failed to sign out: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 