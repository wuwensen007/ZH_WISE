package com.example.wise.updatesystem

import javafx.beans.property.SimpleStringProperty


class WebApp(systemName: String, systemVersion: SimpleStringProperty, updateTime: String, runCommand: String, factoryVer: SubSystemVer, systemProp: SystemProp): SubSystem(systemName, systemVersion, updateTime, runCommand, factoryVer, systemProp)