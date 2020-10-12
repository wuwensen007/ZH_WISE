package com.example.wise

import com.example.wise.config.Constant
import com.example.wise.updatesystem.YX8SubSystemVer
import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader
import java.io.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {


    val cmd = "cmd /c cd " + YX8SubSystemVer.getLocalSubSystemBasePath() + File.separator + Constant.SPC + "&&" + "ZH8_ODBC_SPCDB_PROD_RUN.cmd"
    println(cmd)
    Runtime.getRuntime().exec(cmd).waitFor(1, TimeUnit.DAYS)

    //cmd /c cd C:\Users\wws\WISE_ROOT\WISE\YX8SetUp\SPC&&ZH8_ODBC_SPCDB_PROD_RUN.cmd
    //cmd /c cd C:\Users\wws\WISE_ROOT\WISE\YX8SetUp\SPC&&ZH8_INSTALL_PROD_RUN.cmd

//    val exec = Executors.newSingleThreadExecutor()
//    exec.execute{
//        val cmd = "cmd /c cd " + YX8SubSystemVer.getLocalSubSystemBasePath() + File.separator + Constant.SPC + "&&" + "ZH8_INSTALL_PROD_RUN.cmd"
//        println(cmd)
//        Runtime.getRuntime().exec(cmd).waitFor(1, TimeUnit.SECONDS)
//    }
//    exec.shutdown()
//    val input = LineNumberReader(InputStreamReader(v))
//    input.forEachLine {
//        println(String(it.toByteArray(), Charsets.UTF_8))
//    }
}