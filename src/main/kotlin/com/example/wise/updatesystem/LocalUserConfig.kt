package com.example.wise.updatesystem

import com.example.wise.updatesystem.FAB
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty

class LocalUserConfig {
    private var username = SimpleStringProperty("")
    private var password = SimpleStringProperty("")
    private var fab: SimpleObjectProperty<FAB?> = SimpleObjectProperty(FAB.YX8)
    private var rememberPassword = SimpleBooleanProperty(false)
    private var autoLogin = SimpleBooleanProperty(false)
    private var firstLogin = SimpleBooleanProperty(true)

    constructor() {}
    constructor(username: SimpleStringProperty, password: SimpleStringProperty, fab: SimpleObjectProperty<FAB?>, rememberPassword: SimpleBooleanProperty, autoLogin: SimpleBooleanProperty, firstLogin: SimpleBooleanProperty) {
        this.username = username
        this.password = password
        this.fab = fab
        this.rememberPassword = rememberPassword
        this.autoLogin = autoLogin
        this.firstLogin = firstLogin
    }

    fun getUsername(): String {
        return username.get()
    }

    fun usernameProperty(): SimpleStringProperty {
        return username
    }

    fun setUsername(username: String?) {
        this.username.set(username)
    }

    fun getPassword(): String {
        return password.get()
    }

    fun passwordProperty(): SimpleStringProperty {
        return password
    }

    fun setPassword(password: String?) {
        this.password.set(password)
    }

    fun isRememberPassword(): Boolean {
        return rememberPassword.get()
    }

    fun rememberPasswordProperty(): SimpleBooleanProperty {
        return rememberPassword
    }

    fun setRememberPassword(rememberPassword: Boolean) {
        this.rememberPassword.set(rememberPassword)
    }

    fun isAutoLogin(): Boolean {
        return autoLogin.get()
    }

    fun autoLoginProperty(): SimpleBooleanProperty {
        return autoLogin
    }

    fun setAutoLogin(autoLogin: Boolean) {
        this.autoLogin.set(autoLogin)
    }

    fun getFab(): FAB? {
        return fab.get()
    }

    fun fabProperty(): SimpleObjectProperty<FAB?> {
        return fab
    }

    fun setFab(fab: FAB?) {
        this.fab.set(fab)
    }

    fun isFirstLogin(): Boolean {
        return firstLogin.get()
    }

    fun firstLoginProperty(): SimpleBooleanProperty {
        return firstLogin
    }

    fun setFirstLogin(firstLogin: Boolean) {
        this.firstLogin.set(firstLogin)
    }

    override fun toString(): String {
        return """{
            "username": "${username.value}",
            "password": "${password.value}",
            "fab": "${fab.value!!.name}",
            "rememberPwd": ${rememberPassword.value},
            "autoLogin": ${autoLogin.value},
            "firstLogin": ${firstLogin.value}
        }"""
    }
}