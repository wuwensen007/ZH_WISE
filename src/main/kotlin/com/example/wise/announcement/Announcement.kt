package com.example.wise.announcement

import com.example.wise.WiseApplication
import com.example.wise.repository.Fabs
import com.example.wise.repository.Notes
import com.example.wise.updatesystem.FAB
import com.example.wise.util.LocalDB
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime


class Announcement {

    // 发布公告
    fun releaseAnnouncement(noteProperty: NoteProperty){

        // 保存数据
        transaction(LocalDB.db) {

            val factory = Factory.find { Fabs.fab_name eq noteProperty.factory.value.name }.first()

            Note.new {
                noteinfo = """${noteProperty.noteTitle.valueSafe}#${noteProperty.noteBody.valueSafe}"""
                createtime = LocalDateTime.now()
                type = noteProperty.type.valueSafe
                fabid = factory.id.value
            }

        }
        if (noteProperty.type.valueSafe == "更新公告"){
            // 设置远程ftp的csv文件某字段值为强制更新 TODO

        }
        // 展示公告数据
        displayTheLatestAnnouncements(WiseApplication.curFAB.value)

    }

    fun displayTheLatestAnnouncements(fab: FAB){

        // 查询公告 TODO
        var notes = emptyList<Note>()
        transaction(LocalDB.db) {
            val factory = Factory.find { Fabs.fab_name eq fab.name }.first()
            notes = Note.all().sortedByDescending { it.createtime }
        }
        WiseApplication.curNotes.value.setAll(notes)

    }

}