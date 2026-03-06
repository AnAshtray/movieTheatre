package com.ash.movietheatre.ui.theme

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayerScreen(
    videoUri: Uri,
    onBack: () -> Unit,
    viewModel: VideoPlayerViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(videoUri) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        viewModel.preparePlayer(videoUri)
    }

    DisposableEffect(Unit) {
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    Surface(color = Color.Black) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = viewModel.exoPlayer
                            useController = false
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x80000000))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { viewModel.seekBack() }) {
                        Text("-5s")
                    }

                    Button(onClick = { viewModel.togglePlayPause() }) {
                        Text(if (viewModel.isPlaying) "Pause" else "Play")
                    }

                    Button(onClick = { viewModel.seekForward() }) {
                        Text("+5s")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Volume",
                    color = Color.White
                )
                Slider(
                    value = viewModel.volume,
                    onValueChange = { viewModel.updateVolume(it) },
                    valueRange = 0f..1f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }
    }
}
