package com.epavlov.tts

import com.epavlov.tts.speaker.Emotion
import com.epavlov.tts.speaker.SpeakerList

fun main(args: Array<String>) {
    YandexApi.speak("Привет, б+обры", SpeakerList.getValues().shuffled()[0],Emotion.values().toList().shuffled()[0])
    YandexApi.speak("Здор+ово, бобры", SpeakerList.getValues().shuffled()[0],Emotion.values().toList().shuffled()[0])
}