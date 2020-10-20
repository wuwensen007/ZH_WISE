package com.example.wise.updatesystem

import com.example.wise.config.Constant
import com.example.wise.util.FtpUtil
import com.opencsv.bean.CsvToBeanBuilder
import com.opencsv.bean.StatefulBeanToCsvBuilder
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors



class DesktopSubSystem(systemName: String, systemVersion: SimpleStringProperty, updateTime: String, runCommand: String, factoryVer: SubSystemVer, systemProp: SystemProp): SubSystem(systemName, systemVersion, updateTime, runCommand, factoryVer, systemProp) {

//    var didForcedUpdate: Boolean, var fab: FAB, var subSystemVer: SubSystemVer = YX8SubSystemVer,var sopFileName: String,
    private val logger = LoggerFactory.getLogger(DesktopSubSystem::class.java)

    var curUpdateHelper: SystemUpdateHelper = SystemUpdateHelper(this)


    fun getFtpSystemPath(): String{
        return """${factoryVer.getSubSystemFtpBasePath()}${File.separator}SetUp${File.separator}${systemName}"""
    }

    fun getLocalSystemPath(): String{
        return """${factoryVer.getLocalSubSystemBasePath()}${File.separator}SetUp${File.separator}${systemName}"""
    }

    fun getLocalSystemBackupPath(): String{
        return "${factoryVer.getLocalSubSystemBackupBasePath()}${File.separator}${systemName}"
    }

    // 打开文档
    fun openDocumentation(){
        Runtime.getRuntime().exec("cmd /c ${systemProp.getProperty("sopFileName")}")
    }

    // 更新版本
    fun updateSystem(){
        // 检查是否有新版
        if (haveNewVersion()){

            val exec = Executors.newSingleThreadExecutor()
            exec.execute(curUpdateHelper)
            exec.shutdown()
            curUpdateHelper = SystemUpdateHelper(this)

        }else{
            logger.info("没有版本可以更新或者程序正在启动中")
        }
    }

    fun haveNewVersion(): Boolean {
        // 从ftp下载指定csv文件
        FtpUtil.downloadCSVFiles(csvPath = factoryVer.getFtpCsvPath(), savePath = factoryVer.getSubSystemLocalTempCsvPath())
        // 获取远程csv中系统的信息
        val mesProductList: List<MESProduct> = CsvToBeanBuilder<MESProduct>(FileReader(factoryVer.getSubSystemLocalTempCsvPath())).withType(MESProduct::class.java).build().parse()
        val remoteSubSystemInfo = mesProductList.first { it.systemName == systemName }

        // 读取本地csv中系统信息
        val localMESProductList = CsvToBeanBuilder<MESProduct>(FileReader(factoryVer.getSubSystemLocalCsvPath())).withType(MESProduct::class.java).build().parse()
        val localSubSystemInfo = localMESProductList.first { it.systemName == systemName }
        // 比较两个系统是否一样
        return if (!remoteSubSystemInfo.isEquals(localSubSystemInfo)){
//            val systemUpdateHelper = SystemUpdateHelper(this)
//            updateTasks.add(systemUpdateHelper)
//            println("添加一个更新任务--------------------------------》任务${updateTasks.size}")
            logger.info("有新版应用，应用名:$systemName")
            true
        }else{
            false
        }
    }

    fun updateSystemState(){
        logger.info("---------------------------开始更新系统状态...")
        // 获取远程csv中opi系统的信息, 初始化系统信息
        val mesProductList: List<MESProduct> = CsvToBeanBuilder<MESProduct>(FileReader(factoryVer.getSubSystemLocalTempCsvPath())).withType(MESProduct::class.java).build().parse()
        val newSubSystem = mesProductList.first { it.systemName == systemName }
        Platform.runLater {
            this.systemVersion.value = newSubSystem.version
        }
        this.updateTime = newSubSystem.updateTime
        logger.info("--------------------------系统状态更新完成")
    }

    fun updateLocalCSV(){
        logger.info("----------------------------开始更新本地CSV文件...")
        // 读本地CSV更新数据
        val localMESProductList = CsvToBeanBuilder<MESProduct>(FileReader(factoryVer.getSubSystemLocalTempCsvPath())).withType(MESProduct::class.java).build().parse()
        val newLocalMESProdList = mutableListOf<MESProduct>()
        localMESProductList.forEach {
            if (it.systemName == systemName){
                newLocalMESProdList.add(MESProduct(it.systemName, it.type, it.fileName, it.path, this.systemVersion.valueSafe, this.updateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), it.forceUpdate, it.ftpPath))
            }else{
                newLocalMESProdList.add(it)
            }
        }
        // 提交更新数据
        val writer: Writer = FileWriter(factoryVer.getSubSystemLocalCsvPath())
        val beanToCsv = StatefulBeanToCsvBuilder<MESProduct>(writer).build()
        beanToCsv.write(newLocalMESProdList)
        writer.close()
        logger.info("---------------------------更新本地CSV文件完成")
    }


}