package com.example.alcoholapp.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table

object Categories : Table() {
    val id = varchar("id", 50)
    val name = varchar("name", 100)
    val imageUrl = varchar("image_url", 255)
    
    override val primaryKey = PrimaryKey(id)
}

object Brands : Table() {
    val id = varchar("id", 50)
    val name = varchar("name", 100)
    val imageUrl = varchar("image_url", 255)
    
    override val primaryKey = PrimaryKey(id)
}

object Products : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val price = decimal("price", 10, 2)
    val imageUrl = varchar("image_url", 255)
    val description = text("description")
    val categoryId = reference("category_id", Categories.id)
    val brandId = reference("brand_id", Brands.id)
    val isLimitedEdition = bool("is_limited_edition")
    
    override val primaryKey = PrimaryKey(id)
}

object Banners : Table() {
    val id = varchar("id", 50)
    val imageUrl = varchar("image_url", 255)
    val targetUrl = varchar("target_url", 255)
    
    override val primaryKey = PrimaryKey(id)
}