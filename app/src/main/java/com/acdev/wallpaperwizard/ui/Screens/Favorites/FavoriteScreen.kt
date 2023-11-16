package com.acdev.wallpaperwizard.ui.Screens.Favorites


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.acdev.wallpaperwizard.data.modelClasses.FavuoriteImages
import com.acdev.wallpaperwizard.navigation.Routes
import com.acdev.wallpaperwizard.ui.Screens.Componenets.TopBar
import com.acdev.wallpaperwizard.ui.Screens.Componenets.UnsplashImageItem
import com.acdev.wallpaperwizard.ui.Screens.WallpaperView.showToast
import kotlinx.coroutines.flow.Flow
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun favoriteScreen(
    favImagesList: Flow<List<FavuoriteImages>>,
    navController: NavHostController,
    clearAll: () -> Unit
) {

    val context = LocalContext.current
    val favList by favImagesList.collectAsStateWithLifecycle(initialValue = emptyList())


    if (favList.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No favourite images yet !",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
        }
    } else {
        Scaffold(topBar = {
            TopBar(
                title = "Favorites",
                trailingIconFunction = {
                    showToast(context = context, "Deleting all Favourite Images...")
                    clearAll()
                },
                tailIconID = Icons.Default.Delete
            )

        }, modifier = Modifier.background(MaterialTheme.colorScheme.background)) { paddingValues ->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {
                LazyVerticalGrid(
                    state = rememberLazyGridState(),
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    items(favList.size, key = { index ->
                        // Provide a unique key for each item based on index
                        favList[index].id
                    }) { index ->
                        favList[index].let {
                            UnsplashImageItem(
                                unsplashImageUrl = it.regular,
                                blurHash = it.blur_hash,
                                onClick = {
                                    val regularEncodedUrl = URLEncoder.encode(
                                        it.regular,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    val fullEncodedUrl = URLEncoder.encode(
                                        it.full,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    val Blur_Hash = URLEncoder.encode(
                                        it.blur_hash,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    navController.navigate(Routes.ImageInDetail.route + "/${regularEncodedUrl}/${fullEncodedUrl}/${it.id}/$Blur_Hash")
                                })
                        }
                    }

                }

            }

        }
    }
}




