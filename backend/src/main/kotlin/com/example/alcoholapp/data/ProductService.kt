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
                Product("w1", "Cabernet Sauvignon", "Rich, full-bodied red wine", 24.99, "http://10.0.2.2:8081/static/images/wines/wine1.jpg", "1", true, 0, 4.4, 89),
                Product("w2", "Chardonnay", "Classic white wine", 19.99, "http://10.0.2.2:8081/static/images/wines/wine2.jpg", "1", true, 0, 4.2, 76),
                Product("w3", "Merlot", "Smooth red wine", 22.99, "http://10.0.2.2:8081/static/images/wines/wine3.jpg", "1", true, 0, 4.3, 92)
            )
            "beer" -> listOf(
                Product("b1", "Corona Extra", "Mexican pale lager", 8.99, "http://10.0.2.2:8081/static/images/Beer/beer1.jpg", "2", true, 0, 4.5, 156),
                Product("b2", "Heineken", "Premium lager beer", 9.99, "http://10.0.2.2:8081/static/images/Beer/beer2.jpg", "2", true, 0, 4.4, 143),
                Product("b3", "Budweiser", "American-style lager", 7.99, "http://10.0.2.2:8081/static/images/Beer/beer3.jpg", "2", true, 0, 4.3, 178)
            )
            "whiskey" -> listOf(
                Product("wh1", "Jack Daniel's", "Tennessee whiskey", 29.99, "http://10.0.2.2:8081/static/images/Whiskey/whiskey1.jpg", "3", true, 0, 4.6, 234),
                Product("wh2", "Jameson", "Irish whiskey", 27.99, "http://10.0.2.2:8081/static/images/Whiskey/whiskey2.jpg", "3", true, 0, 4.5, 198),
                Product("wh3", "Glenfiddich", "Single malt scotch", 45.99, "http://10.0.2.2:8081/static/images/Whiskey/whiskey3.jpg", "3", true, 0, 4.7, 167)
            )
            "vodka" -> listOf(
                Product("v1", "Grey Goose", "French vodka", 32.99, "http://10.0.2.2:8081/static/images/vodka/vodka1.jpg", "4", true, 0, 4.5, 189),
                Product("v2", "Absolut", "Swedish vodka", 24.99, "http://10.0.2.2:8081/static/images/vodka/vodka2.jpg", "4", true, 0, 4.4, 176),
                Product("v3", "Belvedere", "Polish vodka", 36.99, "http://10.0.2.2:8081/static/images/vodka/vodka3.jpg", "4", true, 0, 4.6, 145)
            )
            "rum" -> listOf(
                Product("r1", "Captain Morgan", "Spiced rum", 19.99, "http://10.0.2.2:8081/static/images/rum/rum1.jpg", "5", true, 0, 4.3, 212),
                Product("r2", "Malibu", "Coconut rum", 17.99, "http://10.0.2.2:8081/static/images/rum/rum2.jpg", "5", true, 0, 4.2, 187),
                Product("r3", "Havana Club", "Cuban rum", 23.99, "http://10.0.2.2:8081/static/images/rum/rum3.jpg", "5", true, 0, 4.5, 156)
            )
            else -> emptyList()
        }
        return CategoryProductsResponse(products)
    }
}