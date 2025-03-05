package com.example.alcoholapp.data

import com.example.alcoholapp.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class CategoryProductsResponse(
    val products: List<Product>
)

class ProductService {
    fun getProductsByCategory(categoryName: String): CategoryProductsResponse {
        val products = when (categoryName.lowercase()) {
            "wine" -> listOf(
                Product("1", "Red Wine", "Premium red wine", 29.99, "http://10.0.2.2:8081/images/wines/wine1.png", "1"),
                Product("2", "White Wine", "Crisp white wine", 24.99, "http://10.0.2.2:8081/images/wines/wine2.png", "1"),
                Product("3", "Rose Wine", "Refreshing rose wine", 19.99, "http://10.0.2.2:8081/images/wines/wine3.png", "1")
            )
            "beer" -> listOf(
                Product("4", "Lager Beer", "Classic lager", 8.99, "http://10.0.2.2:8081/images/Beer/beer1.png", "2"),
                Product("5", "IPA Beer", "Hoppy IPA", 9.99, "http://10.0.2.2:8081/images/Beer/beer2.png", "2"),
                Product("6", "Stout Beer", "Rich stout", 7.99, "http://10.0.2.2:8081/images/Beer/beer3.png", "2")
            )
            "whiskey" -> listOf(
                Product("7", "Single Malt", "Premium single malt", 59.99, "http://10.0.2.2:8081/images/Whiskey/whiskey1.png", "3"),
                Product("8", "Bourbon", "Kentucky bourbon", 49.99, "http://10.0.2.2:8081/images/Whiskey/whiskey2.png", "3"),
                Product("9", "Scotch", "Aged scotch", 69.99, "http://10.0.2.2:8081/images/Whiskey/whiskey3.png", "3")
            )
            "vodka" -> listOf(
                Product("10", "Classic Vodka", "Pure vodka", 34.99, "http://10.0.2.2:8081/images/vodka/vodka1.png", "4"),
                Product("11", "Flavored Vodka", "Infused vodka", 29.99, "http://10.0.2.2:8081/images/vodka/vodka2.png", "4"),
                Product("12", "Premium Vodka", "Top shelf vodka", 44.99, "http://10.0.2.2:8081/images/vodka/vodka3.png", "4")
            )
            "rum" -> listOf(
                Product("13", "Dark Rum", "Aged dark rum", 27.99, "http://10.0.2.2:8081/images/rum/rum1.png", "5"),
                Product("14", "Spiced Rum", "Caribbean spiced rum", 25.99, "http://10.0.2.2:8081/images/rum/rum2.png", "5"),
                Product("15", "White Rum", "Light white rum", 22.99, "http://10.0.2.2:8081/images/rum/rum3.png", "5")
            )
            else -> emptyList()
        }
        return CategoryProductsResponse(products)
    }
}