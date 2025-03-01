package com.example.models

val mockProducts = listOf(
    Product(
        id = "1",
        name = "Premium Beer",
        description = "Smooth, crisp lager with a balanced taste",
        price = 5.99,
        imageUrl = "https://picsum.photos/400/300?random=1",
        category = "Beer"
    ),
    Product(
        id = "2",
        name = "Red Wine",
        description = "Full-bodied red wine with rich flavors",
        price = 19.99,
        imageUrl = "https://picsum.photos/400/300?random=2",
        category = "Wine"
    ),
    Product(
        id = "3",
        name = "Vodka Premium",
        description = "Triple-distilled premium vodka",
        price = 24.99,
        imageUrl = "https://picsum.photos/400/300?random=3",
        category = "Vodka"
    )
)

val mockBanners = listOf(
    Banner(
        id = "1",
        imageUrl = "https://picsum.photos/800/300?random=4",
        targetUrl = "/products/special-offers"
    ),
    Banner(
        id = "2",
        imageUrl = "https://picsum.photos/800/300?random=5",
        targetUrl = "/products/new-arrivals"
    )
)