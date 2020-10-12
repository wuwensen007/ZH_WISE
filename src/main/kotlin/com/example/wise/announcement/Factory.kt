package com.example.wise.announcement

import com.example.wise.repository.Fabs
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class Factory(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<Factory>(Fabs)
    var fab_name by Fabs.fab_name
    var desc by Fabs.desc
}
