package com.example.wise.updatesystem

import com.example.wise.config.Constant
import java.io.File

object YX12SubSystemVer: SubSystemVer() {

    override fun getSubSystemFtpBasePath(): String {
        return """${Constant.BASEPATH}${File.separator}${Constant.YX12PATH}"""
    }

    override fun getSubSystemLocalCsvPath(): String{
        return """${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.YX12PATH}${File.separator}${Constant.CSVNAME}"""
    }

    override fun getFtpCsvPath(): String {
        return "${Constant.BASEPATH}${File.separator}${Constant.YX12PATH}"
    }

    override fun getSubSystemLocalTempCsvPath(): String {
        return """${Constant.WISE_LOCAL_CSV_TEMP_DIR}${File.separator}${Constant.YX12PATH}${File.separator}${Constant.CSVNAME}"""
    }

    override fun getLocalSubSystemBasePath(): String {
        return """${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.YX12PATH}"""
    }

    override fun getLocalSubSystemBackupBasePath(): String {
        return "${Constant.LOCAL_WISE_BASE_PATH}${File.separator}${Constant.YX12BACKUPPATH}"
    }

    override fun toString(): String {
        return "宜兴12寸桌面版本"
    }

    fun getLocalOnCallBasePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}SetUp${File.separator}${Constant.ONCALL}"
    }

    fun getLocalOPIBasePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}SetUp${File.separator}${Constant.OPI}"
    }

    fun getLocalSMBasePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}SetUp${File.separator}${Constant.SM}"
    }

    fun getLocalSPCBasePath(): String{
        return "${getLocalSubSystemBasePath()}${File.separator}SetUp${File.separator}${Constant.SPC}"
    }

    fun getLocalOnCallSOPFilePath(): String{
        return "${getLocalOnCallBasePath()}${File.separator}${Constant.LOCAL_HELP_DIR}"
    }

    fun getLocalOPISOPFilePath(): String{
        return "${getLocalOPIBasePath()}${File.separator}${Constant.LOCAL_HELP_DIR}"
    }

    fun getLocalSMSOPFilePath(): String{
        return "${getLocalSMBasePath()}${File.separator}${Constant.LOCAL_HELP_DIR}"
    }

    fun getLocalSPCSOPFilePath(): String{
        return "${getLocalSPCBasePath()}${File.separator}${Constant.LOCAL_HELP_DIR}"
    }

}