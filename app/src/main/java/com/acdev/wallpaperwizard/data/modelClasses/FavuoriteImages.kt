package com.acdev.wallpaperwizard.data.modelClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acdev.wallpaperwizard.utils.Constants.UNSPLASH_FAV_IMAGE_DB
import kotlinx.serialization.Serializable

@Entity(tableName = UNSPLASH_FAV_IMAGE_DB)
@Serializable
data class FavuoriteImages(
    val id: String,
    @PrimaryKey
    val full: String,
    val regular: String,
    val blur_hash:String
)
