package com.acdev.wallpaperwizard.data.remote

import com.acdev.wallpaperwizard.BuildConfig
import com.acdev.wallpaperwizard.data.modelClasses.ImageResponse
import com.acdev.wallpaperwizard.data.modelClasses.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Service {
    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/photos")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<ImageResponse>

    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/search/photos")
    suspend fun getSearchedImages(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchResponse
}