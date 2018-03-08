package com.epavlov

import com.epavlov.chatbot.TwitchPircBot
import com.epavlov.tts.YandexSpeaker
import com.epavlov.util.Property


fun main(args: Array<String>) {
    val channel = Property.get("channels")
    val nick = Property.get("nick")
    val oauth= Property.get("oauth")
    val yandexKey = Property.get("yandex.tts.key")
    val bot=TwitchPircBot(nick,oauth, channel.split(","),YandexSpeaker(yandexKey))
    bot.start()
}