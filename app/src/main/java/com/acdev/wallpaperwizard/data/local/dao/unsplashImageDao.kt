package com.acdev.wallpaperwizard.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acdev.wallpaperwizard.data.modelClasses.FavuoriteImages
import com.acdev.wallpaperwizard.data.modelClasses.ImageResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface unsplashImageDao {
    @Query("SELECT * FROM _my_image_db")
    fun getImages(): PagingSource<Int, ImageResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageResponse>)

    @Query("DELETE FROM _my_image_db")
    suspend fun deleteImages()

    @Query("SELECT * FROM fav_image_db")
    fun getFavImages():Flow<List<FavuoriteImages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavImages(favoriteImages: FavuoriteImages)

    @Delete
    suspend fun deleteFavouriteUrl(favoriteImages: FavuoriteImages)

    @Query("DELETE FROM fav_image_db")
    suspend fun deleteAllFavImages()

    @Query("SELECT * FROM fav_image_db WHERE id = :id")
    suspend fun getFavouriteUrl(id: String): FavuoriteImages?
}