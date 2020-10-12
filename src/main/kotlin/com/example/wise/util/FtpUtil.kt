package com.example.wise.util

import com.example.wise.config.Constant
import com.example.wise.config.Constant.DIR_CONTAINS_NO_FILE
import com.example.wise.config.Constant.DIR_NOT_EXIST
import com.example.wise.config.Constant.LOCAL_WISE_BASE_PATH
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.util.*


class FtpUtil {

    companion object {
        /**
         * 日志对象
         */
        private val logger = LoggerFactory.getLogger(FtpUtil::class.java)

        /**
         * 本地字符编码
         */
        private var localCharset = "GBK"

        /**
         * FTP协议里面，规定文件名编码为iso-8859-1
         */
        private const val serverCharset = "ISO-8859-1"

        /**
         * UTF-8字符编码
         */
        private const val CHARSET_UTF8 = "UTF-8"

        /**
         * OPTS UTF8字符串常量
         */
        private const val OPTS_UTF8 = "OPTS UTF8"

        /**
         * 设置缓冲区大小4M
         */
        const val BUFFER_SIZE = 1024 * 1024 * 4

        /**
         * FTPClient对象
         */
        private val ftpClient: FTPClient = FTPClient()

        /**
         * 连接FTP服务器
         *
         * @param address  地址，如：127.0.0.1
         * @param port     端口，如：21
         * @param username 用户名，如：root
         * @param password 密码，如：root
         */



        fun login(ftpClient: FTPClient, address: String = Constant.HOST, port: String = Constant.PORT, username: String = Constant.USER, password: String = Constant.PASSWORD) {
            synchronized(this){
                try {
                    ftpClient.connect(address, port.toInt())
                    ftpClient.login(username, password)
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE)
                    //限制缓冲区大小
                    ftpClient.bufferSize = FtpUtil.BUFFER_SIZE
                    ftpClient.enterLocalPassiveMode() // 设置被动模式，开通一个端口来传输数据
                    val reply = ftpClient.replyCode
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        closeConnect(ftpClient)
                        logger.error("FTP服务器连接失败")
                    }
                } catch (e: Exception) {
                    logger.error("FTP登录失败", e)

                }
            }
        }

        /**
         * FTP服务器路径编码转换
         *
         * @param ftpPath FTP服务器路径
         * @return String
         */
        private fun changeEncoding(ftpPath: String): String? {

            synchronized(this){
                var directory: String? = null
                try {
                    if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(OPTS_UTF8, "ON"))) {
                        localCharset = CHARSET_UTF8
                    }
                    directory = String(ftpPath.toByteArray(charset(localCharset)), Charset.forName(serverCharset))
                } catch (e: Exception) {
                    logger.error("路径编码转换失败", e)
                }
                return directory
            }
        }

        /**
         * 关闭FTP连接
         */
        fun closeConnect(ftpClient: FTPClient) {


            if (ftpClient.isConnected) {
                try {
                    ftpClient.logout()
                    ftpClient.disconnect()
                    logger.info("""FTP连接关闭""")
                } catch (e: IOException) {
                    logger.error("关闭FTP连接失败", e)
                }
            }

        }


        /**
         * 下载该目录下所有文件到本地
         *
         * @param ftpPath  FTP服务器上的相对路径，例如：test/123
         * @param savePath 保存文件到本地的路径，例如：D:/test
         * @return 成功返回true，否则返回false
         */
        fun downloadCSVFiles(savePath: String = Constant.WISE_LOCAL_CSV_TEMP_DIR, csvFileName: String) {

            // 登录
            login(ftpClient)
            try {
                val path = changeEncoding(Constant.BASEPATH)
                // 判断是否存在该目录
                if (!ftpClient.changeWorkingDirectory(path)) {
                    logger.error("""${Constant.BASEPATH}$DIR_NOT_EXIST""")
                    return
                }
                ftpClient.enterLocalPassiveMode() // 设置被动模式，开通一个端口来传输数据

                // 判断该目录下是否有文件
                if (ftpClient.listFiles().isNotEmpty()){

                    val ftpFile = ftpClient.listFiles().first { it.name == csvFileName }
                    val file = File("$savePath${File.separator}${ftpFile.name}")
                    if (!file.parentFile.exists()){
                        //创建文件
                        file.parentFile.mkdirs()
                        file.createNewFile()
                    }
                    try {
                        FileOutputStream(file).use { os -> ftpClient.retrieveFile(ftpFile.name, os) }
                    } catch (e: Exception) {
                        logger.error(e.message, e)
                    }

                }else{
                    logger.error("""${Constant.BASEPATH}$DIR_CONTAINS_NO_FILE""")
                }

            } catch (e: IOException) {
                logger.error("下载文件失败", e)
            } finally {
                closeConnect(ftpClient)
            }
        }


        fun getFilesSize(path: String): Long{
            // 登录

            login(ftpClient)
            var totalSize = 0L

            if (!ftpClient.changeWorkingDirectory(path)) {
                logger.error("""$path$DIR_NOT_EXIST""")
                // 关闭连接
                closeConnect(ftpClient)
                throw RuntimeException("连接失败")
            }
            ftpClient.enterLocalPassiveMode()
            if (ftpClient.listFiles().isNotEmpty()){
                // 列出所有文件 复制文件
                totalSize += ftpClient.listFiles().filter { it.isFile }.map { it.size }.sum()
                ftpClient.listFiles().filter { it.isDirectory }.forEach { ftpFile->
                    // 递归下载目录下的文件
                    totalSize += getFilesSize("$path${File.separator}${ftpFile.name}")
                }

            }
            return totalSize
        }


        fun getSubSystemFilesSize(ftpSubSystemPath: String, ftpCurrentDir: String = ""): Long{

                // 登录
                login(ftpClient)

                var totalSize = 0L

                val path = changeEncoding("""${ftpSubSystemPath}${File.separator}$ftpCurrentDir""")
                // 判断是否存在该目录 递归结束条件
                if (!ftpClient.changeWorkingDirectory(path)) {
                    logger.error("""$ftpSubSystemPath$DIR_NOT_EXIST""")
                    // 关闭连接
                    closeConnect(ftpClient)
                    throw RuntimeException("连接失败")
                }
                ftpClient.enterLocalPassiveMode()
                if (ftpClient.listFiles().isNotEmpty()){
                    // 列出所有文件 复制文件
                    totalSize += ftpClient.listFiles().filter { it.isFile }.map { it.size }.sum()
                    ftpClient.listFiles().filter { it.isDirectory }.forEach { ftpFile->
                        // 递归下载目录下的文件
                        totalSize += getSubSystemFilesSize(ftpSubSystemPath, ftpCurrentDir = ftpFile.name)
                    }

                }
                return totalSize
        }

        /**
         * 下载该目录下所有文件到本地
         *
         * @param ftpPath  FTP服务器上的相对路径，例如：test/123
         * @param savePath 保存文件到本地的路径，例如：D:/test
         * @return 成功返回true，否则返回false
         */
        fun downloadFiles(ftpPath: String, savePath: String): Boolean {
            // 登录
            login(ftpClient)

            try {
                // 判断是否存在该目录
                if (!ftpClient.changeWorkingDirectory(ftpPath)) {
                    logger.error("""$ftpPath$DIR_NOT_EXIST""")
                    return false
                }
                ftpClient.enterLocalPassiveMode() // 设置被动模式，开通一个端口来传输数据

                ftpClient.listFiles().filter { it.isFile }.forEach {

                    val file = File("$savePath${File.separator}${it.name}")
                    if (!file.exists()){
                        file.createNewFile()
                    }
                    try {
                        logger.info("""开始下载文件:${file.toPath()}""")
                        FileOutputStream(file).use { os -> ftpClient.retrieveFile(it.name, os) }
                    } catch (e: Exception) {
                        logger.error(e.message, e)
                    }

                }
            } catch (e: IOException) {
                logger.error("下载文件失败", e)
            } finally {
                closeConnect(ftpClient)
            }
            return true
        }


        /**
         * 检查指定目录下是否含有指定文件
         *
         * @param ftpPath  FTP服务器文件相对路径，例如：test/123
         * @param fileName 要下载的文件名，例如：test.txt
         * @return 成功返回true，否则返回false
         */
        fun checkFileInFtp(ftpPath: String, fileName: String): Boolean {
            // 登录
            login(ftpClient)


            try {
                val path = changeEncoding("""${Constant.BASEPATH}${File.separator}${ftpPath}""")
                // 判断是否存在该目录
                if (!ftpClient.changeWorkingDirectory(path)) {
                    logger.error("""${Constant.BASEPATH}${File.separator}$ftpPath$DIR_NOT_EXIST""")
                    return false
                }
                ftpClient.enterLocalPassiveMode() // 设置被动模式，开通一个端口来传输数据
                val fs = ftpClient.listNames()
                // 判断该目录下是否有文件
                if (fs == null || fs.isEmpty()) {
                    logger.error("""${Constant.BASEPATH}/$ftpPath$DIR_CONTAINS_NO_FILE""")
                    return false
                }else{
                    for (ff in fs) {
                        val ftpName = String(ff.toByteArray(charset(serverCharset)), Charset.forName(localCharset))
                        if (ftpName == fileName) {
                            return true
                        }
                    }
                }
                return false
            } catch (e: IOException) {
                logger.error("请求出错", e)
            } finally {
                closeConnect(ftpClient)
            }
            return false
        }

        /**
         * 下载该目录下所有文件到本地 根据实际需要修改执行逻辑
         *
         * @param ftpPath  FTP服务器上的相对路径，例如：test/123
         * @param savePath 保存文件到本地的路径，例如：D:/test
         * @return 成功返回true，否则返回false
         */
        fun downLoadTableFile(ftpPath: String, savePath: String): Map<String, Any> {
            // 登录
            login(ftpClient)
            val resultMap: MutableMap<String, Any> = HashMap()

            try {
                val path = changeEncoding("""${Constant.BASEPATH}${File.separator}${ftpPath}""")
                // 判断是否存在该目录
                if (!ftpClient.changeWorkingDirectory(path)) {
                    logger.error("${Constant.BASEPATH}${File.separator}$ftpPath$DIR_NOT_EXIST")
                    resultMap["result"] = false
                    return resultMap
                }
                ftpClient.enterLocalPassiveMode() // 设置被动模式，开通一个端口来传输数据
                val fs = ftpClient.listNames()
                // 判断该目录下是否有文件
                if (fs == null || fs.isEmpty()) {
                    logger.error("${Constant.BASEPATH}${File.separator}$ftpPath$DIR_CONTAINS_NO_FILE")
                    resultMap["result"] = false
                    return resultMap
                }
                val tableFileNameList: MutableList<String> = ArrayList()
                //根据表名创建文件夹
                val tableDirName = "$savePath/$ftpPath"
                val tableDirs = File(tableDirName)
                if (!tableDirs.exists()) {
                    tableDirs.mkdirs()
                }
                for (ff in fs) {
                    val ftpName = String(ff.toByteArray(charset(serverCharset)), Charset.forName(localCharset))
                    val file = File("$tableDirName/$ftpName")
                    //存储文件名导入时使用
                    tableFileNameList.add("$tableDirName/$ftpName")
                    try {
                        FileOutputStream(file).use { os -> ftpClient.retrieveFile(ff, os) }
                    } catch (e: Exception) {
                        logger.error(e.message, e)
                    }
                }
                resultMap["fileNameList"] = tableFileNameList
                resultMap["result"] = true
                return resultMap
            } catch (e: IOException) {
                logger.error("下载文件失败", e)
            } finally {
                closeConnect(ftpClient)
            }

            resultMap["result"] = false
            return resultMap
        }

    }
}