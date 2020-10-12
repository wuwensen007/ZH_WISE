package com.example.wise.controller

import com.example.wise.WiseApplication
import com.example.wise.factory.TJ12SubSystemFactory
import com.example.wise.factory.YX12SubSystemFactory
import com.example.wise.factory.YX8SubSystemFactory
import com.example.wise.updatesystem.FAB
import com.example.wise.updatesystem.TJ12SubSystemVer
import com.example.wise.updatesystem.YX12SubSystemVer
import com.example.wise.updatesystem.YX8SubSystemVer
import com.example.wise.util.FtpUtil
import com.example.wise.util.RouteUtil
import com.jfoenix.controls.*
import com.jfoenix.validation.RequiredFieldValidator
import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.AnchorPane
import javafx.stage.Modality
import javafx.stage.Stage

import java.net.URL
import java.util.*



class LoginController: Initializable {

    @FXML lateinit var fabToggleGP: ToggleGroup

    @FXML lateinit var useridTf: JFXTextField

    @FXML lateinit var pwdTf: JFXPasswordField

    @FXML lateinit var langComboBox: JFXComboBox<Locale>

    @FXML lateinit var okBtn: JFXButton

    @FXML lateinit var cancelBtn: JFXButton

    @FXML lateinit var changePwdBtn: JFXButton

    @FXML lateinit var loginPane: AnchorPane

    @FXML lateinit var yx8RadioBtn: JFXRadioButton

    @FXML lateinit var yx12RadioBtn: JFXRadioButton

    @FXML lateinit var tj12RadioBtn: JFXRadioButton


    override fun initialize(location: URL?, resources: ResourceBundle?) {

        val validator = RequiredFieldValidator()
        validator.message = "Input Required"
        validator.icon = GlyphsDude.createIcon(MaterialDesignIcon.ALERT_CIRCLE)
        useridTf.validators.add(validator)
        useridTf.focusedProperty().addListener { _, _, newVal -> if (!newVal) useridTf.validate() }


        pwdTf.validators.add(validator)
        pwdTf.focusedProperty().addListener { _, _, newVal -> if (!newVal) pwdTf.validate() }

        langComboBox.items.addAll(Locale.CHINESE, Locale.ENGLISH, Locale.TAIWAN)

        langComboBox.selectionModel.selectedItemProperty().addListener { _, _, newV->
            val resourceBundle = ResourceBundle.getBundle("lang", newV)
            useridTf.promptText = resourceBundle.getString("userPromptText")
            println(resources?.baseBundleName)
        }

        okBtn.setOnAction {

            if (useridTf.validate() && pwdTf.validate() && langComboBox.validate()){

                // 切换场景
                val stage = useridTf.scene.window as Stage
                stage.hide()
                stage.close()
                RouteUtil.load("ui/IndexView.fxml", "主页", false)
            }else{
                val alert = JFXAlert<Any>(useridTf.scene.window)
                alert.initModality(Modality.APPLICATION_MODAL)
                alert.isOverlayClose = false
                val layout = JFXDialogLayout()
                layout.setHeading(Label("Modal Dialog using JFXAlert"))
                layout.setBody(Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit,"
                        + " sed do eiusmod tempor incididunt ut labore et dolore magna"
                        + " aliqua. Utenim ad minim veniam, quis nostrud exercitation"
                        + " ullamco laboris nisi ut aliquip ex ea commodo consequat."))
                val closeButton = JFXButton("ACCEPT")
                closeButton.styleClass.add("dialog-accept")
                closeButton.setOnAction { alert.hideWithAnimation() }
                layout.setActions(closeButton)
                alert.setContent(layout)
                alert.show()
            }
        }


        // 数据绑定
        WiseApplication.curSubSystems.bind(Bindings.`when`(fabToggleGP.selectedToggleProperty().isEqualTo(yx8RadioBtn)).then(SimpleObjectProperty(YX8SubSystemFactory().getSubSystems()))
                .otherwise(Bindings.`when`(fabToggleGP.selectedToggleProperty().isEqualTo(yx12RadioBtn)).then(SimpleObjectProperty(YX12SubSystemFactory().getSubSystems()))
                        .otherwise(SimpleObjectProperty(TJ12SubSystemFactory().getSubSystems()))))

        WiseApplication.curDesktopSubSystems.bind(Bindings.`when`(fabToggleGP.selectedToggleProperty().isEqualTo(yx8RadioBtn)).then(SimpleObjectProperty(YX8SubSystemFactory().getDesktopSystems()))
                .otherwise(Bindings.`when`(fabToggleGP.selectedToggleProperty().isEqualTo(yx12RadioBtn)).then(SimpleObjectProperty(YX12SubSystemFactory().getDesktopSystems()))
                        .otherwise(SimpleObjectProperty(TJ12SubSystemFactory().getDesktopSystems()))))

        WiseApplication.curFAB.bind(Bindings.`when`(fabToggleGP.selectedToggleProperty().isEqualTo(yx8RadioBtn)).then(FAB.YX8)
                .otherwise(Bindings.`when`(fabToggleGP.selectedToggleProperty().isEqualTo(yx12RadioBtn)).then(FAB.YX12).otherwise(FAB.TJ12)))


        // 重置信息
        cancelBtn.setOnAction {
            resetLoginInfo()
        }

        changePwdBtn.setOnAction {

        }

    }


    private fun resetLoginInfo(){
        useridTf.clear()
        useridTf.resetValidation()
        pwdTf.clear()
        pwdTf.resetValidation()
        langComboBox.selectionModel.clearSelection()
        langComboBox.resetValidation()
    }


}