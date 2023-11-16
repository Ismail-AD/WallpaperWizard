package com.acdev.wallpaperwizard.ui.Screens.WallpaperView

sealed interface WallEventHandler {
    object CheckNetwork : WallEventHandler
}