package com.example.wise.common

import com.example.wise.WiseApplication
import com.example.wise.announcement.Announcement
import com.example.wise.announcement.Note
import com.example.wise.announcement.NoteProperty
import com.example.wise.updatesystem.FAB

sealed class SystemCommand: Command {
    // opi启动命令
    object OPIStartCommand : SystemCommand() {

        override fun execute() {
            WiseApplication.runSubSystem("OPI")
        }
    }
    // opi打开文档命令
    object OPIOpenDocCommand: SystemCommand(){
        override fun execute() {
            WiseApplication.openSubSystemDocumentation("OPI")
        }
    }

    // SM启动命令
    object SMStartCommand : SystemCommand() {
        override fun execute() {
            WiseApplication.runSubSystem("SM")
        }
    }

    // opi打开文档命令
    object SMOpenDocCommand: SystemCommand(){
        override fun execute() {
            WiseApplication.openSubSystemDocumentation("SM")
        }
    }

    // ONALL启动命令
    object ONCALLStartCommand : SystemCommand() {
        override fun execute() {
            WiseApplication.runSubSystem("ONCALL")
        }
    }

    // ONCALL打开文档命令
    object ONCALLOpenDocCommand : SystemCommand() {
        override fun execute() {
            WiseApplication.openSubSystemDocumentation("ONCALL")
        }
    }

    // SPC启动命令
    object SPCStartCommand : SystemCommand() {
        override fun execute() {
            WiseApplication.runSubSystem("SPC")
        }
    }

    // SPC打开文档命令
    object SPCOpenDocCommand : SystemCommand() {
        override fun execute() {
            WiseApplication.openSubSystemDocumentation("SPC")
        }
    }

    // CFM启动命令
    object CFMStartCommand : SystemCommand() {
        override fun execute() {
            WiseApplication.runSubSystem("CFM")
        }
    }


    // REPORT启动命令
    object ReportStartCommand : SystemCommand() {
        override fun execute() {
            WiseApplication.runSubSystem("REPORT")
        }
    }

    // REPORT启动命令
    object PMSStartCommand : SystemCommand() {
        override fun execute() {
            WiseApplication.runSubSystem("PMS")
        }
    }


    object NoCommand: SystemCommand(){
        override fun execute(){}
    }

    object UpdateSystemCommand: SystemCommand(){
        override fun execute() {
            WiseApplication.startUpdateSubSystems()
        }
    }

    object ForcedToUpdateSystemCommand: SystemCommand(){
        override fun execute() {
            WiseApplication.startForcedToUpdateSubSystems()
        }

    }

    class ReleaseNoteCommand(val noteProperty: NoteProperty) : SystemCommand() {
        override fun execute() {
            println("公告类型:----> ${noteProperty.type}")
            val announcement = Announcement()
            announcement.releaseAnnouncement(noteProperty)
        }
    }

    class DisplayNoteCommand(val size: Int, val fab: FAB): SystemCommand(){
        override fun execute() {
            val announcement = Announcement()
            announcement.displayTheLatestAnnouncements(fab)
        }

    }

}