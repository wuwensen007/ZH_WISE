package com.example.wise

import com.example.wise.announcement.Note
import com.example.wise.updatesystem.DesktopSubSystem
import com.example.wise.updatesystem.FAB
import com.example.wise.updatesystem.SubSystem
import com.example.wise.updatesystem.SystemInitHelper
import com.jfoenix.controls.JFXDecorator
import com.jfoenix.controls.JFXProgressBar
import com.sun.javafx.application.LauncherImpl
import javafx.application.Application
import javafx.application.Platform
import javafx.application.Preloader
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.concurrent.Task
import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.HBox
import javafx.stage.Stage
import java.util.*


class WiseApplication: Application() {

	companion object{

		var curSubSystems: SimpleObjectProperty<ObservableMap<String, SimpleObjectProperty<SubSystem>>> = SimpleObjectProperty()

		var curDesktopSubSystems: SimpleObjectProperty<ObservableMap<String, SimpleObjectProperty<DesktopSubSystem>>> = SimpleObjectProperty()

		var curFAB: SimpleObjectProperty<FAB> = SimpleObjectProperty()

		var curNotes: SimpleObjectProperty<ObservableList<Note>> = SimpleObjectProperty(FXCollections.observableArrayList())

		// 登录
		fun login(){

		}

		// 登出
		fun logout(){

		}

		// 是否有子系统要更新
		fun haveSubSystemUpdate(): Boolean{

			curSubSystems.value.filter { it.value.value is DesktopSubSystem  }.map { it.value.value as DesktopSubSystem }.forEach {
				if (it.haveNewVersion()){
					return true
				}
			}
			return false
		}


		// 是否有子系统强制要更新
		fun haveSubSystemForcedUpdate(): Boolean{

			curSubSystems.value.filter { it.value.value is DesktopSubSystem  }.map { it.value.value as DesktopSubSystem }.forEach {
				val didForcedUpdate = it.systemProp.getProperty("didForcedUpdate") as  Boolean
				if (it.haveNewVersion() && didForcedUpdate){
					return true
				}
			}
			return false
		}

		fun runSubSystem(systemName: String) {
			curSubSystems.value[systemName]?.value?.runSystem()
		}

		fun haveSubSystem(systemName: String): Boolean {
			return curSubSystems.value.containsKey(systemName)
		}

		private fun getDesktopSubSystems(): List<DesktopSubSystem>{
			return curSubSystems.value.filter { it.value.value is DesktopSubSystem }.map { it.value.value as DesktopSubSystem }.toList()
		}

		fun getSubSystemVersion(systemName: String): SimpleStringProperty?{
			return curSubSystems.value[systemName]?.value?.systemVersion
		}

		fun startUpdateSubSystems(){
			getDesktopSubSystems().filter { it.haveNewVersion() }.forEach {
				it.updateSystem()
			}
		}

		fun startForcedToUpdateSubSystems(){
			getDesktopSubSystems().filter { it.haveNewVersion() && (it.systemProp.getProperty("didForcedUpdate") as Boolean) }.forEach {
				it.updateSystem()
			}
		}

		fun getUpdateTasks(): List<Task<Void>> {
			val res = mutableListOf<Task<Void>>()
			getDesktopSubSystems().filter { it.haveNewVersion() }.forEach {
				res.add(it.curUpdateHelper)
			}
			return res
		}

		fun openSubSystemDocumentation(systemName: String){
			curDesktopSubSystems.value[systemName]?.value?.openDocumentation()
		}

	}


	override fun init() {
		val systemInitHelper = SystemInitHelper()
		if (systemInitHelper.whetherToInitialize()){
			systemInitHelper.progressProperty().addListener { _, _, newValue ->
				// 更新进度
				LauncherImpl.notifyPreloader(this, Preloader.ProgressNotification(newValue.toDouble()))
			}
			systemInitHelper.run()
		}else{
			// 更新进度
			LauncherImpl.notifyPreloader(this, Preloader.ProgressNotification(1.0))
			Thread.sleep(50)
		}
	}


	override fun stop() {
		Platform.exit()
	}


	override fun start(primaryStage: Stage?) {

		val loader = FXMLLoader()

		val url = loader.classLoader.getResource("ui/LoginView.fxml")
		loader.location = url

		// 设置国际化
		val resourceBundle = ResourceBundle.getBundle("lang")
		loader.resources = resourceBundle

		var root = loader.load<Parent>()

		val decorator = JFXDecorator(primaryStage, root)
		decorator.isCustomMaximize = false
		val scene = Scene(decorator)

		primaryStage?.apply {
			title = "登录"
			this.scene = scene
			isResizable = false
			sizeToScene()
			show()
		}

	}
}

class WisePreloader: Preloader(){

	var bar: JFXProgressBar? = null
	var stage: Stage? = null

	var scene: Scene? = null


	override fun init() {

		bar = JFXProgressBar(0.0)
		val hbox = HBox()
		hbox.alignment = Pos.CENTER
		val label = Label("初始化资源中...")
		val thePercentage = Label()
		thePercentage.textProperty().bind(Bindings.concat(Bindings.format("%2.0f",bar?.progressProperty()?.multiply(100))).concat("%"))
		hbox.children.addAll(label, bar, thePercentage)
		scene = Scene(hbox, 500.0, 150.0)
	}



	@Throws(Exception::class)
	override fun start(stage: Stage) {
		this.stage = stage
		stage.title = "欢迎页面"
		stage.scene = scene
		stage.show()
	}


	override fun handleStateChangeNotification(evt: StateChangeNotification?) {
		when (evt?.type) {
			StateChangeNotification.Type.BEFORE_INIT -> {
				println("BEFORE_INIT")
			}
			StateChangeNotification.Type.BEFORE_LOAD -> {
				println("BEFORE_LOAD")
			}
			StateChangeNotification.Type.BEFORE_START -> {
				println("BEFORE_START")

				stage?.close()

			}
		}
	}

	override fun handleApplicationNotification(pn: PreloaderNotification?) {
		if (pn is ProgressNotification) {
			bar?.progress = pn.progress
		}
	}

}

fun main(args: Array<String>) {
	//Platform.setImplicitExit(false);
	LauncherImpl.launchApplication(WiseApplication::class.java, WisePreloader::class.java, args)

//	Application.launch(WiseApplication::class.java, *args)
}
