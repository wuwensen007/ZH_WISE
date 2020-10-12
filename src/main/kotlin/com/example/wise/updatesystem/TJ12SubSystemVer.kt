package com.example.wise.updatesystem

import com.example.wise.config.Constant
import java.io.File

object TJ12SubSystemVer: SubSystemVer() {

    override fun getSubSystemFtpBasePath(): String {
        return """${Constant.BASEPATH}${File.separator}${Constant.TJ12PATH}"""
    }

    override fun getSubSystemLocalCsvPath(): String{
        return """${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.TJ12CSVNAME}"""
    }

    override fun getSubSystemLocalTempCsvPath(): String {
        return """${Constant.WISE_LOCAL_CSV_TEMP_DIR}${File.separator}${Constant.TJ12CSVNAME}"""
    }

    override fun getSubSystemCsvName(): String {
        return Constant.TJ12CSVNAME
    }

    override fun getLocalSubSystemBasePath(): String {
        return """${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.TJ12PATH}"""
    }

    override fun getCFMIndexUrl(): String {
        TODO("Not yet implemented")
    }

    override fun getReportIndexUrl(): String {
        TODO("Not yet implemented")
    }

    override fun getPMSIndexUrl(): String {
        TODO("Not yet implemented")
    }

    override fun getLocalSubSystemBackupBasePath(): String {
        return "${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.TJ12BACKUPPATH}"
    }

    override fun getLocalSubSystemHelpDocBasePath(): String {
        return "${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.TJ12PATH}${File.separator}${Constant.LOCAL_HELP_DIR}"
    }

    override fun toString(): String {
        return "天津12寸桌面版本"
    }


}