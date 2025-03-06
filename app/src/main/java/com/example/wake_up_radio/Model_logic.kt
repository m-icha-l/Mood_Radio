package com.example.wake_up_radio
import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.audio.AudioAttributes

class Model_logic(context: Context) : ViewModel()
{
    var isPlaying by mutableStateOf(false)
    val kaakko_URL = "https://wr2.downtime.fi/kaakko.aac"
    // Initialize ExoPlayer
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(com.google.android.exoplayer2.C.USAGE_MEDIA)
                .setContentType(com.google.android.exoplayer2.C.AUDIO_CONTENT_TYPE_MUSIC)
                .build(),
            true
        )
        val mediaItem = MediaItem.fromUri(kaakko_URL)
        setMediaItem(mediaItem)
        prepare()
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