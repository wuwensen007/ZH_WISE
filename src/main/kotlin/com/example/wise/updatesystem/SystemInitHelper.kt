package com.example.wise.updatesystem

import com.example.wise.config.Constant
import com.example.wise.util.FtpUtil
import com.example.wise.util.FtpUtil.Companion.downloadFiles
import javafx.concurrent.Task
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.io.CopyStreamAdapter
import org.apache.commons.net.io.CopyStreamListener
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream

class SystemInitHelper: Task<Void>() {

    var updateDesktopSubSystemSize: Long = FtpUtil.getFilesSize(Constant.BASEPATH)
    var appTotalBytesTransferred = 0L
    var updateRateOfProcess = 0.0


    private val ftpClient: FTPClient = FTPClient()

    init {
        ftpClient.copyStreamListener = createListener()
    }

    private val logger = LoggerFactory.getLogger(SystemInitHelper::class.java)

    companion object{
        fun whetherToInitialize(): Boolean {
            // 登录
            val file = File(Constant.LOCAL_WISE_BASE_PATH)
            if (!file.exists()){
                file.mkdirs()
                return true
            }
            return file.listFiles().isEmpty()
        }
    }



    fun initLocalSystem(){
        downloadFiles(fromPath = Constant.BASEPATH, toPath = Constant.LOCAL_WISE_BASE_PATH)
    }

    private fun downloadFiles(fromPath: String, toPath: String) {

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
                val file = File("${toPath}${File.separator}${ftpFile.name}")
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
                downloadFiles(newFromPath, newToPath)
            }

        }else{
            logger.error(Constant.DIR_CONTAINS_NO_FILE)
        }

    }








    private fun createListener(): CopyStreamListener {

        return object : CopyStreamAdapter() {
            override fun bytesTransferred(totalBytesTransferred: Long,
                                          bytesTransferred: Int, streamSize: Long) {

                appTotalBytesTransferred += bytesTransferred
                updateRateOfProcess = appTotalBytesTransferred / updateDesktopSubSystemSize.toDouble()

                updateMessage("Message $appTotalBytesTransferred")
                updateProgress(appTotalBytesTransferred, updateDesktopSubSystemSize)
            }
        }
    }





    override fun call(): Void? {
        initLocalSystem()
        return null
    }

}