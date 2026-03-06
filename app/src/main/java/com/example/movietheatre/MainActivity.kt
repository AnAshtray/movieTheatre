package com.ash.movietheatre

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ash.movietheatre.ui.theme.MovieTheaterTheme
import com.ash.movietheatre.ui.theme.VideoPlayerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieTheaterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MovieNavHost()
                }
            }
        }
    }
}

private const val ROUTE_PICKER = "picker"
private const val ROUTE_PLAYER = "player"

@Composable
fun MovieNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTE_PICKER
    ) {
        composable(ROUTE_PICKER) {
            VideoPickerScreen(
                onVideoPicked = { uri ->
                    val encoded = Uri.encode(uri.toString())
                    navController.navigate("$ROUTE_PLAYER?uri=$encoded")
                }
            )
        }

        composable(
            route = "$ROUTE_PLAYER?uri={uri}",
            arguments = listOf(
                navArgument("uri") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("uri") ?: return@composable
            VideoPlayerScreen(
                videoUri = Uri.parse(uriString),
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun VideoPickerScreen(
    onVideoPicked: (Uri) -> Unit
) {
    val pickerLauncher =
        androidx.activity.compose.rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) onVideoPicked(uri)
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                pickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.VideoOnly
                    )
                )
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Select a video")
        }
    }
}