package com.acdev.wallpaperwizard.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.acdev.wallpaperwizard.ui.Screens.CategoryScreen.CSeventHandler
import com.acdev.wallpaperwizard.ui.Screens.CategoryScreen.CategoryScreenViewModel
import com.acdev.wallpaperwizard.ui.Screens.CategoryScreen.categoryScreen
import com.acdev.wallpaperwizard.ui.Screens.Favorites.FavoriteViewModel
import com.acdev.wallpaperwizard.ui.Screens.Favorites.favoriteScreen
import com.acdev.wallpaperwizard.ui.Screens.HomeScreen.HomeEventHandler
import com.acdev.wallpaperwizard.ui.Screens.HomeScreen.HomeScreen
import com.acdev.wallpaperwizard.ui.Screens.HomeScreen.HomeViewModel
import com.acdev.wallpaperwizard.ui.Screens.SearchResponseScreen.SearchResEventHandler
import com.acdev.wallpaperwizard.ui.Screens.SearchResponseScreen.SearchResViewModel
import com.acdev.wallpaperwizard.ui.Screens.SearchResponseScreen.SearchResponseScreen
import com.acdev.wallpaperwizard.ui.Screens.WallpaperView.WallViewModel
import com.acdev.wallpaperwizard.ui.Screens.WallpaperView.WallpaperView

@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val bottomItems = bottomNavItems()
    val backStackEntry = navController.currentBackStackEntryAsState()
    var shouldHideBottomBar by remember {
        mutableStateOf(true)
    }
    val screenWithNoBar = listOf(
        "${Routes.ImageInDetail.route}/{regularUrl}/{fullUrl}/{id}/{blur_hash}",
        "${Routes.CategoryScreen.route}/{categoryData}/{name}",
        "${Routes.SearchScreen.route}/{query}"
    )

    Scaffold(bottomBar = {
        AnimatedVisibility(
            visible = shouldHideBottomBar,
            enter = fadeIn(
                animationSpec = tween(
                    delayMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = fadeOut(animationSpec = tween(delayMillis = 500, easing = LinearOutSlowInEasing))
        ) {
            BottomNavBar(screenWithNoBar, navController, backStackEntry, bottomItems)
        }
    }) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(padding),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(220, delayMillis = 90)
                ) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(220, delayMillis = 90)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(90))
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(220, delayMillis = 90)
                ) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(220, delayMillis = 90)
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(90))
            }) {
            composable(Routes.Home.route) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                homeViewModel.homeEvent((HomeEventHandler.NetworkChecking))
                HomeScreen(
                    homeViewModel.homeScreenState,
                    homeViewModel::homeEvent,
                    homeViewModel.imagesList,
                    homeViewModel.wallpaperItems, navController,homeViewModel::recall
                )
            }
            composable(
                Routes.ImageInDetail.route + "/{regularUrl}/{fullUrl}/{id}/{blur_hash}"
            ) {
                val wallViewModel: WallViewModel = hiltViewModel()
                val idOfImage = it.arguments?.getString("id")
                val fullUrl = it.arguments?.getString("fullUrl")
                val blur_hash = it.arguments?.getString("blur_hash")
                it.arguments?.getString("regularUrl")?.let { regUrl ->
                    fullUrl?.let { fullUrl ->
                        idOfImage?.let { id ->
                            wallViewModel.getFavoritesByID(id = id)
                            val isFav by wallViewModel.favImage.collectAsStateWithLifecycle()
                            WallpaperView(
                                navController = navController,
                                wallViewModel.wallpaperScreenState,
                                wallViewModel::eventHandler,
                                regUrl,
                                fullUrl,
                                isFav,
                                wallViewModel::addFavorites,
                                wallViewModel::removeFromFavorites,
                                id, blur_hash
                            )
                        }
                    }
                }
            }
            composable(
                Routes.FavScreen.route
            ) {
                val favoriteViewModel: FavoriteViewModel = hiltViewModel()
                favoriteScreen(
                    favoriteViewModel.getAllFavoriteImages,
                    navController,
                    favoriteViewModel::clearAllFavorites
                )
            }
            composable(
                Routes.CategoryScreen.route + "/{categoryData}/{name}"
            ) {
                val categoryScreenViewModel: CategoryScreenViewModel = hiltViewModel()
                val title = it.arguments?.getString("name")
                it.arguments?.getString("categoryData")?.let {cat->
                    categoryScreenViewModel.homeEvent(CSeventHandler.CategoryData(cat))
                    title?.let { titleScr ->
                        categoryScreen(categoryScreenViewModel.imagesList, navController, titleScr,categoryScreenViewModel::recall,categoryScreenViewModel::homeEvent,categoryScreenViewModel.catState)
                    }
                }
            }
            composable(Routes.SearchScreen.route + "/{query}") {
                val searchResViewModel: SearchResViewModel = hiltViewModel()
                it.arguments?.getString("query")?.let { query ->
                    searchResViewModel.eventHandler(SearchResEventHandler.BeginSearch(query))
                    SearchResponseScreen(
                        navController,
                        searchResViewModel.searchedImages,
                        searchResViewModel.searchScreenState, searchResViewModel::eventHandler,searchResViewModel::recall
                    )
                }
            }
        }

    }
}

@Composable
fun BottomNavBar(
    screenWithNoBar: List<String>,
    navController: NavHostController,
    backStackEntry: State<NavBackStackEntry?>,
    bottomItems: List<BottomNavItem>
) {
    if (backStackEntry.value?.destination?.route !in screenWithNoBar) {
        NavigationBar(containerColor = Color.Transparent, modifier = Modifier.height(75.dp)) {
            bottomItems.forEach { item ->
                NavigationBarItem(
                    selected = backStackEntry.value?.destination?.route == item.route,
                    onClick = {
                        if (item.route != backStackEntry.value?.destination?.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "",
                            tint = if (backStackEntry.value?.destination?.route == item.route) {
                                MaterialTheme.colorScheme.surfaceTint
                            } else {
                                MaterialTheme.colorScheme.secondary
                            }
                        )
                    }, label = {
                        Text(
                            text = item.name,
                            color = if (backStackEntry.value?.destination?.route == item.route) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.secondary
                            },
                            fontWeight = if (backStackEntry.value?.destination?.route == item.route) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }

}

fun bottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem("Home", Routes.Home.route, Icons.Filled.Home),
        BottomNavItem("Favourites", Routes.FavScreen.route, Icons.Filled.FavoriteBorder)
    )
}