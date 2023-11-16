package com.acdev.wallpaperwizard.data.modelClasses

data class Result(
    val height: Int,
    val id: String,
    val likes: Int,
    val links: Links,
    val urls: Urls,
    val user: User,
    val width: Int
)