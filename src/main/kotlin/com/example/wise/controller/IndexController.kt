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
import java.util.concurrent.Executors

class IndexController: Initializable {

    @FXML lateinit var opiBtn: JFXButton

    @FXML lateinit var smBtn: JFXButton

    @FXML lateinit var spcBtn: JFXButton

    @FXML lateinit var oncallBtn: JFXButton

    @FXML lateinit var reportBtn: JFXButton

    @FXML lateinit var cfmBtn: JFXButton

    @FXML lateinit var pmsBtn: JFXButton

    @FXML lateinit var fineReportBtn: JFXButton

    @FXML lateinit var zhlxReportBtn: JFXButton

    @FXML lateinit var vidasBtn: JFXButton

    @FXML lateinit var checkUpdateBtn: JFXButton

    @FXML lateinit var fabComboBox: JFXComboBox<FAB>

    @FXML lateinit var opiVerLink: Hyperlink

    @FXML lateinit var smVerLink: Hyperlink

    @FXML lateinit var spcVerLink: Hyperlink

    @FXML lateinit var oncallVerLink: Hyperlink

    @FXML lateinit var reportVerLink: Hyperlink

    @FXML lateinit var cfmVerLink: Hyperlink

    @FXML lateinit var pmsVerLink: Hyperlink

    @FXML lateinit var fineReportVerLink: Hyperlink

    @FXML lateinit var zhlxReportVerLink: Hyperlink

    @FXML lateinit var vidasVerLink: Hyperlink

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
        Platform.runLater {
            val exec = Executors.newSingleThreadExecutor()
            exec.execute{
                WiseApplication.curSubSystems.bind(Bindings.`when`(fabComboBox.selectionModel.selectedItemProperty().isEqualTo(FAB.YX8)).then(SimpleObjectProperty(YX8SubSystemFactory().getSubSystems()))
                        .otherwise(Bindings.`when`(fabComboBox.selectionModel.selectedItemProperty().isEqualTo(FAB.YX12)).then(SimpleObjectProperty(YX12SubSystemFactory().getSubSystems()))
                                .otherwise(SimpleObjectProperty(TJ12SubSystemFactory().getSubSystems()))))

                WiseApplication.curDesktopSubSystems.bind(Bindings.`when`(fabComboBox.selectionModel.selectedItemProperty().isEqualTo(FAB.YX8)).then(SimpleObjectProperty(YX8SubSystemFactory().getDesktopSystems()))
                        .otherwise(Bindings.`when`(fabComboBox.selectionModel.selectedItemProperty().isEqualTo(FAB.YX12)).then(SimpleObjectProperty(YX12SubSystemFactory().getDesktopSystems()))
                                .otherwise(SimpleObjectProperty(TJ12SubSystemFactory().getDesktopSystems()))))
            }
            exec.shutdown()
        }


        WiseApplication.curDesktopSubSystems.addListener { _, _, newValue ->

            versionUnBind()
            versionBind()
        }

        // 版本解绑
        versionUnBind()
        // 版本绑定
        versionBind()

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
                val alert = JFXAlert<Any>(opiBtn.scene?.window)
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

    private fun versionUnBind(){
        // 解除绑定
        opiVerLink.textProperty().unbind()
        smVerLink.textProperty().unbind()
        spcVerLink.textProperty().unbind()
        oncallVerLink.textProperty().unbind()
        cfmVerLink.textProperty().unbind()
        reportVerLink.textProperty().unbind()
        pmsVerLink.textProperty().unbind()
        fineReportVerLink.textProperty().unbind()
        zhlxReportVerLink.textProperty().unbind()
        vidasVerLink.textProperty().unbind()
    }

    private fun versionBind(){
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
        if (WiseApplication.haveSubSystem("FINEREPORT")){
            fineReportVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("FINEREPORT"))
        }
        if (WiseApplication.haveSubSystem("ZHLXREPORT")){
            zhlxReportVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("ZHLXREPORT"))
        }
        if (WiseApplication.haveSubSystem("VIDAS")){
            vidasVerLink.textProperty().bind(WiseApplication.getSubSystemVersion("VIDAS"))
        }
    }



    private fun setBtnPropBinding(){

        opiVerLink.visibleProperty().bind(opiBtn.disableProperty().not())
        smVerLink.visibleProperty().bind(smBtn.disableProperty().not())
        spcVerLink.visibleProperty().bind(spcBtn.disableProperty().not())
        oncallVerLink.visibleProperty().bind(oncallBtn.disableProperty().not())
        cfmVerLink.visibleProperty().bind(cfmBtn.disableProperty().not())
        reportVerLink.visibleProperty().bind(reportBtn.disableProperty().not())
        pmsVerLink.visibleProperty().bind(pmsBtn.disableProperty().not())
        fineReportVerLink.visibleProperty().bind(fineReportBtn.disableProperty().not())
        zhlxReportVerLink.visibleProperty().bind(zhlxReportBtn.disableProperty().not())
        vidasVerLink.visibleProperty().bind(vidasBtn.disableProperty().not())

        opiSopLink.visibleProperty().bind(opiBtn.disableProperty().not())
        smSopLink.visibleProperty().bind(smBtn.disableProperty().not())
        spcSopLink.visibleProperty().bind(spcBtn.disableProperty().not())
        oncallSopLink.visibleProperty().bind(oncallBtn.disableProperty().not())
    }

    private fun initNotes(fab: FAB){
        command = SystemCommand.DisplayNoteCommand(5, fab = fab)
        command.execute()
    }

    private fun setBtnEvent(){

        opiBtn.setOnAction {
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

        fineReportBtn.setOnAction {
            command = SystemCommand.FineReportStartCommand
            command.execute()
        }

        zhlxReportBtn.setOnAction {
            command = SystemCommand.ZHLXReportStartCommand
            command.execute()
        }

        vidasBtn.setOnAction {
            command = SystemCommand.VIDASStartCommand
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

        smBtn.isDisable = !WiseApplication.haveSubSystem("SM")
        opiBtn.isDisable = !WiseApplication.haveSubSystem("OPI")
        spcBtn.isDisable = !WiseApplication.haveSubSystem("SPC")
        oncallBtn.isDisable = !WiseApplication.haveSubSystem("ONCALL")
        cfmBtn.isDisable = !WiseApplication.haveSubSystem("CFM")
        reportBtn.isDisable = !WiseApplication.haveSubSystem("REPORT")
        pmsBtn.isDisable = !WiseApplication.haveSubSystem("PMS")
        vidasBtn.isDisable = !WiseApplication.haveSubSystem("VIDAS")
        fineReportBtn.isDisable = !WiseApplication.haveSubSystem("FINEREPORT")
        zhlxReportBtn.isDisable = !WiseApplication.haveSubSystem("ZHLXREPORT")
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
            val stage = notesBtn.scene.window as Stage
            RouteUtil.load(stage,"ui/FABNoteView.fxml", "发布公告", false)
        }
    }



}