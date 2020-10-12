package com.example.wise.util

import com.example.wise.config.Constant
import org.jetbrains.exposed.sql.Database

object LocalDB {

    val db by lazy {
        Database.connect(
                url = Constant.LOCAL_DB_URL,
                driver = Constant.LOCAL_DB_DRIVER,
                user = Constant.LOCAL_DB_USER,
                password = Constant.LOCAL_DB_PASSWORD)
    }
}