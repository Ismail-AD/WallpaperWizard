package com.acdev.wallpaperwizard.ui.Screens.HomeScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.acdev.wallpaperwizard.data.modelClasses.Category
import com.acdev.wallpaperwizard.data.modelClasses.ImageResponse
import com.acdev.wallpaperwizard.navigation.Routes
import com.acdev.wallpaperwizard.ui.Screens.Componenets.CategoryItem
import com.acdev.wallpaperwizard.ui.Screens.Componenets.UnsplashImageItem
import kotlinx.coroutines.flow.Flow
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeScreenState: HomeViewModel.HomeScreenUtils,
    homeEventHandler: (HomeEventHandler) -> Unit,
    imagesList: Flow<PagingData<ImageResponse>>,
    wallpaperItems: ArrayList<Category>,
    navController: NavHostController,
    recall: () -> Unit,
) {
    val context = LocalContext.current
    val images = imagesList.collectAsLazyPagingItems()
    val shouldHideSearchBar by remember {
        mutableStateOf(true)
    }
    var isInternet by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = Unit) {
        homeEventHandler.invoke(HomeEventHandler.NetworkChecking)
    }
    LaunchedEffect(key1 = homeScreenState.networkState) {
        isInternet = homeScreenState.networkState
    }
    if (isInternet) {
        Scaffold(topBar = {
            AnimatedVisibility(
                visible = shouldHideSearchBar,
                enter = fadeIn(
                    animationSpec = tween(
                        delayMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        delayMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                )
            ) {
                SearchBar(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                    query = homeScreenState.textToSearch,
                    onQueryChange = { data ->
                        homeEventHandler.invoke(
                            HomeEventHandler.SearchedData(
                                data
                            )
                        )

                    },
                    onSearch = {
                        if (homeScreenState.textToSearch.trim().isNotEmpty()) {
                            navController.navigate(Routes.SearchScreen.route + "/${homeScreenState.textToSearch}") {
                                launchSingleTop = true
                                popUpTo(Routes.Home.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Input something to search !",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    },
                    active = false,
                    onActiveChange = {},
                    placeholder = {
                        Text(
                            text = "Search Wallpaper",
                            fontSize = 16.sp, color = Color(0xff625F62)
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (homeScreenState.textToSearch.trim().isNotEmpty())
                            IconButton(onClick = {
                                homeEventHandler.invoke(HomeEventHandler.ClearSearchedData)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close Icon",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                    }) {

                }
            }
        }, modifier = Modifier.background(MaterialTheme.colorScheme.background)) { paddingValues ->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Category",
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 17.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )

                Row(
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                        .horizontalScroll(
                            rememberScrollState()
                        )
                ) {
                    wallpaperItems.forEach { category ->
                        CategoryItem(
                            icon = category.imageVector,
                            title = category.name, onClick = {
                                navController.navigate(Routes.CategoryScreen.route + "/${category.query}/${category.name}") {
                                    launchSingleTop = true
                                    popUpTo(Routes.Home.route) {
                                        inclusive = false
                                    }
                                }
                            })
                    }
                }
                if (images.loadState.refresh is LoadState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.inverseSurface)
                    }
                }
                Text(
                    text = "Popular",
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 17.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
                LazyVerticalGrid(
                    state = rememberLazyGridState(),
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    items(images.itemCount, key = { index ->
                        // Provide a unique key for each item based on index
                        images[index]?.id.toString()
                    }) { index ->
                        images[index]?.let {
                            UnsplashImageItem(
                                unsplashImageUrl = it.urls.small,
                                blurHash = it.blur_hash,
                                onClick = {
                                    val regularEncodedUrl = URLEncoder.encode(
                                        it.urls.regular,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    val fullEncodedUrl = URLEncoder.encode(
                                        it.urls.full,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    val Blur_Hash = URLEncoder.encode(
                                        it.blur_hash,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    navController.navigate(Routes.ImageInDetail.route + "/${regularEncodedUrl}/${fullEncodedUrl}/${it.id}/${Blur_Hash}")
                                })
                        }
                    }

                }

            }

        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "No Internet Connection", color = MaterialTheme.colorScheme.inverseSurface)
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    homeEventHandler.invoke(HomeEventHandler.NetworkChecking)
                    recall()
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inverseSurface),
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Text(text = "Retry", color = Color.White, fontSize = 16.sp)
            }
        }
    }


}





