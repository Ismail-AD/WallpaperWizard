package com.acdev.wallpaperwizard.utils

import android.content.res.Resources
import android.util.DisplayMetrics

// gives you the respective dimension of the screen in pixels based on the display metrics obtained from the system resources.
val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics

fun getHeight(): Int {
    return displayMetrics.heightPixels
}
fun getWidth(): Int {
   return displayMetrics.widthPixels
}