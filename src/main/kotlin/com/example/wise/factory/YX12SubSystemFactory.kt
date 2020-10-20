package com.example.wise.factory

import com.example.wise.updatesystem.*
import com.opencsv.bean.CsvToBeanBuilder
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.util.concurrent.Executors

class YX12SubSystemFactory: SubSystemFactory() {

    companion object{
        private val logger = LoggerFactory.getLogger(YX12SubSystemFactory::class.java)
        private val list = mutableListOf<SubSystem>()
        var isInit = false


        fun initYX12Systems(){
            logger.info("开始初始化宜兴12寸所有子系统")
            isInit = true
//            val exec = Executors.newSingleThreadExecutor()
//            exec.execute {
//
//            }
//            exec.shutdown()
            val mesProductList: List<MESProduct> = CsvToBeanBuilder<MESProduct>(FileReader(YX12SubSystemVer.getSubSystemLocalCsvPath())).withType(MESProduct::class.java).build().parse()

            mesProductList.forEach {
                var didForcedToUpdate = false
                if (it.forceUpdate == "Y"){
                    didForcedToUpdate = true
                }
                val systemProp = SystemProp()
                systemProp.setProperty("didForcedUpdate", didForcedToUpdate)

                val webAppProp = SystemProp()
                webAppProp.setProperty("url", it.path)

                // 添加客户端应用
                list.add(when(it.systemName.toUpperCase()){
                    "CFM" -> {
                        // 添加web应用
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX12SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "PMS" -> {
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.path, factoryVer = YX12SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "REPORT" -> {
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX12SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "FINEREPORT" -> {
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX12SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "ZHLXREPORT" -> {
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX12SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "VIDAS"->{
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX12SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "ONCALL" -> {
                        systemProp.setProperty("sopFileName", "${YX12SubSystemVer.getLocalOnCallSOPFilePath()}${File.separator}${it.sopFileName}")
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """java -jar ${YX12SubSystemVer.getLocalOnCallBasePath()}${File.separator}${it.fileName}""", factoryVer = YX12SubSystemVer, systemProp = systemProp)
                    }
                    "OPI" -> {
                        systemProp.setProperty("sopFileName", "${YX12SubSystemVer.getLocalOPISOPFilePath()}${File.separator}${it.sopFileName}")
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """java -jar ${YX12SubSystemVer.getLocalOPIBasePath()}${File.separator}${it.fileName}""", factoryVer = YX12SubSystemVer, systemProp = systemProp)
                    }
                    "SM" -> {
                        systemProp.setProperty("sopFileName", "${YX12SubSystemVer.getLocalSMSOPFilePath()}${File.separator}${it.sopFileName}")
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """cmd /c cd ${YX12SubSystemVer.getLocalSMBasePath()}&&${it.fileName}""", factoryVer = YX12SubSystemVer, systemProp = systemProp)
                    }
                    "SPC" -> {
                        systemProp.setProperty("sopFileName", "${YX12SubSystemVer.getLocalSPCSOPFilePath()}${File.separator}${it.sopFileName}")
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """cmd /c cd ${YX12SubSystemVer.getLocalSPCBasePath()}&&${it.fileName}""", factoryVer = YX12SubSystemVer, systemProp = systemProp)
                    }
                    else -> throw RuntimeException("不支持的子系统")
                })
            }
        }

    }

    override fun getSubSystems(): ObservableMap<String,SimpleObjectProperty<SubSystem>> {

        if (!isInit){
            initYX12Systems()
        }
        // 读取本地CSV初始化子系统

        logger.info("读取本地宜兴12寸CSV初始化子系统")
        val map = list.map { it.systemName.toUpperCase() to SimpleObjectProperty(it) }.toMap()

        return FXCollections.observableMap(map)
    }

    override fun getDesktopSystems(): ObservableMap<String, SimpleObjectProperty<DesktopSubSystem>> {

        if (!isInit){
            initYX12Systems()
        }
        // 读取本地CSV初始化桌面子系统

        logger.info("读取本地宜兴12寸CSV初始化桌面子系统")
        val map = list.filterIsInstance<DesktopSubSystem>().map { it.systemName.toUpperCase() to SimpleObjectProperty(it) }.toMap()

        return FXCollections.observableMap(map)
    }
}