package com.example.wise.updatesystem


abstract class SubSystemVer {

    abstract fun getSubSystemFtpBasePath(): String

    abstract fun getSubSystemLocalCsvPath(): String

    abstract fun getSubSystemLocalTempCsvPath(): String

    abstract fun getSubSystemCsvName(): String

    abstract fun getLocalSubSystemBasePath(): String

    abstract fun getCFMIndexUrl(): String

    abstract fun getReportIndexUrl(): String

    abstract fun getPMSIndexUrl(): String

    abstract fun getLocalSubSystemBackupBasePath(): String

    abstract fun getLocalSubSystemHelpDocBasePath(): String
}