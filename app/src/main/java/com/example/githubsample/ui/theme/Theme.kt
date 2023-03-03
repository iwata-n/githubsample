package com.example.githubsample.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.githubsample.FeatureFlag
import com.example.githubsample.ReleaseFeatureFlag

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

val LocalFeatureFlag = staticCompositionLocalOf<FeatureFlag> {
    ReleaseFeatureFlag()
}

@Composable
fun GitHubSampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    featureFlag: FeatureFlag = ReleaseFeatureFlag(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {
        CompositionLocalProvider(
            LocalFeatureFlag provides featureFlag
        ) {
            content()
        }
    }
}
