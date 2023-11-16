package com.acdev.wallpaperwizard.ui.Screens.SearchResponseScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.acdev.wallpaperwizard.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchResViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var categorySelected = MutableStateFlow<String?>("")
    var searchScreenState by mutableStateOf(MainScreenUtils())

    var searchedImages = categorySelected.flatMapLatest { query ->
        if (query.isNullOrEmpty()) {
            repository.getImages().flowOn(Dispatchers.IO).cachedIn(viewModelScope)
        } else {
            repository.getSearchImages(query).flowOn(Dispatchers.IO).cachedIn(viewModelScope)
        }
    }

    fun recall(){
        searchedImages = categorySelected.flatMapLatest { query ->
            if (query.isNullOrEmpty()) {
                repository.getImages().flowOn(Dispatchers.IO).cachedIn(viewModelScope)
            } else {
                repository.getSearchImages(query).flowOn(Dispatchers.IO).cachedIn(viewModelScope)
            }
        }
    }

    fun eventHandler(searchResEventHandler: SearchResEventHandler) {
        when (searchResEventHandler) {
            is SearchResEventHandler.SearchedData -> {
                searchScreenState =
                    searchScreenState.copy(searchedText = searchResEventHandler.textToSearch)
            }

            SearchResEventHandler.NetworkChecking -> {
                viewModelScope.launch {
                    val status = isConnected()
                    searchScreenState = searchScreenState.copy(networkState = status)
                }
            }

            SearchResEventHandler.ClearSearchedData -> searchScreenState =
                searchScreenState.copy(searchedText = "")

            is SearchResEventHandler.BeginSearch -> {
                categorySelected.value = searchResEventHandler.text
            }
        }
    }


    private suspend fun isConnected(): Boolean {
        return withContext(Dispatchers.IO) {
            val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8")
            process.waitFor() == 0
        }
    }

    data class MainScreenUtils(
        val networkState: Boolean = true,
        val searchedText: String = "",
    )
}