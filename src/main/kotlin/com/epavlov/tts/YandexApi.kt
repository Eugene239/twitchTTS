package com.epavlov.tts

import com.epavlov.tts.speaker.Emotion
import com.epavlov.tts.speaker.Speaker
import com.squareup.okhttp.HttpUrl
import javax.sound.sampled.AudioSystem

object YandexApi {

    fun speak(key:String, text:String, speaker: Speaker,emotion: Emotion){
        println("${speaker.name}[${emotion.name}]: $text ")
        val url = "https://tts.voicetech.yandex.net/generate?" +
                "key=$key&" +
                "text=${text.replace("+","%2B")}&" +
                "format=wav&" +
                "quality=hi&" +
                "lang=ru-RU&" +
                "speaker=${speaker.name}&" +
                "speed=1.2&"+
                "emotion=${emotion.name}"
        val clip = AudioSystem.getClip()
        clip.open(AudioSystem.getAudioInputStream(HttpUrl.parse(url).url()))
        clip.start()
        Thread.sleep(100)
        while (clip.isActive) { Thread.sleep(100)}
    }

}