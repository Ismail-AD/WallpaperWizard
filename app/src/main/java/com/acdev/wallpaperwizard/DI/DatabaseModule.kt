package com.acdev.wallpaperwizard.DI

import android.content.Context
import androidx.room.Room
import com.acdev.wallpaperwizard.data.local.roomDatabase
import com.acdev.wallpaperwizard.utils.Constants.UNSPLASH_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun roomDB(@ApplicationContext context: Context): roomDatabase {
        return Room.databaseBuilder(context, roomDatabase::class.java, UNSPLASH_DB)
            .fallbackToDestructiveMigration().build()
    }
}