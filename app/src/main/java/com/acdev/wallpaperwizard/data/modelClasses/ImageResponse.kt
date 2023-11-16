package com.acdev.wallpaperwizard.data.modelClasses

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acdev.wallpaperwizard.utils.Constants.UNSPLASH_IMAGE_DB
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = UNSPLASH_IMAGE_DB)
data class ImageResponse(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @Embedded //fields of the embedded object will be treated as if they are part of the containing entity's table when the database is created.
    val urls: Urls,
    val likes: Int,
    @Embedded
    val user: User,
    val height: Int,
    val blur_hash: String
)
