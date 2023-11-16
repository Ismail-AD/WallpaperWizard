package com.acdev.wallpaperwizard.ui.Screens.CategoryScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.acdev.wallpaperwizard.data.repository.Repository
import com.acdev.wallpaperwizard.ui.Screens.HomeScreen.HomeEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryScreenViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private var categorySelected = MutableStateFlow<String?>("")
    var catState by mutableStateOf(CatScreenUtils())

    @OptIn(ExperimentalCoroutinesApi::class)
    var imagesList = categorySelected.flatMapLatest { query ->
        if (query.isNullOrEmpty()) {
            repository.getImages().flowOn(Dispatchers.IO).cachedIn(viewModelScope)
        } else {
            repository.getSearchImages(query).flowOn(Dispatchers.IO).cachedIn(viewModelScope)
        }
    }

    fun recall() {
        imagesList =
            categorySelected.flatMapLatest { query ->
                if (query.isNullOrEmpty()) {
                    repository.getImages().flowOn(Dispatchers.IO).cachedIn(viewModelScope)
                } else {
                    repository.getSearchImages(query).flowOn(Dispatchers.IO)
                        .cachedIn(viewModelScope)
                }
            }
    }

    fun homeEvent(eventHandler: CSeventHandler) {
        when (eventHandler) {
            is CSeventHandler.CategoryData -> {
                categorySelected.value = eventHandler.text
            }

            CSeventHandler.NetworkChecking -> {
                viewModelScope.launch {
                    val status = isConnected()
                    catState = catState.copy(networkState = status)
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

    data class CatScreenUtils(
        val networkState: Boolean = true
    )
}