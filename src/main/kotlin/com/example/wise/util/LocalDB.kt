package com.example.wise.util

import com.example.wise.config.Constant
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object LocalDB {

    val db by lazy {
        val config = HikariConfig().apply {
            jdbcUrl         = Constant.LOCAL_DB_URL
            driverClassName = Constant.LOCAL_DB_DRIVER
            username        = Constant.LOCAL_DB_USER
            password        = Constant.LOCAL_DB_PASSWORD
            maximumPoolSize = 10
        }
        val dataSource = HikariDataSource(config)

        Database.connect(dataSource)
    }
}