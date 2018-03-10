package com.epavlov.chatbot.viewers

import com.epavlov.util.Property
import com.google.gson.Gson
import com.squareup.okhttp.CacheControl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
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
    private const val period = 10000L
    private var url: String? = ""
    private val gson = Gson()
    private val  viewers = Collections.synchronizedList(ArrayList<String>())
    private val listeners = ArrayList<ViewerListener>()
    private var timer : TimerTask?=null
    init {
        url = "https://tmi.twitch.tv/group/user/${Property.get("nick")}/chatters"
    }

    @Throws(Exception::class)
    private fun getJson(): String  {
        val client = OkHttpClient()
        client.setConnectTimeout(period.div(2),TimeUnit.SECONDS)
        val request = Request.Builder()
                .get()
                .url(url)
                .cacheControl(CacheControl.Builder().noCache().build())
                .build()
         return try {
             client.newCall(request).execute().body().string()
         }catch (e:IOException){
             log.error(e.toString())
             return ""
         }
    }

    private fun getList(): List<String> {
        try {
            val data = gson.fromJson(getJson(), ViewerEntity::class.java)
            data?.chatters?.viewers?.let {
                return it
            }
        } catch (e: Exception) {
            log.error(e.toString(), e)
        }
        log.debug("returning $viewers")
        return viewers
    }

    fun start() {
        timer= Timer("getViewers", true)
                .schedule(1000, period) {
                    val newList = getList()
                    log.debug("new Data: $newList  old Data: $viewers")
                    newList.filter { !viewers.contains(it) }.forEach {userName->
                        viewers.add(userName)
                        listeners.forEach { it.onConnected(userName) }
                    }
                    viewers.filter {!newList.contains(it) }.forEach { userName->
                        viewers.remove(userName)
                        listeners.forEach { it.onDisconnect(userName) }
                    }
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