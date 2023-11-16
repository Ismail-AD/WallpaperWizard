package com.acdev.wallpaperwizard.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun saveMediaToStorage(bitmap: Bitmap, context: Context) {
    val filename = "${System.currentTimeMillis()}.jpg"

    var outputStream: OutputStream? = null
    if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/WallpaperWizard")
        }
        val imageUri =
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        imageUri?.let {
            outputStream = context.contentResolver.openOutputStream(it)
        }
    } else {
        val imgDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + File.separator + "WallpaperWizard"
        val imageFile = File(imgDirectory,filename)
        outputStream = FileOutputStream(imageFile)
    }
    outputStream?.use {
        Log.d("CHECKIT","image going to be write on ")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }

}