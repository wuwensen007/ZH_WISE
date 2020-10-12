package com.example.wise.updatesystem

import com.example.wise.config.Constant
import java.io.File

object YX8SubSystemVer: SubSystemVer() {


    override fun getSubSystemFtpBasePath(): String {
        return """${Constant.BASEPATH}${File.separator}${Constant.YX8PATH}"""
    }

    override fun getSubSystemLocalCsvPath(): String{
        return """${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.YX8CSVNAME}"""
    }

    override fun getSubSystemLocalTempCsvPath(): String {
        return """${Constant.WISE_LOCAL_CSV_TEMP_DIR}${File.separator}${Constant.YX8CSVNAME}"""
    }

    override fun getLocalSubSystemBasePath(): String{
        return """${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.YX8PATH}"""
    }

    override fun getCFMIndexUrl(): String {
        return """http://10.210.66.6:9080/wfmwfview/file?p=web/index.html"""
    }

    override fun getReportIndexUrl(): String {
        return """http://10.210.65.101:8080/RPT_Web/"""
    }

    override fun getPMSIndexUrl(): String {
        return """http://10.210.66.6:9080/PMSClientWeb/"""
    }

    override fun getLocalSubSystemBackupBasePath(): String {
        return "${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.YX8BACKUPPATH}"
    }

    override fun getLocalSubSystemHelpDocBasePath(): String {
        return "${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.YX8PATH}"
    }

    override fun getSubSystemCsvName(): String {
        return Constant.YX8CSVNAME
    }

    override fun toString(): String {
        return "宜兴8寸桌面版本"
    }

    fun getLocalOnCallBasePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}${Constant.ONCALL}"
    }

    fun getLocalOPIBasePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}${Constant.OPI}"
    }

    fun getLocalSMBasePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}${Constant.SM}"
    }

    fun getLocalSPCBasePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}${Constant.SPC}"
    }

    fun getLocalOnCallSOPFilePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}${Constant.ONCALL}${File.separator}${Constant.LOCAL_HELP_DIR}${File.separator}ONCALL.doc"
    }

    fun getLocalOPISOPFilePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}${Constant.OPI}${File.separator}${Constant.LOCAL_HELP_DIR}${File.separator}OPI.txt"
    }

    fun getLocalSMSOPFilePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}${Constant.SM}${File.separator}${Constant.LOCAL_HELP_DIR}${File.separator}SM.md"
    }

    fun getLocalSPCSOPFilePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}${Constant.SPC}${File.separator}${Constant.LOCAL_HELP_DIR}${File.separator}SPC.doc"
    }

}