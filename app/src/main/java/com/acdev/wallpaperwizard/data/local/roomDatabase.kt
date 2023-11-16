package com.acdev.wallpaperwizard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.acdev.wallpaperwizard.data.local.dao.remoteKeysDao
import com.acdev.wallpaperwizard.data.local.dao.unsplashImageDao
import com.acdev.wallpaperwizard.data.modelClasses.FavuoriteImages
import com.acdev.wallpaperwizard.data.modelClasses.ImageResponse
import com.acdev.wallpaperwizard.data.modelClasses.UnsplashRemoteKeys

@Database(entities = [ImageResponse::class,UnsplashRemoteKeys::class,FavuoriteImages::class], version = 8)
abstract class roomDatabase : RoomDatabase() {
    abstract fun unsplashImageDao(): unsplashImageDao
    abstract fun unsplashKeyDao(): remoteKeysDao
}