package com.acdev.wallpaperwizard.data.modelClasses

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acdev.wallpaperwizard.utils.Constants.UNSPLASH_IMAGE_DB
import kotlinx.serialization.Serializable

@Serializable
//@Entity(tableName = UNSPLASH_IMAGE_DB)
data class UnsplashImage(
//    @PrimaryKey(autoGenerate = false)
    val id: String,
    val height: Int,
    val likes: Int,
//    @Embedded //
//    val links: Links,
//    @Embedded
//    val urls: Urls,
//    @Embedded
//    val user: User,
    val width: Int,
)