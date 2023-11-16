package com.acdev.wallpaperwizard.data.modelClasses

import kotlinx.serialization.Serializable

@Serializable
data class Urls(
    val full: String,
    val regular: String,
    val raw: String,
    val small:String
)