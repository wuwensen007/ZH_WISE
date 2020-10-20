package com.example.wise.util

import com.jfoenix.controls.JFXDecorator
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.util.*

object RouteUtil {

    fun load(stage: Stage,fxmlPath: String?, title: String?, resizable: Boolean) {


        val loader = FXMLLoader()

        val url = loader.classLoader.getResource(fxmlPath)
        loader.location = url

        // 设置国际化
        val resourceBundle = ResourceBundle.getBundle("lang")
        loader.resources = resourceBundle

        var root = loader.load<Parent>()

//        val decorator = JFXDecorator(stage, root)
//        decorator.isCustomMaximize = false
        val scene = Scene(root)
        scene.stylesheets.addAll(this::class.java.getResource("/application.css").toExternalForm());
        stage.title = title
        stage.scene = scene
        stage.sizeToScene()
        stage.isResizable = resizable
        stage.show()
    }

}