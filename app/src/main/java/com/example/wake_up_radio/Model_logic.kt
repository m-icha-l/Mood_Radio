package com.example.wake_up_radio

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter

data class RadioData(
    val radio_links: MutableList<String>,
    val radio_names: MutableList<String>
)

class Model_logic(context: Context) : ViewModel() {

    var isPlaying by mutableStateOf(false)
    var radio_URL by mutableStateOf("https://wr2.downtime.fi/kaakko.aac")
    private val jsonFile = File(context.filesDir, "new_radios.json")
    private val gson = Gson()

    // Initialize ExoPlayer
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(com.google.android.exoplayer2.C.USAGE_MEDIA)
                .setContentType(com.google.android.exoplayer2.C.AUDIO_CONTENT_TYPE_MUSIC)
                .build(),
            true
        )
        setMediaItem(MediaItem.fromUri(radio_URL))
        prepare()
    }

    private fun loadRadioData(): RadioData {
        Log.d("works","hello:)")
        return if (jsonFile.exists()) {

            FileReader(jsonFile).use { reader ->
                gson.fromJson(reader, RadioData::class.java) ?: RadioData(mutableListOf(), mutableListOf())
            }
        } else {
            Log.d("works","hello:)_2")
            RadioData(mutableListOf(), mutableListOf())
        }
    }

    private fun saveRadioData(radioData: RadioData) {
        FileWriter(jsonFile).use { writer ->
            gson.toJson(radioData, writer)
        }
    }

    fun addRadioStation(newLink: String, newName: String) {
        val radioData = loadRadioData()
        if (!radioData.radio_links.contains(newLink)) {
            radioData.radio_links.add(newLink)
            radioData.radio_names.add(newName)
            saveRadioData(radioData)
        }
    }

    fun get_radio_names(): List<String>{
        val radioData = loadRadioData()
        return radioData.radio_names
    }
    fun get_radio_links(): List<String>{
        val radioData = loadRadioData()
        return radioData.radio_links
    }

    fun change_radio(choosen_radio: String) {
        val wasPlaying = isPlaying
        pause_play()
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        radio_URL = choosen_radio
        exoPlayer.setMediaItem(MediaItem.fromUri(radio_URL))
        exoPlayer.prepare()
        if (wasPlaying) {
            exoPlayer.playWhenReady = true
            isPlaying = true
        }
    }

    fun pause_play() {
        if (isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        }
        isPlaying = !isPlaying
    }
}
