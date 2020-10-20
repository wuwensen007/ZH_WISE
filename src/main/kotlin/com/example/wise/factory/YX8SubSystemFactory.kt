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


class YX8SubSystemFactory: SubSystemFactory() {

    companion object{
        private val logger = LoggerFactory.getLogger(YX8SubSystemFactory::class.java)
        private val list = mutableListOf<SubSystem>()
        var isInit = false

        fun initYX8Systems(){

            logger.info("开始初始化宜兴8寸所有子系统")
            isInit = true
//            val exec = Executors.newSingleThreadExecutor()
//            exec.execute {
//
//            }
//            exec.shutdown()
            val mesProductList: List<MESProduct> = CsvToBeanBuilder<MESProduct>(FileReader(YX8SubSystemVer.getSubSystemLocalCsvPath())).withType(MESProduct::class.java).build().parse()

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
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX8SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "PMS" -> {
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.path, factoryVer = YX8SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "REPORT" -> {
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX8SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)

                    }
                    "FINEREPORT" -> {
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX8SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "ZHLXREPORT" -> {
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX8SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "VIDAS"->{
                        WebApp(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, factoryVer = YX8SubSystemVer,  runCommand = """cmd /c start ${it.path}""", systemProp = webAppProp)
                    }
                    "ONCALL" -> {
                        systemProp.setProperty("sopFileName", "${YX8SubSystemVer.getLocalOnCallSOPFilePath()}${File.separator}${it.sopFileName}")
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """java -jar ${YX8SubSystemVer.getLocalOnCallBasePath()}${File.separator}${it.fileName}""", factoryVer = YX8SubSystemVer, systemProp = systemProp)
                    }
                    "OPI" -> {
                        systemProp.setProperty("sopFileName", "${YX8SubSystemVer.getLocalOPISOPFilePath()}${File.separator}${it.sopFileName}")
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """java -jar ${YX8SubSystemVer.getLocalOPIBasePath()}${File.separator}${it.fileName}""", factoryVer = YX8SubSystemVer, systemProp = systemProp)
                    }
                    "SM" -> {
                        systemProp.setProperty("sopFileName", "${YX8SubSystemVer.getLocalSMSOPFilePath()}${File.separator}${it.sopFileName}")
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """cmd /c cd ${YX8SubSystemVer.getLocalSMBasePath()}&&${it.fileName}""", factoryVer = YX8SubSystemVer, systemProp = systemProp)
                    }
                    "SPC" -> {
                        systemProp.setProperty("sopFileName", "${YX8SubSystemVer.getLocalSPCSOPFilePath()}${File.separator}${it.sopFileName}")
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """cmd /c cd ${YX8SubSystemVer.getLocalSPCBasePath()}&&${it.fileName}""", factoryVer = YX8SubSystemVer, systemProp = systemProp)
                    }
                    else -> throw RuntimeException("不支持的子系统")
                })
            }
        }
    }



    override fun getSubSystems(): ObservableMap<String,SimpleObjectProperty<SubSystem>> {
2
        if (!isInit){
            initYX8Systems()
        }
        logger.info("读取本地宜兴8寸CSV初始化子系统")
        val map = list.map { it.systemName.toUpperCase() to SimpleObjectProperty(it) }.toMap()

        return FXCollections.observableMap(map)
    }

    override fun getDesktopSystems(): ObservableMap<String, SimpleObjectProperty<DesktopSubSystem>> {
        if (!isInit){
            initYX8Systems()
        }
        logger.info("读取本地宜兴8寸CSV初始化子系统")
        val map = list.filterIsInstance<DesktopSubSystem>().map { it.systemName.toUpperCase() to SimpleObjectProperty(it) }.toMap()

        return FXCollections.observableMap(map)
    }
}