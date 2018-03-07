package com.epavlov.tts.speaker


data class Speaker(var name: String, var gender: Boolean)

object SpeakerList {
    private val values = ArrayList<Speaker>()


    init {
        values.add(Speaker("jane", false))
        values.add(Speaker("oksana", false))
        values.add(Speaker("alyss", false))
        values.add(Speaker("omazh", false))
        values.add(Speaker("zahar", true))
        values.add(Speaker("ermil", true))
    }

    fun getValues(): List<Speaker> {
        return values.toMutableList()
    }
}

enum class Emotion {
    GOOD, NEUTRAL, EVIL
}