package com.acdev.wallpaperwizard.ui.Screens.HomeScreen

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MobileFriendly
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.acdev.wallpaperwizard.data.modelClasses.Category
import com.acdev.wallpaperwizard.data.modelClasses.ImageResponse
import com.acdev.wallpaperwizard.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    var homeScreenState by mutableStateOf(HomeScreenUtils())
    private var categorySelected = MutableStateFlow<String?>("popular")

    @OptIn(ExperimentalCoroutinesApi::class)
    var imagesList = categorySelected.flatMapLatest { query ->
        if (query.isNullOrEmpty()) {
            repository.getImages().flowOn(Dispatchers.IO).cachedIn(viewModelScope)
        } else {
            repository.getSearchImages(query).flowOn(Dispatchers.IO).cachedIn(viewModelScope)
        }
    }

    fun recall(){
        imagesList = categorySelected.flatMapLatest { query ->
            if (query.isNullOrEmpty()) {
                repository.getImages().flowOn(Dispatchers.IO).cachedIn(viewModelScope)
            } else {
                repository.getSearchImages(query).flowOn(Dispatchers.IO).cachedIn(viewModelScope)
            }
        }
    }


    val wallpaperItems =
        arrayListOf(
            Category("Space", "space", Icons.Filled.RocketLaunch),
            Category("Dark", "dark wallpapers", Icons.Filled.DarkMode),
            Category("4k Wallpapers", "4k wallpapers", Icons.Filled.HighQuality),
            Category("Mobile", "Android Wallpapers", Icons.Filled.MobileFriendly),
            Category("Nature", "Nature", Icons.Filled.Nature),
            Category("Digital Art", "digital art", Icons.Filled.Image),
            Category("Games", "games", Icons.Filled.Games),
        )
    var selectedIndex = mutableIntStateOf(0)

    fun homeEvent(eventHandler: HomeEventHandler) {
        when (eventHandler) {
            is HomeEventHandler.SearchBarOpen -> homeScreenState =
                homeScreenState.copy(isVisible = eventHandler.openSB)

            is HomeEventHandler.SearchedData -> homeScreenState =
                homeScreenState.copy(textToSearch = eventHandler.textToSearch)

            HomeEventHandler.ClearSearchedData -> homeScreenState =
                homeScreenState.copy(textToSearch = "")

            is HomeEventHandler.CategoryChanged -> {
                selectedIndex.value = eventHandler.selected
                categorySelected.value = eventHandler.newCat
            }

            HomeEventHandler.NetworkChecking -> {
                viewModelScope.launch {
                    val status = isConnected()
                    homeScreenState = homeScreenState.copy(networkState = status)
                }
            }
        }
    }

    private suspend fun isConnected(): Boolean {
        return withContext(Dispatchers.IO) {
            val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8")
            process.waitFor() == 0
        }
    }

    data class HomeScreenUtils(
        val textToSearch: String = "",
        val isVisible: Boolean = false,
        val networkState: Boolean = true
    )
}