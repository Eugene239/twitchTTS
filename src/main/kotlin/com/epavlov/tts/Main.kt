package com.epavlov.tts

import com.epavlov.util.Property
import com.epavlov.tts.speaker.Emotion
import com.epavlov.tts.speaker.SpeakerList

fun main(args: Array<String>) {
    YandexApi.speak(Property.get("yandex.tts.key"),"Привет, б+обры", SpeakerList.getValues().shuffled()[0],Emotion.values().toList().shuffled()[0])
    YandexApi.speak(Property.get("yandex.tts.key"),"Здор+ово, бобры", SpeakerList.getValues().shuffled()[0],Emotion.values().toList().shuffled()[0])
}