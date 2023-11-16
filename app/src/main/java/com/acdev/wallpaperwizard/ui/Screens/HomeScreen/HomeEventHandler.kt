package com.acdev.wallpaperwizard.ui.Screens.HomeScreen

sealed interface HomeEventHandler{
        data class SearchBarOpen(val openSB: Boolean) : HomeEventHandler
        data class SearchedData(val textToSearch: String) : HomeEventHandler
        data class CategoryChanged(val selected: Int,val newCat:String) : HomeEventHandler
        object NetworkChecking: HomeEventHandler
        object ClearSearchedData : HomeEventHandler
}