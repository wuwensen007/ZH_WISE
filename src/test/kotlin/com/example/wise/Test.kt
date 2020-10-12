package com.example.wise


import com.example.wise.announcement.Note
import com.example.wise.config.Constant
import com.example.wise.util.FtpUtil
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import javax.swing.filechooser.FileSystemView


class Test {


    @BeforeEach
    fun initDB(){
        Database.connect(
                url = Constant.LOCAL_DB_URL,
                driver = Constant.LOCAL_DB_DRIVER,
                user = Constant.LOCAL_DB_USER,
                password = Constant.LOCAL_DB_PASSWORD)
    }

    @Test
    fun test1(){

        transaction {
            Note.all().forEach {
                println(it)
            }
        }
    }

    @Test
    fun test2(){
        val fsv = FileSystemView.getFileSystemView()
        val com: File = fsv.homeDirectory
        System.out.println(com.getPath()) //这便是桌面的具体路径
    }

    @Test
    fun test3(){

        fun getPath(): String{
            var path = this.javaClass.protectionDomain.codeSource.location.path
            if (System.getProperty("os.name").contains("dows")) {
                path = path.substring(1, path.length)
            }
            if (path.contains("jar")) {
                path = path.substring(0, path.lastIndexOf("."))
                return path.substring(0, path.lastIndexOf("/"))
            }
            return path.replace("target/classes/", "")
        }

        println(getPath())
    }

    @Test
    fun test4(){

        val userDir = System.getProperty("user.home") //项目的当前工作目录

        println("userDir:   $userDir")

    }

    @Test
    fun test5(){
        println(FtpUtil.getFilesSize(Constant.BASEPATH))
    }

}
