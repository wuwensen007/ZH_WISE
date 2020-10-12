package com.example.wise.controller

import com.example.wise.WiseApplication
import com.example.wise.announcement.Note
import com.example.wise.common.Command
import com.example.wise.common.SystemCommand
import com.example.wise.factory.TJ12SubSystemFactory
import com.example.wise.factory.YX12SubSystemFactory
import com.example.wise.factory.YX8SubSystemFactory
import com.example.wise.updatesystem.DesktopSubSystem
import com.example.wise.updatesystem.FAB
import com.example.wise.util.RouteUtil
import com.jfoenix.controls.*
import com.sun.javafx.scene.control.behavior.ScrollBarBehavior
import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.binding.When
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Control
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.ScrollBar
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.util.StringConverter
import org.controlsfx.control.TaskProgressView
import java.net.URL
import java.util.*

class IndexController: Initializable {

    @FXML var opiBtn: JFXButton? = null

    @FXML lateinit var smBtn: JFXButton

    @FXML lateinit var spcBtn: JFXButton

    @FXML lateinit var oncallBtn: JFXButton

    @FXML lateinit var reportBtn: JFXButton

    @FXML lateinit var cfmBtn: JFXButton

    @FXML lateinit var pmsBtn: JFXButton

    @FXML lateinit var checkUpdateBtn: JFXButton

    @FXML lateinit var fabComboBox: JFXComboBox<FAB>

    @FXML lateinit var opiVerLink: Hyperlink

    @FXML lateinit var smVerLink: Hyperlink

    @FXML lateinit var spcVerLink: Hyperlink

    @FXML lateinit var oncallVerLink: Hyperlink

    @FXML lateinit var reportVerLink: Hyperlink

    @FXML lateinit var cfmVerLink: Hyperlink

    @FXML lateinit var pmsVerLink: Hyperlink

    @FXML lateinit var opiSopLink: Hyperlink

    @FXML lateinit var smSopLink: Hyperlink

    @FXML lateinit var spcSopLink: Hyperlink

    @FXML lateinit var oncallSopLink: Hyperlink

    @FXML lateinit var notesBtn: JFXButton

    @FXML lateinit var noteListView: JFXListView<Note>

    private var command: Command = SystemCommand.NoCommand

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        fabComboBox.items.addAll(FAB.values())

        fabComboBox.converter = object :StringConverter<FAB>(){
            override fun toString(fab: FAB): String {
                return fab.toString()
            }

            override fun fromString(string: String): FAB {
                return FAB.fromString(string)
            }
        }

        noteListView.itemsProperty().bind(WiseApplication.curNotes)


        noteListView.placeholderProperty().bind(
                When(SimpleIntegerProperty(0)
                                .isEqualTo(noteListView.items.size))
                                .then(Label("当前没有公告"))
                                .otherwise(null as Label?))

        noteListView.setCellFactory {
            object : JFXListCell<Note>(){
                override fun updateItem(item: Note?, empty: Boolean) {
                    if (item == null || empty){
//                        text = null
//                        graphic = null
                    }else{
                        text = item.toString()
                        if (item.type == "一般公告"){
                            graphic = GlyphsDude.createIcon(MaterialDesignIcon.BULLHORN, "5em")
                        }else{
                            graphic = GlyphsDude.createIcon(MaterialDesignIcon.BULLETIN_BOARD, "5em")
                        }

                    }
                }
            }
        }

        noteListView.isExpanded = true

        fabComboBox.selectionModel.select(WiseApplication.curFAB.value)
        WiseApplication.curFAB.unbind()

        // 数据绑定
        WiseApplication.curSubSystems.bind(Bindings.`when`(fabComboBox.selectionModel.selectedItemProperty().isEqualTo(FAB.YX8)).then(SimpleObjectProperty(YX8SubSystemFactory().getSubSystems()))
                .otherwise(Bindings.`when`(fabComboBox.selectionModel.selectedItemProperty().isEqualTo(FAB.YX12)).then(SimpleObjectProperty(YX12SubSystemFactory().getSubSystems()))
                        .otherwise(SimpleObjectProperty(TJ12SubSystemFactory().getSubSystems()))))

