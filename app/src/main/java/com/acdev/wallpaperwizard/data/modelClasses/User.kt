package com.acdev.wallpaperwizard.data.modelClasses

import androidx.room.Embedded
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @Embedded("user_links_")
    val links: Links,
    val username: String
)