package com.example.wise.updatesystem

class SystemProp {

    val properties: MutableMap<String, Any> = mutableMapOf()

    fun getProperty(key: String): Any?{
        if (properties.containsKey(key)){
            return properties[key]
        }
        return null
    }

    fun setProperty(key: String, value: Any){
        properties[key] = value
    }


}