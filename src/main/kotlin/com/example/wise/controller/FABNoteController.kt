package com.example.wise.controller

import com.example.wise.WiseApplication
import com.example.wise.announcement.Factory
import com.example.wise.announcement.NoteProperty
import com.example.wise.common.Command
import com.example.wise.common.SystemCommand
import com.example.wise.updatesystem.FAB
import com.jfoenix.controls.*
import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.stage.Modality
import java.net.URL
import java.util.*

class FABNoteController: Initializable {


    @FXML lateinit var noteTileTf: JFXTextField

    @FXML lateinit var noteTA: JFXTextArea

    @FXML lateinit var updateBtn: JFXButton

    @FXML lateinit var onlyNoticeBtn: JFXButton

    @FXML lateinit var cancelBtn: JFXButton

    @FXML lateinit var strLenLbl: Label

    private val noteProperty = NoteProperty()

    private var command: Command = SystemCommand.NoCommand

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        noteProperty.noteTitle.bind(noteTileTf.textProperty())
        noteProperty.noteBody.bind(noteTA.textProperty())
        noteProperty.factory.bind(WiseApplication.curFAB)

        cancelBtn.setOnAction {
            noteTileTf.clear()
            noteTA.clear()
        }

        noteTA.editableProperty().bind(Bindings.lessThan(noteTA.textProperty().length(), 500))

        strLenLbl.textProperty().bind(Bindings.concat("当前字数:").concat(noteTA.textProperty().length()))

        updateBtn.setOnAction {

            if (verifyAnnouncementIsComplete()){
                noteProperty.type.set("更新公告")
                command = SystemCommand.ReleaseNoteCommand(noteProperty)
                command.execute()

                val alert = JFXAlert<Any>(updateBtn.scene.window)
                alert.initModality(Modality.APPLICATION_MODAL)
                alert.isOverlayClose = false
                val layout = JFXDialogLayout()
                layout.setHeading(Label("提示"))
                layout.setBody(Label("发布更新公告成功"))
                val closeButton = JFXButton("OK")
                closeButton.setOnAction { alert.hideWithAnimation() }
                layout.setActions(closeButton)
                alert.setContent(layout)
                alert.show()
            }
        }

        onlyNoticeBtn.setOnAction {

            if (verifyAnnouncementIsComplete()){
                noteProperty.type.set("一般公告")
                command = SystemCommand.ReleaseNoteCommand(noteProperty)
                command.execute()

                val alert = JFXAlert<Any>(updateBtn.scene.window)
                alert.initModality(Modality.APPLICATION_MODAL)
                alert.isOverlayClose = false
                val layout = JFXDialogLayout()
                layout.setHeading(Label("提示"))
                layout.setBody(Label("发布一般公告成功"))
                val closeButton = JFXButton("OK")
                closeButton.setOnAction { alert.hideWithAnimation() }
                layout.setActions(closeButton)
                alert.setContent(layout)
                alert.show()
            }


        }

    }


    private fun verifyAnnouncementIsComplete(): Boolean{
        if (noteTileTf.text.isNullOrEmpty() || noteTA.text.isNullOrEmpty()){
            val alert = JFXAlert<Any>(updateBtn.scene.window)
            alert.initModality(Modality.APPLICATION_MODAL)
            alert.isOverlayClose = false
            val layout = JFXDialogLayout()
            layout.setHeading(Label("提示"))
            layout.setBody(Label("请把公告信息填写完整!!"))
            val closeButton = JFXButton("OK")
            closeButton.setOnAction { alert.hideWithAnimation() }
            layout.setActions(closeButton)
            alert.setContent(layout)
            alert.show()
            return false
        }
        return true
    }
}