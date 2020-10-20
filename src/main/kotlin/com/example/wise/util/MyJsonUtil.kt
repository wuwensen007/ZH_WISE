package com.example.wise.util

import com.alibaba.fastjson.JSONObject
import com.example.wise.updatesystem.FAB
import com.example.wise.updatesystem.LocalUserConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files

object MyJsonUtil {
    var TEMP_FILE_PATH = System.getProperty("java.io.tmpdir") + "user_onCall.json"
    fun readJsonFile(): LocalUserConfig {
        val localUserConfig = LocalUserConfig()
        val jsonTmpFile = File(TEMP_FILE_PATH)
        try {
            val content: String = String(Files.readAllBytes(jsonTmpFile.toPath()))

            val jsonObject = JSONObject.parse(content) as JSONObject
            val username: String = jsonObject.getString("username")
            val password: String = jsonObject.getString("password")
            if (!username.isEmpty() && !password.isEmpty()) {
                localUserConfig.setUsername(jsonObject.getString("username"))
                localUserConfig.setPassword(jsonObject.getString("password"))
            }
            localUserConfig.setRememberPassword(jsonObject.getBoolean("rememberPwd"))
            localUserConfig.setAutoLogin(jsonObject.getBoolean("autoLogin"))
            localUserConfig.setFab(FAB.valueOf(jsonObject.getString("fab")))
            localUserConfig.setFirstLogin(jsonObject.getBoolean("firstLogin"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return localUserConfig
    }

    fun writeJsonFile(userConfig: LocalUserConfig) {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(TEMP_FILE_PATH)
            out.write(userConfig.toString().toByteArray())
            out.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun initJsonFile(userConfig: LocalUserConfig) {
        val tempFile = File(TEMP_FILE_PATH)
        if (!tempFile.exists()) {
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(TEMP_FILE_PATH)
                out.write(userConfig.toString().toByteArray())
                out.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    out!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}