package com.example.wise.repository

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Notes: IntIdTable("wise_note") {

    val noteinfo = text("noteinfo")

    val createtime = datetime("createtime")

    val fabid = integer("fabid").nullable()

    var type = varchar("type", length = 50)
}