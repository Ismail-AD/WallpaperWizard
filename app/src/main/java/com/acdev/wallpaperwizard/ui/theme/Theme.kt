package com.acdev.wallpaperwizard.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB9A6B5),
    background = Color(0xFF161B2E),
    onBackground = Color.White,
    surface = Color(0xFF161B2E),
    tertiary = Color(0xff393F44),
    onSurface = Color.White,
    inverseSurface = Color(0xFF242B44),
    inverseOnSurface = Color(0xFFADACAD),
    secondary = SecondaryDark,
    surfaceTint = Color(0xFF1a1c1e)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFB9A6B5),
    surfaceTint = Color(0xFF6650a4),
    background = Color(0xFFFFFFFF),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    inverseSurface = Color(0xFF242B44),
    inverseOnSurface = Color(0xff625F62),
    secondary = SecondaryLight,
    tertiary = Color.White

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun WallpaperWizardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> {
            DarkColorScheme
        }
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !darkTheme // not darkTheme makes the status bar icons visible
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}