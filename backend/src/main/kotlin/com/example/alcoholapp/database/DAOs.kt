package com.example.alcoholapp.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.alcoholapp.model.*

class CategoryDAO {
    fun getAllCategories(): List<Category> {
        // Hardcoded categories with absolute URLs
        return listOf(
            Category("1", "Wine", "/static/images/wine.png"),
            Category("2", "Beer", "/static/images/beer.png"),
            Category("3", "Whiskey", "/static/images/whiskey.png"),
            Category("4", "Vodka", "/static/images/vodka.png"),
            Category("5", "Rum", "/static/images/rum.png"),
            Category("6", "Cider", "/static/images/cider.png")
        )
    }
}

class BrandDAO {
    fun getAllBrands(): List<Brand> = transaction {
        Brands.selectAll().map { row ->
            Brand(
                id = row[Brands.id].toString(),
                name = row[Brands.name],
                imageUrl = row[Brands.imageUrl]
            )
        }
    }
}

class ProductDAO {
    fun getLimitedEditionProducts(): List<Product> = transaction {
        Products
            .select { Products.isLimitedEdition eq true }
            .map { row ->
                Product(
                    id = row[Products.id].toString(),
                    name = row[Products.name],
                    price = row[Products.price].toDouble(),
                    imageUrl = row[Products.imageUrl],
                    description = row[Products.description],
                    categoryId = row[Products.categoryId]
                )
            }
    }
}

class BannerDAO {
    fun getAllBanners(): List<Banner> = transaction {
        Banners.selectAll().map { row ->
            Banner(
                id = row[Banners.id].toString(),
                imageUrl = row[Banners.imageUrl],
                targetUrl = row[Banners.targetUrl]
            )
        }
    }
}