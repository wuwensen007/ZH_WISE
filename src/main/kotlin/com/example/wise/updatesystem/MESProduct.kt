package com.example.wise.updatesystem

import com.opencsv.bean.CsvBindByName

data class MESProduct(
        @CsvBindByName(column = "SystemName", required = true) var systemName: String = "",
        @CsvBindByName(column = "Type") var type: String = "",
        @CsvBindByName(column = "FileName") var fileName: String = "",
        @CsvBindByName(column = "Path") var path: String = "",
        @CsvBindByName(column = "Version", required = true) var version:String = "",
        @CsvBindByName(column = "UpdateTime") var updateTime: String = "",
        @CsvBindByName(column = "ForceUpdate") var forceUpdate: String = "",
        @CsvBindByName(column = "FTPPath") var ftpPath: String = "",
        @CsvBindByName(column = "SOPFileName") var sopFileName: String = ""){

    fun isEquals(otherMESProduct: MESProduct): Boolean{
        return systemName == otherMESProduct.systemName && version == otherMESProduct.version
    }
}
