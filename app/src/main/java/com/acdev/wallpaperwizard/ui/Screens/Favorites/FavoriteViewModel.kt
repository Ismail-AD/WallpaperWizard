package com.acdev.wallpaperwizard.ui.Screens.Favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acdev.wallpaperwizard.data.modelClasses.FavuoriteImages
import com.acdev.wallpaperwizard.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    val getAllFavoriteImages = repository.getAllFavImages()

    fun clearAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllFavImages()
        }
    }
}