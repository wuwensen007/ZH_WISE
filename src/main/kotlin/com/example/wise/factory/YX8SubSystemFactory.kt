package com.example.wise.factory

import com.example.wise.config.Constant
import com.example.wise.config.Constant.LOCAL_WISE_BASE_PATH
import com.example.wise.updatesystem.*
import com.opencsv.bean.CsvToBeanBuilder
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class YX8SubSystemFactory: SubSystemFactory() {

    companion object{
        private val logger = LoggerFactory.getLogger(YX8SubSystemFactory::class.java)
        private val list = mutableListOf<SubSystem>()

        init {
            val mesProductList: List<MESProduct> = CsvToBeanBuilder<MESProduct>(FileReader("""${LOCAL_WISE_BASE_PATH}${File.separator}${Constant.YX8CSVNAME}""")).withType(MESProduct::class.java).build().parse()

            mesProductList.forEach {
                var didForcedToUpdate = false
                if (it.forceUpdate == "Y"){
                    didForcedToUpdate = true
                }
                val systemProp = SystemProp()
                systemProp.setProperty("didForcedUpdate", didForcedToUpdate)

                // 添加客户端应用
                list.add(when(it.systemName.toUpperCase()){
                    "ONCALL" -> {
                        systemProp.setProperty("sopFileName", YX8SubSystemVer.getLocalOnCallSOPFilePath())
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """java -jar ${YX8SubSystemVer.getLocalOnCallBasePath()}${File.separator}${it.fileName}""", factoryVer = YX8SubSystemVer, systemProp = systemProp)
                    }
                    "OPI" -> {
                        systemProp.setProperty("sopFileName", YX8SubSystemVer.getLocalOPISOPFilePath())
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """java -jar ${YX8SubSystemVer.getLocalOPIBasePath()}${File.separator}${it.fileName}""", factoryVer = YX8SubSystemVer, systemProp = systemProp)
                    }
                    "SM" -> {
                        systemProp.setProperty("sopFileName", YX8SubSystemVer.getLocalSMSOPFilePath())
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """cmd /c cd ${YX8SubSystemVer.getLocalSubSystemBasePath()}${File.separator}${Constant.SM}&&${it.fileName}""", factoryVer = YX8SubSystemVer, systemProp = systemProp)
                    }
                    "SPC" -> {
                        systemProp.setProperty("sopFileName", YX8SubSystemVer.getLocalSPCSOPFilePath())
                        DesktopSubSystem(systemName = it.systemName, systemVersion = SimpleStringProperty(it.version), updateTime = it.updateTime, runCommand = """cmd /c cd ${YX8SubSystemVer.getLocalSubSystemBasePath()}${File.separator}${Constant.SPC}&&${it.fileName}""", factoryVer = YX8SubSystemVer, systemProp = systemProp)
                    }
                    else -> throw RuntimeException("不支持的子系统")
                })
            }


            // 添加web应用
            val cfmSysProp = SystemProp()
            cfmSysProp.setProperty("url", YX8SubSystemVer.getCFMIndexUrl())
            val reportSysProp = SystemProp()
            reportSysProp.setProperty("url", YX8SubSystemVer.getReportIndexUrl())
            val pmsSysProp = SystemProp()
            pmsSysProp.setProperty("url", YX8SubSystemVer.getPMSIndexUrl())
            val cfm = WebApp(systemName = "CFM", systemVersion = SimpleStringProperty("1.0.0"), updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/m/d h:mm")), factoryVer = YX8SubSystemVer,  runCommand = """cmd /c start ${YX8SubSystemVer.getCFMIndexUrl()}""", systemProp = cfmSysProp) as SubSystem
            val report = WebApp(systemName = "REPORT", systemVersion = SimpleStringProperty("1.0.0"), updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/m/d h:mm")), factoryVer = YX8SubSystemVer,  runCommand = """cmd /c start ${YX8SubSystemVer.getReportIndexUrl()}""", systemProp = reportSysProp) as SubSystem
            val pms = WebApp(systemName = "PMS", systemVersion = SimpleStringProperty("1.0.0"), updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/m/d h:mm")), factoryVer = YX8SubSystemVer,  runCommand = """cmd /c start ${YX8SubSystemVer.getPMSIndexUrl()}""", systemProp = pmsSysProp) as SubSystem

            list.addAll(listOf(cfm, report, pms))

        }


    }

    override fun getSubSystems(): ObservableMap<String,SimpleObjectProperty<SubSystem>> {

        logger.info("读取本地宜兴8寸CSV初始化子系统")
        val map = list.map { it.systemName to SimpleObjectProperty(it) }.toMap()

        return FXCollections.observableMap(map)
    }

    override fun getDesktopSystems(): ObservableMap<String, SimpleObjectProperty<DesktopSubSystem>> {
        logger.info("读取本地宜兴8寸CSV初始化子系统")
        val map = list.filterIsInstance<DesktopSubSystem>().map { it.systemName to SimpleObjectProperty(it) }.toMap()

        return FXCollections.observableMap(map)
    }
}