package com.acdev.wallpaperwizard.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.acdev.wallpaperwizard.data.local.roomDatabase
import com.acdev.wallpaperwizard.data.modelClasses.FavuoriteImages
import com.acdev.wallpaperwizard.data.modelClasses.ImageResponse
import com.acdev.wallpaperwizard.data.remote.Service
import com.acdev.wallpaperwizard.pagging.SearchViaPagingSource
import com.acdev.wallpaperwizard.pagging.remoteMediator
import com.acdev.wallpaperwizard.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class Repository @Inject constructor(
    private val serviceApi: Service,
    private val roomDatabase: roomDatabase,
) {

    fun getImages(): Flow<PagingData<ImageResponse>> {
        return Pager(
            config = PagingConfig(Constants.ITEMS_PER_PAGE),
            remoteMediator = remoteMediator(serviceApi, roomDatabase), pagingSourceFactory = {
                roomDatabase.unsplashImageDao().getImages()
            }
        ).flow
    }

    fun getAllFavImages(): Flow<List<FavuoriteImages>> {
        return roomDatabase.unsplashImageDao().getFavImages()
    }

    suspend fun addToFavorites(favoriteImages: FavuoriteImages) {
        roomDatabase.unsplashImageDao().insertFavImages(favoriteImages)
    }

    suspend fun getFavByID(id: String): FavuoriteImages? {
        return roomDatabase.unsplashImageDao().getFavouriteUrl(id)
    }

    suspend fun deleteAllFavImages() {
        roomDatabase.unsplashImageDao().deleteAllFavImages()
    }

    suspend fun removeFromFavorite(favoriteImages: FavuoriteImages) {
        roomDatabase.unsplashImageDao().deleteFavouriteUrl(favoriteImages)
        Log.d("CHECKME", "remove image")
    }


    fun getSearchImages(query: String): Flow<PagingData<ImageResponse>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.ITEMS_PER_PAGE),
            pagingSourceFactory = {
                SearchViaPagingSource(serviceApi, query)
            }
        ).flow
    }
}