package com.acdev.wallpaperwizard.utils

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.Log
import android.view.WindowManager
import dagger.hilt.android.qualifiers.ApplicationContext

fun setWallPaper(
    context: Context,
    wallpaperAs: Int,
    finalImageBitmap: Bitmap?
) {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val screenWidth = windowManager.defaultDisplay.width
    val screenHeight = windowManager.defaultDisplay.height
    val wallpaperManager = WallpaperManager.getInstance(context)
    wallpaperManager.suggestDesiredDimensions(screenWidth, screenHeight)
    val thread = Thread {
        try {
            val wallpaper = finalImageBitmap?.let {
                scaleAndCropBitmap(it, screenWidth, screenHeight)
            }
            when (wallpaperAs) {
                1 -> wallpaperManager.setBitmap(wallpaper)


                2 -> wallpaperManager.setBitmap(
                    wallpaper,
                    null,
                    true,
                    WallpaperManager.FLAG_LOCK
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    thread.start()
}

private fun scaleAndCropBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
    val sourceWidth = bitmap.width
    val sourceHeight = bitmap.height

    val widthRatio = targetWidth.toFloat() / sourceWidth.toFloat()
    val heightRatio = targetHeight.toFloat() / sourceHeight.toFloat()

    val scaleFactor = widthRatio.coerceAtLeast(heightRatio)

    val scaledWidth = (sourceWidth * scaleFactor).toInt()
    val scaledHeight = (sourceHeight * scaleFactor).toInt()

    val offsetX = (targetWidth - scaledWidth) / 2
    val offsetY = (targetHeight - scaledHeight) / 2

    val scaledBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(scaledBitmap)
    val scaleMatrix = RectF(
        offsetX.toFloat(),
        offsetY.toFloat(),
        (offsetX + scaledWidth).toFloat(),
        (offsetY + scaledHeight).toFloat()
    )
    canvas.drawBitmap(bitmap, null, scaleMatrix, null)

    return scaledBitmap
}

