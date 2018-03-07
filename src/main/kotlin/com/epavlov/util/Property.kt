package com.epavlov.util

import java.io.BufferedInputStream
import java.util.*

object Property {
    private val prop = Properties()
    init{
        BufferedInputStream(this.javaClass.classLoader.getResourceAsStream("application.properties")).use {
            prop.load(it)
        }
    }
    fun get(key:String): String {
       return prop.getProperty(key)?:""
    }
}