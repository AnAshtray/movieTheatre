package com.ash.movietheatre.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColors = lightColorScheme(
    primary = PinkPrimary,
    onPrimary = Color.White,
    secondary = GreenSecondary,
    onSecondary = Color.Black,
    background = LightBackground,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = PinkPrimary,
    onPrimary = Color.White,
    secondary = GreenSecondary,
    onSecondary = Color.Black,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkBackground,
    onSurface = Color.White
)

@Composable
fun MovieTheaterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
