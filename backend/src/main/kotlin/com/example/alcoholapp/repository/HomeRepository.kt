package com.example.alcoholapp.repository

import com.example.alcoholapp.model.*

class HomeRepository {
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
                    "3"
                ),
                Product(
                    "2",
                    "Black Label Reserve",
                    "Special Release",
                    29.99,
                    "http://10.0.2.2:8081/static/images/limited/blacklabel.png",
                    "3"
                ),
                Product(
                    "3",
                    "Bacardi Gold Reserve",
                    "Limited Edition Rum",
                    45.99,
                    "http://10.0.2.2:8081/static/images/limited/bacardi-gold.png",
                    "5"
                ),
                Product(
                    "4",
                    "Taylor's Vintage Port",
                    "Collector's Edition",
                    89.99,
                    "http://10.0.2.2:8081/static/images/limited/taylors-vintage.png",
                    "1"
                ),
                Product(
                    "5",
                    "Crystal Head Vodka",
                    "Aurora Special Edition",
                    65.99,
                    "http://10.0.2.2:8081/static/images/limited/crystal-head.png",
                    "4"
                )
            )
        )
    }
}