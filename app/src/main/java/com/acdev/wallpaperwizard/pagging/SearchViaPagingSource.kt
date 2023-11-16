package com.acdev.wallpaperwizard.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.acdev.wallpaperwizard.data.modelClasses.ImageResponse
import com.acdev.wallpaperwizard.data.remote.Service
import com.acdev.wallpaperwizard.utils.Constants.ITEMS_PER_PAGE

class SearchViaPagingSource(private val service: Service, val query: String) :
    PagingSource<Int, ImageResponse>() {

    override fun getRefreshKey(state: PagingState<Int, ImageResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResponse> {
        return try {
            val nextPage = params.key ?: 1
            val response =
                service.getSearchedImages(query = query, page = nextPage, perPage = ITEMS_PER_PAGE)
            if (response.results != null && response.results.isNotEmpty()) {
                LoadResult.Page(
                    data = response.results,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (response.results.isEmpty()) null else nextPage + 1
                )
            } else {
                LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}