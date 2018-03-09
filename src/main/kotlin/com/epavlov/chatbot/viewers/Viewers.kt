package com.epavlov.chatbot.viewers

import com.epavlov.util.Property
import com.google.gson.Gson
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

data class ViewerEntity(
        val chatter_count: Int,
        val chatters: Chatters
)

data class Chatters(
        val moderators: List<String>,
        val staff: List<Any>,
        val admins: List<Any>,
        val global_mods: List<Any>,
        val viewers: List<String>
)

interface  ViewerListener{
    fun onDisconnect(userName:String)
    fun onConnected(userName: String)
}

object Viewers {
    private val log = LoggerFactory.getLogger(Viewers::class.java)
    private val period = 2000L
    private var url: String? = ""
    private val gson = Gson()
    private val viewers = ArrayList<String>()
    private val listeners = ArrayList<ViewerListener>()
    private var timer : TimerTask?=null
    init {
        url = "https://tmi.twitch.tv/group/user/${Property.get("nick")}/chatters"
    }

    private fun getJson(): String {
        val client = OkHttpClient()
        val request = Request.Builder()
                .get()
                .url(url)
                .build()
        val response= client.newCall(request).execute().body().string()
        log.debug("response: $response")
        return response
    }

    private fun getList(): List<String> {
        try {
            val data = gson.fromJson(getJson(), ViewerEntity::class.java)
            return data.chatters.viewers
        } catch (e: Exception) {
            log.error(e.toString(), e)
        }
        return listOf()
    }

    fun start() {
        timer= Timer("getViewers", true)
                .schedule(1000, period) {
                    log.debug("start task ${LocalDateTime.now()}")
                    val newList = getList()
                    log.debug("new Data: $newList")
                    newList.filter { !viewers.contains(it) }.forEach {userName->
                        listeners.forEach { it.onConnected(userName) }
                    }
                    viewers.filter {!newList.contains(it) }.forEach { userName->
                        listeners.forEach { it.onDisconnect(userName)
                        }
                    }
                    viewers.clear()
                    viewers.addAll(newList)
                }
    }
    fun stop(){
        timer?.let {
            timer!!.cancel()
        }
    }

    fun addListener(listener: ViewerListener){
        listeners.add(listener)
    }
    fun removeListener(listener: ViewerListener){
        listeners.remove(listener)
    }
}