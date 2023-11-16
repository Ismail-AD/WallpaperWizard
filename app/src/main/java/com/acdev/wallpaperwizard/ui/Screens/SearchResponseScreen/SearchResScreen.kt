package com.acdev.wallpaperwizard.ui.Screens.SearchResponseScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.acdev.wallpaperwizard.data.modelClasses.ImageResponse
import com.acdev.wallpaperwizard.navigation.Routes
import com.acdev.wallpaperwizard.ui.Screens.Componenets.TopBar
import com.acdev.wallpaperwizard.ui.Screens.Componenets.UnsplashImageItem
import kotlinx.coroutines.flow.Flow
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun SearchResponseScreen(
    navController: NavHostController,
    searchedImages: Flow<PagingData<ImageResponse>>,
    searchScreenState: SearchResViewModel.MainScreenUtils,
    eventHandler: (SearchResEventHandler) -> Unit,
    recall: () -> Unit,
) {
    val images = searchedImages.collectAsLazyPagingItems()

    var isInternet by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = Unit) {
        eventHandler.invoke(SearchResEventHandler.NetworkChecking)
    }
    LaunchedEffect(key1 = searchScreenState.networkState) {
        isInternet = searchScreenState.networkState
    }

    if (isInternet) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center
        ) {
            TopBar(headerIconID = Icons.Filled.ArrowBack, navigationIconFunction = {
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.SearchScreen.route)
                    launchSingleTop = true
                }
            })
            if (images.loadState.refresh is LoadState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.inverseSurface)
                }
            }
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
                            unsplashImageUrl = it.urls.regular,
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
                                navController.navigate(Routes.ImageInDetail.route + "/${regularEncodedUrl}/${fullEncodedUrl}/${it.id}/${Blur_Hash}") {
                                    launchSingleTop = true
                                    popUpTo(Routes.Home.route) {
                                        inclusive = true
                                    }
                                }
                            })
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
                    eventHandler.invoke(SearchResEventHandler.NetworkChecking)
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