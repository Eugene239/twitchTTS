package com.epavlov.tts

import com.epavlov.tts.speaker.SpeakerI
import com.squareup.okhttp.HttpUrl
import org.slf4j.LoggerFactory
import javax.sound.sampled.AudioSystem


class YandexSpeaker(private val apiKey:String) :SpeakerI {
    private val log = LoggerFactory.getLogger(YandexSpeaker::class.java)

    private data class Speaker(var name: String, var gender: Boolean)
    private val values = ArrayList<Speaker>()
    init {
        values.add(Speaker("jane", false))
        values.add(Speaker("oksana", false))
        values.add(Speaker("alyss", false))
        values.add(Speaker("omazh", false))
        values.add(Speaker("zahar", true))
        values.add(Speaker("ermil", true))
    }
    enum class Emotion {
        GOOD, NEUTRAL, EVIL
    }
    override fun speak(text:String, name:String){
        speak(text,name,Emotion.values().toList().shuffled()[0])
    }
    fun speak(text: String, name: String,emotion: Emotion){
        log.info("[SPEAK] $name: $text ${emotion.name}")
        synchronized(this) {
            val url = HttpUrl.parse("https://tts.voicetech.yandex.net/generate?" +
                    "key=$apiKey&" +
                    "text=${text.replace("+", "%2B")}&" +
                    "format=wav&" +
                    "quality=hi&" +
                    "lang=ru-RU&" +
                    "speaker=$name&" +
                    "speed=1.0&" +
                    "emotion=${emotion.name}").url()
            log.debug(url.toString())
            val clip = AudioSystem.getClip()
            clip.open(AudioSystem.getAudioInputStream(url))
            clip.start()
            Thread.sleep(100)
            while (clip.isActive) {
                Thread.sleep(100)
            }
        }
    }
    override fun getNames(): List<String> {
       return values.map { it.name }
    }
}