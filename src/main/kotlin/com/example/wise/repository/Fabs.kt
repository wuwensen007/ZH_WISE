package com.example.wise.repository

import org.jetbrains.exposed.dao.id.IntIdTable

object Fabs: IntIdTable("fab") {

    val fab_name = varchar("fab_name", length = 25)

    val desc = text("desc")
}