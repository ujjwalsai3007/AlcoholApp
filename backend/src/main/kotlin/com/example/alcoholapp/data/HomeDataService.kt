package com.example.alcoholapp.data

import com.example.alcoholapp.model.*
import com.example.alcoholapp.database.*

class HomeDataService {
    private val categoryDAO = CategoryDAO()
    private val brandDAO = BrandDAO()
    private val bannerDAO = BannerDAO()
    private val productDAO = ProductDAO()

    fun getHomeData(): HomeResponse {
        return HomeResponse(
            categories = listOf(
                Category("1", "Wine", "http://10.0.2.2:8081/static/images/wine.png"),
                Category("2", "Beer", "http://10.0.2.2:8081/static/images/beer.png"),
                Category("3", "Whiskey", "http://10.0.2.2:8081/static/images/whiskey.png"),
                Category("4", "Vodka", "http://10.0.2.2:8081/static/images/vodka.png"),
                Category("5", "Rum", "http://10.0.2.2:8081/static/images/rum.png")
            ),
            brands = listOf(
                Brand("1", "Jim Beam", "http://10.0.2.2:8081/static/images/brands/JimBeam.png"),
                Brand("2", "Smirnoff", "http://10.0.2.2:8081/static/images/brands/Smirnoff.png"),
                Brand("3", "Corona", "http://10.0.2.2:8081/static/images/brands/corona.png"),
                Brand("4", "BlueZone", "http://10.0.2.2:8081/static/images/brands/BlueZone.png"),
                Brand("5", "Kingfisher", "http://10.0.2.2:8081/static/images/brands/Kingfisher.png"),
                Brand("6", "McDowell's", "http://10.0.2.2:8081/static/images/brands/McDowells.png"),
                Brand("7", "RedBull", "http://10.0.2.2:8081/static/images/brands/RedBull.png"),
                Brand("8", "Bacardi", "http://10.0.2.2:8081/static/images/brands/Bacardi.png")
            ),
            banners = listOf(
                Banner("1", "http://10.0.2.2:8081/static/images/alcoholbanner.png", "https://example.com/promo1")
            ),
            limitedEditionProducts = listOf(
                Product(
                    "1",
                    "Johnnie Walker Blue Label",
                    "Limited Edition Whiskey",
                    35.99,
                    "http://10.0.2.2:8081/static/images/limited/Jonniewalker.png",
                    "3",
                    true,
                    0,
                    4.5,
                    128,
                    true
                ),
                Product(
                    "2",
                    "Black Label Reserve",
                    "Special Release",
                    29.99,
                    "http://10.0.2.2:8081/static/images/limited/blacklabel.png",
                    "3",
                    true,
                    0,
                    4.3,
                    98,
                    true
                ),
                Product(
                    "3",
                    "Bacardi Gold Reserve",
                    "Limited Edition Rum",
                    45.99,
                    "http://10.0.2.2:8081/static/images/limited/bacardi-gold.png",
                    "5",
                    true,
                    0,
                    4.7,
                    156,
                    true
                ),
                Product(
                    "4",
                    "Taylor's Vintage Port",
                    "Collector's Edition",
                    89.99,
                    "http://10.0.2.2:8081/static/images/limited/taylors-vintage.png",
                    "1",
                    true,
                    0,
                    4.8,
                    78,
                    true
                ),
                Product(
                    "5",
                    "Crystal Head Vodka",
                    "Aurora Special Edition",
                    65.99,
                    "http://10.0.2.2:8081/static/images/limited/crystal-head.png",
                    "4",
                    true,
                    0,
                    4.6,
                    112,
                    true
                )
            ),
            categoryProducts = mapOf(
                "1" to listOf( // Wine
                    Product(
                        "w1", "Cabernet Sauvignon", "Rich, full-bodied red wine", 
                        24.99, "http://10.0.2.2:8081/static/images/wine/cabernet.png", "1",
                        true, 0, 4.4, 89
                    ),
                    Product(
                        "w2", "Chardonnay", "Classic white wine", 
                        19.99, "http://10.0.2.2:8081/static/images/wine/chardonnay.png", "1",
                        true, 0, 4.2, 76
                    ),
                    Product(
                        "w3", "Merlot", "Smooth red wine", 
                        22.99, "http://10.0.2.2:8081/static/images/wine/merlot.png", "1",
                        true, 0, 4.3, 92
                    )
                ),
                "2" to listOf( // Beer
                    Product(
                        "b1", "Corona Extra", "Mexican pale lager", 
                        8.99, "http://10.0.2.2:8081/static/images/beer/corona.png", "2",
                        true, 0, 4.5, 156
                    ),
                    Product(
                        "b2", "Heineken", "Premium lager beer", 
                        9.99, "http://10.0.2.2:8081/static/images/beer/heineken.png", "2",
                        true, 0, 4.4, 143
                    ),
                    Product(
                        "b3", "Budweiser", "American-style lager", 
                        7.99, "http://10.0.2.2:8081/static/images/beer/budweiser.png", "2",
                        true, 0, 4.3, 178
                    )
                ),
                "3" to listOf( // Whiskey
                    Product(
                        "wh1", "Jack Daniel's", "Tennessee whiskey", 
                        29.99, "http://10.0.2.2:8081/static/images/whiskey/jackdaniels.png", "3",
                        true, 0, 4.6, 234
                    ),
                    Product(
                        "wh2", "Jameson", "Irish whiskey", 
                        27.99, "http://10.0.2.2:8081/static/images/whiskey/jameson.png", "3",
                        true, 0, 4.5, 198
                    ),
                    Product(
                        "wh3", "Glenfiddich", "Single malt scotch", 
                        45.99, "http://10.0.2.2:8081/static/images/whiskey/glenfiddich.png", "3",
                        true, 0, 4.7, 167
                    )
                ),
                "4" to listOf( // Vodka
                    Product(
                        "v1", "Grey Goose", "French vodka", 
                        32.99, "http://10.0.2.2:8081/static/images/vodka/greygoose.png", "4",
                        true, 0, 4.5, 189
                    ),
                    Product(
                        "v2", "Absolut", "Swedish vodka", 
                        24.99, "http://10.0.2.2:8081/static/images/vodka/absolut.png", "4",
                        true, 0, 4.4, 176
                    ),
                    Product(
                        "v3", "Belvedere", "Polish vodka", 
                        36.99, "http://10.0.2.2:8081/static/images/vodka/belvedere.png", "4",
                        true, 0, 4.6, 145
                    )
                ),
                "5" to listOf( // Rum
                    Product(
                        "r1", "Captain Morgan", "Spiced rum", 
                        19.99, "http://10.0.2.2:8081/static/images/rum/captainmorgan.png", "5",
                        true, 0, 4.3, 212
                    ),
                    Product(
                        "r2", "Malibu", "Coconut rum", 
                        17.99, "http://10.0.2.2:8081/static/images/rum/malibu.png", "5",
                        true, 0, 4.2, 187
                    ),
                    Product(
                        "r3", "Havana Club", "Cuban rum", 
                        23.99, "http://10.0.2.2:8081/static/images/rum/havanaclub.png", "5",
                        true, 0, 4.5, 156
                    )
                )
            )
        )
    }
}