        WiseApplication.curDesktopSubSystems.bind(Bindings.`when`(fabComboBox.selectionModel.selectedItemProperty().isEqualTo(FAB.YX8)).then(SimpleObjectProperty(YX8SubSystemFactory().getDesktopSystems()))
                .otherwise(Bindings.`when`(fabComboBox.selectionModel.selectedItemProperty().isEqualTo(FAB.YX12)).then(SimpleObjectProperty(YX12SubSystemFactory().getDesktopSystems()))
                        .otherwise(SimpleObjectProperty(TJ12SubSystemFactory().getDesktopSystems()))))

        // 更新按钮状态
        updateBtnState()




        fabComboBox.selectionModel.selectedItemProperty().addListener { _, _, newV->
            // 更新按钮状态
            updateBtnState()
            WiseApplication.curFAB.value = newV
            initNotes(newV)
        }

        // 按钮属性绑定
        setBtnPropBinding()

        // 设置按钮事件
        setBtnEvent()

        // 设置链接按钮事件
        setLinkBtnEvent()

        // 设置检查更新按钮事件
        setCheckBtnEvent()

        // 设置发布公告按钮事件
        setNotesBtnEvent()

        // 初始化公告列表
        initNotes(WiseApplication.curFAB.value)


        // 如果有强制更新的系统
        if (WiseApplication.haveSubSystemForcedUpdate()){

            Platform.runLater {
                val alert = JFXAlert<Any>(opiBtn?.scene?.window)
                alert.initModality(Modality.APPLICATION_MODAL)
                alert.isOverlayClose = false
                val layout = JFXDialogLayout()
                layout.setHeading(Label("强制更新弹出框"))

                val taskProgressView = TaskProgressView<Task<Void>>()

                taskProgressView.tasks.setAll(WiseApplication.getUpdateTasks())

                layout.setBody(taskProgressView)

                val closeButton = JFXButton("OK")
                closeButton.styleClass.add("dialog-accept")
                closeButton.setOnAction { alert.hideWithAnimation() }
                layout.setActions(closeButton)

                // 状态绑定
                closeButton.disableProperty().bind(Bindings.isNotEmpty(taskProgressView.tasks))

                alert.setContent(layout)
                alert.show()
                command = SystemCommand.ForcedToUpdateSystemCommand
                command.execute()
            }

        }else{
            Platform.runLater{
                showTipInfo("友情提示", "如果要更新系统,请在更新子应用前请关闭子应用,谢谢配合!!!")
            }
        }

    }



    private fun setBtnPropBinding(){

        if (WiseApplication.haveSubSystem("OPI")){
            opiVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("OPI"))
        }
        if (WiseApplication.haveSubSystem("SM")){
            smVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("SM"))
        }
        if (WiseApplication.haveSubSystem("SPC")){
            spcVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("SPC"))
        }
        if (WiseApplication.haveSubSystem("ONCALL")){
            oncallVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("ONCALL"))
        }
        if (WiseApplication.haveSubSystem("CFM")){
            cfmVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("CFM"))
        }
        if (WiseApplication.haveSubSystem("REPORT")){
            reportVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("REPORT"))
        }
        if (WiseApplication.haveSubSystem("PMS")){
            pmsVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("PMS"))
        }


        opiVerLink.visibleProperty().bind(opiBtn?.visibleProperty())
        smVerLink.visibleProperty().bind(smBtn.visibleProperty())
        spcVerLink.visibleProperty().bind(spcBtn.visibleProperty())
        oncallVerLink.visibleProperty().bind(oncallBtn.visibleProperty())
        cfmVerLink.visibleProperty().bind(cfmBtn.visibleProperty())
        reportVerLink.visibleProperty().bind(reportBtn.visibleProperty())
        pmsVerLink.visibleProperty().bind(pmsBtn.visibleProperty())

        opiSopLink.visibleProperty().bind(opiBtn?.visibleProperty())
        smSopLink.visibleProperty().bind(smBtn.visibleProperty())
        spcSopLink.visibleProperty().bind(spcBtn.visibleProperty())
        oncallSopLink.visibleProperty().bind(oncallBtn.visibleProperty())
    }


    private fun initNotes(fab: FAB){
        command = SystemCommand.DisplayNoteCommand(5, fab = fab)
        command.execute()
    }

    private fun setBtnEvent(){
        opiBtn?.setOnAction {
            command = SystemCommand.OPIStartCommand
            command.execute()
        }

        smBtn.setOnAction {
            command = SystemCommand.SMStartCommand
            command.execute()
        }

        spcBtn.setOnAction {
            command = SystemCommand.SPCStartCommand
            command.execute()
        }

        oncallBtn.setOnAction {
            command = SystemCommand.ONCALLStartCommand
            command.execute()
        }

        cfmBtn.setOnAction {
            command = SystemCommand.CFMStartCommand
            command.execute()
        }

        reportBtn.setOnAction {
            command = SystemCommand.ReportStartCommand
            command.execute()
        }

        pmsBtn.setOnAction {
            command = SystemCommand.PMSStartCommand
            command.execute()
        }
    }




    private fun setLinkBtnEvent(){

        opiSopLink.setOnAction {
            command = SystemCommand.OPIOpenDocCommand
            command.execute()
        }

        smSopLink.setOnAction {
            command = SystemCommand.SMOpenDocCommand
            command.execute()
        }

        oncallSopLink.setOnAction {
            command = SystemCommand.ONCALLOpenDocCommand
            command.execute()
        }

        spcSopLink.setOnAction {
            command = SystemCommand.SPCOpenDocCommand
            command.execute()
        }

    }

    private fun updateBtnState(){

        smBtn.isVisible = WiseApplication.haveSubSystem("SM")
        opiBtn?.isVisible = WiseApplication.haveSubSystem("OPI")
        spcBtn.isVisible = WiseApplication.haveSubSystem("SPC")
        oncallBtn.isVisible = WiseApplication.haveSubSystem("ONCALL")
        cfmBtn.isVisible = WiseApplication.haveSubSystem("CFM")
        reportBtn.isVisible = WiseApplication.haveSubSystem("REPORT")
        pmsBtn.isVisible = WiseApplication.haveSubSystem("PMS")
    }

    private fun setCheckBtnEvent(){
        // 检查各个子系统的版本是否有更新
        checkUpdateBtn.setOnAction {

            if (WiseApplication.haveSubSystemUpdate()){
                val alert = JFXAlert<Any>(opiBtn?.scene?.window)
                alert.initModality(Modality.APPLICATION_MODAL)
                alert.isOverlayClose = false
                val layout = JFXDialogLayout()
                layout.setHeading(Label("Modal Dialog using JFXAlert"))
                layout.setBody(Label("TEST 有系统要更新"))

                val taskProgressView = TaskProgressView<Task<Void>>()

                taskProgressView.tasks.setAll(WiseApplication.getUpdateTasks())

                layout.setBody(taskProgressView)

                val closeButton = JFXButton("OK")
                closeButton.styleClass.add("dialog-accept")
                closeButton.setOnAction { alert.hideWithAnimation() }
                layout.setActions(closeButton)

                // 状态绑定
                closeButton.disableProperty().bind(Bindings.isNotEmpty(taskProgressView.tasks))

                alert.setContent(layout)
                alert.show()

                command = SystemCommand.UpdateSystemCommand
                command.execute()
            }else{
                showTipInfo("更新子系统提示", "TEST  所有子系统都是最新版,没有系统要更新!!!")
            }
        }
    }


    private fun showTipInfo(head: String, body: String){
        val alert = JFXAlert<Any>(opiBtn?.scene?.window)
        alert.initModality(Modality.APPLICATION_MODAL)
        alert.isOverlayClose = false
        val layout = JFXDialogLayout()
        layout.setHeading(Label(head))
        layout.setBody(Label(body))
        val closeButton = JFXButton("OK")
        closeButton.styleClass.add("dialog-accept")
        closeButton.setOnAction { alert.hideWithAnimation() }
        layout.setActions(closeButton)
        alert.setContent(layout)
        alert.show()
    }

    private fun setNotesBtnEvent(){
        notesBtn.setOnAction {
            // 切换场景
//            val stage = notesBtn.scene.window as Stage
//            stage.hide()
//            stage.close()
            RouteUtil.load("ui/FABNoteView.fxml", "发布公告", false)
        }
    }



}