package com.example.wise.updatesystem

import com.example.wise.factory.TJ12SubSystemFactory
import com.example.wise.factory.YX12SubSystemFactory
import com.example.wise.factory.YX8SubSystemFactory
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableMap
import java.util.concurrent.Executors

object SubSystemManager {


    var curSystems: SimpleObjectProperty<ObservableMap<String, SimpleObjectProperty<SubSystem>>> = SimpleObjectProperty()



    fun initSubSystem(){
        val exec = Executors.newSingleThreadExecutor()
        exec.execute{
            // 初始化本地用户信息
            if (!YX8SubSystemFactory.isInit){
                YX8SubSystemFactory.initYX8Systems()
            }

            if (!YX12SubSystemFactory.isInit){
                YX12SubSystemFactory.initYX12Systems()
            }

            if (!TJ12SubSystemFactory.isInit){
                TJ12SubSystemFactory.initTJ12Systems()
            }
        }
        exec.shutdown()
    }



}