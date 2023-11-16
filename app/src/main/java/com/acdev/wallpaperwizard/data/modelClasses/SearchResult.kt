package com.acdev.wallpaperwizard.data.modelClasses

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val results: List<UnsplashImage>,
    val total: Int,
    val total_pages: Int
)