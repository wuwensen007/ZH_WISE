package com.example.wise.updatesystem


enum class FAB(private var fabName: String) {

    YX8("宜兴8寸"), YX12("宜兴12寸"),TJ12("天津12寸");

    companion object{

        private val stringToEnum = values().map { toString() to it }.toMap()

        fun fromString(fabName: String): FAB{
            return stringToEnum.getValue(fabName)
        }

    }

    override fun toString(): String {
        return fabName
    }


}