package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isReady = false
    private var lastSpokenText: String = ""
    private var lastSpokenTime: Long = 0L
    private val cooldownMs = 3500L // 3.5s speech cooldown

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            isReady = true
        }
    }

    fun speakObject(objectName: String) {
        if (!isReady || objectName.isBlank()) return
        val now = System.currentTimeMillis()
        if (objectName == lastSpokenText && (now - lastSpokenTime) < cooldownMs) {
            return
        }

        lastSpokenText = objectName
        lastSpokenTime = now
        tts?.speak("$objectName detected", TextToSpeech.QUEUE_FLUSH, null, "ObjectLensTts")
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
        isReady = false
    }
}
