package com.example.wise.updatesystem


import javafx.beans.property.SimpleStringProperty
import org.slf4j.LoggerFactory

// 子系统默认8寸
abstract class SubSystem(var systemName: String, var systemVersion: SimpleStringProperty, var updateTime: String, var runCommand: String, var factoryVer: SubSystemVer, var systemProp: SystemProp) {

    private val logger = LoggerFactory.getLogger(SubSystem::class.java)
    // 启动程序
    fun runSystem(){
        logger.info(runCommand)
        val isAlive = Runtime.getRuntime().exec(runCommand)
        logger.info("$systemName $isAlive")
    }

    override fun equals(other: Any?): Boolean {

        val remoteSubSystem: SubSystem = other as SubSystem
        if (remoteSubSystem.systemVersion.valueSafe != systemVersion.valueSafe || remoteSubSystem.systemName != systemName){
            return false
        }
        return true
    }

    override fun toString(): String {
        return "SubSystem(systemName='$systemName', systemVersion='$systemVersion', updateTime=$updateTime)"
    }
}