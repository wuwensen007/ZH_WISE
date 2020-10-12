package com.example.wise.updatesystem

import com.example.wise.config.Constant
import com.example.wise.util.FtpUtil
import javafx.concurrent.Task
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.io.CopyStreamAdapter
import org.apache.commons.net.io.CopyStreamListener
import org.slf4j.LoggerFactory
import java.io.*
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class SystemUpdateHelper(var system: DesktopSubSystem): Task<Void>() {

    private val logger = LoggerFactory.getLogger(SystemUpdateHelper::class.java)

    private val ftpClient: FTPClient

    var updateDesktopSubSystemSize: Long = FtpUtil.getFilesSize(system.getFtpSystemPath())
    var appTotalBytesTransferred = 0L
    var updateRateOfProcess = 0.0

    init {
        updateTitle(system.systemName)
        ftpClient = FTPClient()
        ftpClient.copyStreamListener = createListener()
    }


    // 备份还原系统
    private fun backupSubSystem(){
        logger.info("----------------------------系统备份中")
        downFiles(system.getLocalSystemPath(), system.getLocalSystemBackupPath())
        logger.info("----------------------------系统备份成功")
    }

    private fun downFiles(from: String, toDir: String){

        val file = File(from)
        if (!file.exists()){
            return
        }
        val to = File(toDir)
        if (!to.exists()){
            to.mkdirs()
        }
        file.listFiles().forEach {
            if (it.isFile){
                // 创建文件
                val newFile = File("$toDir${File.separator}${it.name}")
                Files.copy(it.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }else if(it.isDirectory){
                val newDir = File("$toDir${File.separator}${it.name}")
                newDir.mkdirs()
                downFiles(it.canonicalPath, newDir.canonicalPath)
            }
        }
    }

    // 还原系统，回退旧版本
    private fun restoreSubSystem(){
        logger.info("----------------------------系统还原中")
        downFiles(system.getLocalSystemBackupPath(), system.getLocalSystemPath())
        logger.info("----------------------------系统还原成功")
    }

    private fun resetRateOfProgress(){
        updateRateOfProcess = 0.0
        appTotalBytesTransferred = 0
        logger.info("----------------------------重置进度......")
    }

    private fun createListener(): CopyStreamListener {

        return object : CopyStreamAdapter() {
            override fun bytesTransferred(totalBytesTransferred: Long,
                                          bytesTransferred: Int, streamSize: Long) {

                appTotalBytesTransferred += bytesTransferred
                updateRateOfProcess = appTotalBytesTransferred / updateDesktopSubSystemSize.toDouble()

                updateMessage("Message $appTotalBytesTransferred");
                updateProgress(appTotalBytesTransferred, updateDesktopSubSystemSize)
            }
        }
    }

    private fun downUpdateSubSystem(){

        backupSubSystem()
        // 重置进度
        resetRateOfProgress()
        logger.info("----------------------------正在更新请稍后...")
        downloadSubSystemFiles(fromPath = system.getFtpSystemPath(), toPath = system.getLocalSystemPath())
        FtpUtil.closeConnect(ftpClient)
        logger.info("----------------------------更新完成...")
        resetRateOfProgress()
    }

    // 递归下载文件夹下所有文件
    private fun  downloadSubSystemFiles(fromPath: String, toPath: String) {

        // 登录
        FtpUtil.login(ftpClient)

        // 创建本地目录
        val curLocalDir = File(toPath)
        if (!curLocalDir.exists()){
            curLocalDir.mkdirs()
        }

        val isSwitchedSuccess = ftpClient.changeWorkingDirectory(fromPath)
        logger.info("""迭代路径: $fromPath ---------------------->FTP切换目录是否成功: $isSwitchedSuccess""")
        if (!isSwitchedSuccess) {
            logger.error("""$fromPath${Constant.DIR_NOT_EXIST}""")
        }

        if (ftpClient.listFiles().isNotEmpty()){
            // 列出所有文件 复制文件

            ftpClient.listFiles().filter { it.isFile }.forEach { ftpFile->
                logger.info("文件: ${ftpFile.name}")

                //复制文件
                var file = File("${toPath}${File.separator}${ftpFile.name}")
                if (!file.exists()){
                    file.createNewFile()
                }
                try {
                    FileOutputStream(file).use { os ->
                        ftpClient.retrieveFile(ftpFile.name, os)
                    }
                } catch (e: Exception) {
                    logger.error(e.message, e)
                }
            }

            ftpClient.listFiles().filter { it.isDirectory }.forEach { ftpFile->
                logger.info("目录: ${ftpFile.name}")

                val newFromPath = "$fromPath${File.separator}${ftpFile.name}"
                val newToPath = "$toPath${File.separator}${ftpFile.name}"
                downloadSubSystemFiles(newFromPath, newToPath)
            }

        }else{
            logger.error("""${system.getFtpSystemPath()}${Constant.DIR_CONTAINS_NO_FILE}""")
        }

    }

    override fun call(): Void? {
        //下载远程程序文件
        try {
            downUpdateSubSystem()
        }catch (e: Exception){
            logger.error("更新系统失败", e)
            restoreSubSystem()
            return null
        }

        // 更新对象状态
        system.updateSystemState()

        // 更新本地csv文件
        system.updateLocalCSV()

        done()
        return null
    }

    override fun scheduled() {
        updateMessage("Message:准备下载");
    }

    override fun succeeded() {
        updateMessage("Message:下载完成");
    }

    override fun cancelled() {
        updateMessage("Message:下载被取消");
    }

    override fun failed() {
        updateMessage("Message:下载失败");
    }
}