package com.example.wise.announcement

import com.example.wise.repository.Notes
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.lang.StringBuilder
import java.time.format.DateTimeFormatter


class Note(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<Note>(Notes)

    var noteinfo by Notes.noteinfo
    var createtime by Notes.createtime
    var type by Notes.type
    var fabid by Notes.fabid

    override fun toString(): String{
        val content = StringBuilder()

        noteinfo.split("#")[1].forEachIndexed { index, c ->
            if (index % 70 == 0 && index != 0){
                content.append("\n")
            }
            content.append(c)
        }

        return """公告类型: ${type}${"\t\t"}创建时间: ${createtime.format(DateTimeFormatter.ofPattern("YYYY年MM月dd日 HH时mm分ss秒"))}
                            |工厂: ${fabid}
                            |标题: ${noteinfo.split("#")[0]}${"\n"}内容:${"\n"} $content
                        """.trimMargin()
    }


}