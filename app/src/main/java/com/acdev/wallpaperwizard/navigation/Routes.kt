package com.acdev.wallpaperwizard.navigation

sealed class Routes(val route:String){
    object Home:Routes("Home_Screen")
    object FavScreen:Routes("Fav_Screen")
    object CategoryScreen:Routes("Category_Screen")
    object ImageInDetail:Routes("ImageView_Screen")
    object SearchScreen:Routes("search_Screen")
}
