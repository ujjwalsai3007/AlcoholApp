package com.example.alcoholapp.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        // Configure Hikari connection pool
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/alcoholapp"
            username = System.getenv("DATABASE_USER") ?: "postgres"
            password = System.getenv("DATABASE_PASSWORD") ?: "postgres"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        // Create tables and initialize with sample data
        val databaseInitializer = DatabaseInitializer()
        databaseInitializer.initializeDatabase()
    }
}