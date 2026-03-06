package com.ash.movietheatre.ui.theme

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(application).build()

    var isPlaying by mutableStateOf(true)
        private set

    var volume by mutableStateOf(exoPlayer.volume)
        private set

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlayingNow: Boolean) {
            isPlaying = isPlayingNow
        }
    }

    init {
        exoPlayer.addListener(playerListener)
    }

    fun preparePlayer(videoUri: Uri) {
        if (exoPlayer.mediaItemCount == 0) {
            exoPlayer.setMediaItem(MediaItem.fromUri(videoUri))
            exoPlayer.playWhenReady = true
            exoPlayer.prepare()
        }
    }

    fun togglePlayPause() {
        if (isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
    }

    fun seekBack() {
        val current = exoPlayer.currentPosition
        exoPlayer.seekTo((current - 5_000).coerceAtLeast(0))
    }

    fun seekForward() {
        val duration = exoPlayer.duration.takeIf { it > 0 } ?: Long.MAX_VALUE
        val current = exoPlayer.currentPosition
        exoPlayer.seekTo((current + 5_000).coerceAtMost(duration))
    }

    fun updateVolume(newVolume: Float) {
        volume = newVolume
        exoPlayer.volume = newVolume
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }
}
