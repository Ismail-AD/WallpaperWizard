package com.acdev.wallpaperwizard.ui.Screens.WallpaperView

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acdev.wallpaperwizard.data.local.roomDatabase
import com.acdev.wallpaperwizard.data.modelClasses.FavuoriteImages
import com.acdev.wallpaperwizard.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WallViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var wallpaperScreenState by mutableStateOf(WallpaperScreenUtils())

    private val _favImage = MutableStateFlow<FavuoriteImages?>(null)
    val favImage: StateFlow<FavuoriteImages?> = _favImage

    fun eventHandler(wallEventHandler: WallEventHandler) {
        when (wallEventHandler) {
            WallEventHandler.CheckNetwork -> {
                viewModelScope.launch {
                    val status = isConnected()
                    wallpaperScreenState = wallpaperScreenState.copy(networkState = status)
                }
            }
        }
    }

    fun addFavorites(favoriteImages: FavuoriteImages) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFav = repository.getFavByID(favoriteImages.id)
            if (isFav == null) {
                repository.addToFavorites(favoriteImages)
            } else {
                repository.removeFromFavorite(favoriteImages)
            }
        }
    }

    fun getFavoritesByID(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFavByID(id)
            withContext(Dispatchers.Main) {
                _favImage.value = result
            }
        }
    }

    fun removeFromFavorites(favoriteImages: FavuoriteImages) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFromFavorite(favoriteImages)
        }
    }

    private suspend fun isConnected(): Boolean {
        return withContext(Dispatchers.IO) {
            val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8")
            process.waitFor() == 0
        }
    }

    data class WallpaperScreenUtils(
        val networkState: Boolean = true,
        val searchedText: String = "",
    )
}