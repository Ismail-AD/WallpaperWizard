package com.acdev.wallpaperwizard.data.modelClasses

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val results: List<ImageResponse>,
    val total: Int,
    val total_pages: Int
)