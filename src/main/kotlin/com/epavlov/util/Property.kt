package com.epavlov.util

import java.io.FileInputStream
import java.util.*

object Property {
    private val prop = Properties()
    init{
        FileInputStream("src/main/resources/application.properties").use {
            prop.load(it)
        }
    }
    fun get(key:String): String {
       return prop.getProperty(key)?:""
    }
}