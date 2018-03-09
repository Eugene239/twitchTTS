package com.epavlov.chatbot

import com.epavlov.chatbot.viewers.ViewerListener
import com.epavlov.chatbot.viewers.Viewers
import com.epavlov.tts.speaker.SpeakerI
import org.pircbotx.Configuration
import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.ActionEvent
import org.pircbotx.hooks.events.MessageEvent
import org.slf4j.LoggerFactory


class TwitchPircBot(userName:String, token:String, channels:List<String>,val speaker: SpeakerI) :ListenerAdapter(){
    private val log = LoggerFactory.getLogger(TwitchPircBot::class.java)
    private var bot:PircBotX?=null
    init {
        val configuration = Configuration.Builder()
                .setName(userName)
                .setServerPassword(token)
                .addServer("irc.twitch.tv", 6667)
                .addListener(this)
                .addAutoJoinChannels(channels)
                .buildConfiguration()
        bot = PircBotX(configuration)

    }
    fun start(){

        Viewers.addListener(object :ViewerListener{
            override fun onDisconnect(userName: String) {
                log.info("[DISCONNECTED] $userName")
            }

            override fun onConnected(userName: String) {
                log.info("[CONNECTED] $userName")
                speaker.speak("Приветствую тебя, $userName",speaker.getNames().shuffled()[0])
            }

        })
        Viewers.start()
        //Connect to the server
        bot!!.startBot()


    }

    override fun onMessage(event: MessageEvent?) {
        super.onMessage(event)
        event.let {
            log.info("[MESSAGE] ${event?.channel?.name} from ${event?.user?.nick}: ${event?.message} ")
            speaker.speak(event?.message!!,speaker.getNames().shuffled()[0])
        }
    }

    override fun onAction(event: ActionEvent?) {
        super.onAction(event)
        log.info(event.toString())
    }
}