package com.acdev.wallpaperwizard.ui.Screens.SearchResponseScreen


sealed interface SearchResEventHandler {
    object NetworkChecking : SearchResEventHandler
    data class BeginSearch(val text: String) : SearchResEventHandler
    object ClearSearchedData : SearchResEventHandler
    data class SearchedData(val textToSearch: String) : SearchResEventHandler
}