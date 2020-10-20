package com.example.wise.updatesystem

import com.example.wise.config.Constant


abstract class SubSystemVer {

    abstract fun getSubSystemFtpBasePath(): String

    abstract fun getSubSystemLocalCsvPath(): String

    abstract fun getFtpCsvPath(): String

    abstract fun getSubSystemLocalTempCsvPath(): String

    fun getSubSystemCsvName(): String{
        return Constant.CSVNAME
    }

    abstract fun getLocalSubSystemBasePath(): String

    abstract fun getLocalSubSystemBackupBasePath(): String

}