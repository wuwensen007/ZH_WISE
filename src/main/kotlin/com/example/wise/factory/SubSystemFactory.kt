package com.example.wise.factory

import com.example.wise.updatesystem.DesktopSubSystem
import com.example.wise.updatesystem.SubSystem
import com.example.wise.updatesystem.WebApp
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableMap

abstract class SubSystemFactory {

    abstract fun getSubSystems(): ObservableMap<String,SimpleObjectProperty<SubSystem>>

    abstract fun getDesktopSystems(): ObservableMap<String,SimpleObjectProperty<DesktopSubSystem>>
}