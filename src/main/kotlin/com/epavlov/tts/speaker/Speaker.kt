package com.epavlov.tts.speaker

interface SpeakerI{
    fun speak(text:String, name:String)
    fun getNames():List<String>
}
