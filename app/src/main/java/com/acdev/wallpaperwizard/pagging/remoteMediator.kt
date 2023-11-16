package com.acdev.wallpaperwizard.pagging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.acdev.wallpaperwizard.data.local.roomDatabase
import com.acdev.wallpaperwizard.data.modelClasses.ImageResponse
import com.acdev.wallpaperwizard.data.modelClasses.UnsplashRemoteKeys
import com.acdev.wallpaperwizard.data.remote.Service
import com.acdev.wallpaperwizard.utils.Constants.ITEMS_PER_PAGE
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class remoteMediator @Inject constructor(
    private val serviceApi: Service,
    private val roomDatabase: roomDatabase,
) :
    RemoteMediator<Int, ImageResponse>() {
    private val unsplashImageDao = roomDatabase.unsplashImageDao()
    private val unsplashKeyDao = roomDatabase.unsplashKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageResponse>,
    ): MediatorResult {
        return try {
            val pageToLoad = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = lastItemRemoteKey(state)
                    val nextPage = remoteKey?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKey == null
                    )
                    nextPage
                }
            }
            val networkResponse = serviceApi.getImages(pageToLoad, ITEMS_PER_PAGE)
            val endOfPagination = networkResponse.isEmpty()

            roomDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    unsplashImageDao.deleteImages()
                    unsplashKeyDao.deleteAllKeys()
                }
                val upcomingPage = if (endOfPagination) null else pageToLoad + 1

                val listOfKeys = networkResponse.map { image ->
                    UnsplashRemoteKeys(id = image.id, upcomingPage)
                }
                unsplashKeyDao.insertKey(listOfKeys)
                unsplashImageDao.insertImages(networkResponse)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ImageResponse>
    ): UnsplashRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                unsplashKeyDao.getRemoteKey(id = id)
            }
        }
    }

    private fun lastItemRemoteKey(state: PagingState<Int, ImageResponse>): UnsplashRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { image ->
            unsplashKeyDao.getRemoteKey(image.id)
        }
    }
}