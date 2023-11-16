package com.acdev.wallpaperwizard.ui.Screens.CategoryScreen


sealed interface CSeventHandler{
    data class CategoryData(val text: String) : CSeventHandler
    object NetworkChecking: CSeventHandler
}