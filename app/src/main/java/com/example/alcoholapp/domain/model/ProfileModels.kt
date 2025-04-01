package com.example.alcoholapp.domain.model

data class Address(
    val id: String,
    val label: String,
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val isDefault: Boolean = false
) {
    fun getFormattedAddress(): String {
        return "$street\n$city, $state $postalCode"
    }
}

data class OrderHistory(
    val orderId: String,
    val date: String,
    val total: Double,
    val itemCount: Int,
    val status: String
)

data class PaymentMethod(
    val id: String,
    val cardType: String,
    val lastFourDigits: String,
    val expiryDate: String,
    val isDefault: Boolean = false
)

data class ProfileSection(
    val title: String,
    val icon: Int,
    val actionType: ProfileActionType
)

enum class ProfileActionType {
    PROFILE_INFO,
    ADDRESSES,
    PAYMENT_METHODS,
    ORDER_HISTORY,
    PREFERENCES,
    HELP_SUPPORT,
    SIGN_OUT
} 