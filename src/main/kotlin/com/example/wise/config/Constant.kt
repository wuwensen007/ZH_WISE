package com.example.wise.config

import java.io.File
import java.io.InputStream
import java.util.*

object Constant {

    private val prop: Properties = Properties()

    init {
        // 读取配置文件的信息

        val inStream = Constant::class.java.classLoader.getResourceAsStream("application.properties")
        prop.load(inStream)
    }


    /**
     * 该目录不存在
     */
    const val DIR_NOT_EXIST = "该目录不存在"

    /**
     * 本地WISE目录
     */
    val LOCAL_WISE_BASE_PATH = """${System.getProperty("user.home")}${File.separator}WISE_ROOT${File.separator}${prop.getProperty("myconfig.basepath")}"""

//    val LOCAL_WISE_BASE_PATH = """D:${File.separator}WISE_ROOT${File.separator}${prop.getProperty("myconfig.basepath")}"""
    /**
     *
     *
     * FTP上下载的文件临时目录
     */
    val WISE_LOCAL_CSV_TEMP_DIR = """${System.getProperty("user.home")}${File.separator}WISE_ROOT${File.separator}${prop.getProperty("myconfig.basepath")}${File.separator}${prop.getProperty("myconfig.csvtempdir")}"""

//    val WISE_LOCAL_CSV_TEMP_DIR = """D:${File.separator}WISE_ROOT${File.separator}${prop.getProperty("myconfig.basepath")}${File.separator}${prop.getProperty("myconfig.csvtempdir")}"""
    /**
     * "该目录下没有文件
     */
    const val DIR_CONTAINS_NO_FILE = "该目录下没有文件"

    val HOST = prop.getProperty("myconfig.host")

    val PORT = prop.getProperty("myconfig.port")

    val USER = prop.getProperty("myconfig.username")

    val PASSWORD = prop.getProperty("myconfig.password")

    val YX12PATH = prop.getProperty("myconfig.yx12path")

    val YX8PATH = prop.getProperty("myconfig.yx8path")

    val TJ12PATH = prop.getProperty("myconfig.tj12path")

    val BASEPATH = prop.getProperty("myconfig.basepath")

    val OPI = prop.getProperty("myconfig.opi")

    val SPC = prop.getProperty("myconfig.spc")

    val SM = prop.getProperty("myconfig.sm")

    val ONCALL = prop.getProperty("myconfig.oncall")

    val YX8CSVNAME = prop.getProperty("myconfig.yx8csvname")

    val YX12CSVNAME = prop.getProperty("myconfig.yx12csvname")

    val TJ12CSVNAME = prop.getProperty("myconfig.tj12csvname")

    val CSVTEMPDIR = prop.getProperty("myconfig.csvtempdir")

    val YX8BACKUPPATH = prop.getProperty("myconfig.yx8backuppath")

    val YX12BACKUPPATH = prop.getProperty("myconfig.yx12backuppath")

    val TJ12BACKUPPATH = prop.getProperty("myconfig.tj12backuppath")

    val LOCAL_DB_URL = prop.getProperty("localdb.url")

    val LOCAL_DB_DRIVER = prop.getProperty("localdb.driver")

    val LOCAL_DB_USER = prop.getProperty("localdb.user")

    val LOCAL_DB_PASSWORD = prop.getProperty("localdb.password")

    val LOCAL_HELP_DIR = prop.getProperty("myconfig.dochelpdir")

}