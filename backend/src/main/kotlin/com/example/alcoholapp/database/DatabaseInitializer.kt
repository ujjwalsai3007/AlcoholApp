package com.example.alcoholapp.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseInitializer {
    fun initializeDatabase() {
        transaction {
            // Create tables if they don't exist
            SchemaUtils.create(Categories, Brands, Products, Banners)

            // Insert sample categories if table is empty
            if (Categories.selectAll().count() == 0L) {
                Categories.insert {
                    it[id] = "1"
                    it[name] = "Wine"
                    it[imageUrl] = "/static/images/wine.png"
                }
                Categories.insert {
                    it[id] = "2"
                    it[name] = "Beer"
                    it[imageUrl] = "/static/images/beer.png"
                }
                Categories.insert {
                    it[id] = "3"
                    it[name] = "Whiskey"
                    it[imageUrl] = "/static/images/whiskey.png"
                }
                Categories.insert {
                    it[id] = "4"
                    it[name] = "Vodka"
                    it[imageUrl] = "/static/images/vodka.png"
                }
                Categories.insert {
                    it[id] = "5"
                    it[name] = "Rum"
                    it[imageUrl] = "/static/images/rum.png"
                }
            }

            // Insert sample brands if table is empty
            if (Brands.selectAll().count() == 0L) {
                Brands.insert {
                    it[id] = "1"
                    it[name] = "Jack Daniel's"
                    it[imageUrl] = "/static/images/brands/jack_daniels.png"
                }
                Brands.insert {
                    it[id] = "2"
                    it[name] = "Absolut"
                    it[imageUrl] = "/static/images/brands/absolut.png"
                }
                Brands.insert {
                    it[id] = "3"
                    it[name] = "Heineken"
                    it[imageUrl] = "/static/images/brands/heineken.png"
                }
            }

            // Insert sample products if table is empty
            if (Products.selectAll().count() == 0L) {
                Products.insert {
                    it[name] = "Jack Daniel's Old No. 7"
                    it[price] = 35.99.toBigDecimal()
                    it[imageUrl] = "/static/images/products/jack_daniels_old_no7.png"
                    it[description] = "The world-famous Tennessee whiskey with a smooth character and flavors of vanilla, toasted oak and caramel."
                    it[categoryId] = "3" // Whiskey category
                    it[brandId] = "1" // Jack Daniel's brand
                    it[isLimitedEdition] = true
                }
                Products.insert {
                    it[name] = "Absolut Vodka Limited Edition"
                    it[price] = 29.99.toBigDecimal()
                    it[imageUrl] = "/static/images/products/absolut_limited.png"
                    it[description] = "Special edition bottle of premium Swedish vodka with unique design."
                    it[categoryId] = "4" // Vodka category
                    it[brandId] = "2" // Absolut brand
                    it[isLimitedEdition] = true
                }
            }

            // Insert sample banners if table is empty
            if (Banners.selectAll().count() == 0L) {
                Banners.insert {
                    it[id] = "1"
                    it[imageUrl] = "/static/images/banners/summer_sale.png"
                    it[targetUrl] = "/promotions/summer-sale"
                }
                Banners.insert {
                    it[id] = "2"
                    it[imageUrl] = "/static/images/banners/new_arrivals.png"
                    it[targetUrl] = "/new-arrivals"
                }
            }
        }
    }
}