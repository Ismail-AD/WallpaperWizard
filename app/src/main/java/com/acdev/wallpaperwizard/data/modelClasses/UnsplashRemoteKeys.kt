package com.acdev.wallpaperwizard.data.modelClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acdev.wallpaperwizard.utils.Constants.REMOTE_KEYS_DB

@Entity(tableName = REMOTE_KEYS_DB)
data class UnsplashRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val nextPage: Int?
)
