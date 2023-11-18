package com.acdev.wallpaperwizard.ui.Screens.WallpaperView

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.acdev.wallpaperwizard.data.modelClasses.FavuoriteImages
import com.acdev.wallpaperwizard.ui.Screens.Componenets.TopBar
import com.acdev.wallpaperwizard.utils.BlurHashDecoder
import com.acdev.wallpaperwizard.utils.getImageFromURL
import com.acdev.wallpaperwizard.utils.saveMediaToStorage
import com.acdev.wallpaperwizard.utils.setWallPaper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperView(
    navController: NavHostController,
    wallpaperScreenState: WallViewModel.WallpaperScreenUtils,
    eventHandler: (WallEventHandler) -> Unit,
    regularUrl: String,
    fullUrl: String,
    isFav: FavuoriteImages?,
    add: (FavuoriteImages) -> Unit,
    remove: (FavuoriteImages) -> Unit,
    id: String,
    bh: String?
) {

    val context = LocalContext.current
    var finalImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageDecodingStarted by remember { mutableStateOf(true) }
    var showProgress by remember { mutableStateOf(false) }
    var downloadIt by remember { mutableStateOf(false) }
    var setWall by remember { mutableStateOf(false) }
    var setLock by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isFavorite by remember {
        mutableStateOf(isFav != null)
    }
    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = isFav) {
        isFavorite = (isFav != null)
    }
    LaunchedEffect(key1 = imageDecodingStarted, key2 = setWall, key3 = setLock) {
        if (setWall && !imageDecodingStarted) {
            setWallpaperWithToast(context, finalImageBitmap, 1, scope)
        }
        if (setLock && !imageDecodingStarted) {
            setWallpaperWithToast(context, finalImageBitmap, 2, scope)
        }
    }
    LaunchedEffect(key1 = downloadIt, key2 = imageDecodingStarted) {
        if (downloadIt && !imageDecodingStarted) {
            finalImageBitmap?.let {
                saveMediaToStorage(it, context)
                showToast = true
            }
        }
    }
    LaunchedEffect(key1 = Unit,key2 = showToast) {
        finalImageBitmap = getImageFromURL(fullUrl)
        showProgress = false
        imageDecodingStarted = false
        if (showToast) {
            showToast(context = context,"Saved to gallery!")
        }
    }
    if (showProgress) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Dialog(onDismissRequest = { /*TODO*/ }) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (wallpaperScreenState.networkState) {
            TopBar(
                navigationIconFunction = {
                    navController.popBackStack()
                },
                headerIconID = Icons.Default.ArrowBack
            )
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                val blurHashDrawable = BlurHashDecoder.blurHashBitmap(context.resources, bh)
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context).data(regularUrl).error(blurHashDrawable)
                        .placeholder(blurHashDrawable)
                        .crossfade(500).build(),
                    contentDescription = null, contentScale = ContentScale.Crop
                ) {
                    SubcomposeAsyncImageContent(modifier = Modifier.fillMaxSize())
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                singleButton(icon = Icons.Filled.Download, navigationIconFunction = {
                    if (!imageDecodingStarted) {
                        finalImageBitmap?.let {
                            saveMediaToStorage(it, context)
                            showToast = true
                        }
                    } else {
                        showProgress = true
                        downloadIt = true
                    }
                })
                Spacer(modifier = Modifier.width(20.dp))
                Card(
                    modifier = Modifier
                        .width(170.dp)
                        .shadow( // Add shadow to the Card
                            elevation = 4.dp, // Adjust the elevation value as needed
                            shape = CircleShape
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .clickable { showBottomSheet = true }
                            .fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SET AS",
                            modifier = Modifier.padding(vertical = 13.dp, horizontal = 10.dp),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Cursive
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                singleButton(
                    icon = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                ) {
                    bh?.let {
                        isFavorite = if (isFavorite) {
                            remove(
                                FavuoriteImages(
                                    id = id,
                                    full = fullUrl,
                                    regular = regularUrl, blur_hash = it
                                )
                            )
                            false
                        } else {
                            add(
                                FavuoriteImages(
                                    id = id,
                                    full = fullUrl,
                                    regular = regularUrl, blur_hash = it
                                )
                            )
                            true
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Internet Connection",
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    onClick = {
                        eventHandler.invoke(WallEventHandler.CheckNetwork)
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inverseSurface),
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                ) {
                    Text(text = "Retry", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }, sheetState = sheetState) {
            Column {
                singleSheetItem(
                    name = "SET WALLPAPER",
                    icon = Icons.Filled.Wallpaper,
                    onCLick = {
                        if (imageDecodingStarted) {
                            showProgress = true
                            setWall = true
                        } else {
                            setWallpaperWithToast(context, finalImageBitmap, 1, scope)
                        }
                    })
                singleSheetItem(name = "SET LOCK SCREEN", icon = Icons.Filled.Lock) {
                    if (imageDecodingStarted) {
                        showProgress = true
                        setLock = true
                    } else {
                        setWallpaperWithToast(context, finalImageBitmap, 2, scope)
                    }
                }
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

fun setWallpaperWithToast(
    context: Context,
    finalImageBitmap: Bitmap?,
    value: Int,
    scope: CoroutineScope,
) {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        showToast(context, "Error setting wallpaper: ${exception.localizedMessage}")
    }
    val wallpaperCoroutineScope = CoroutineScope(Dispatchers.Main)

    wallpaperCoroutineScope.launch(coroutineExceptionHandler) {
        try {
            scope.launch(Dispatchers.IO) {
                setWallPaper(
                    context,
                    value,
                    finalImageBitmap
                )
            }
            showToast(context, "Setting wallpaper...Wait for a while")
            delay(1000)
        } catch (e: Exception) {
            showToast(context, "Error setting wallpaper: ${e.localizedMessage}")
        }
    }
}

@Composable
fun singleSheetItem(name: String, icon: ImageVector, onCLick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCLick() }
            .padding(vertical = 10.dp, horizontal = 20.dp),
    ) {
        Icon(imageVector = icon, contentDescription = "", tint = MaterialTheme.colorScheme.onBackground)
        Text(text = name, modifier = Modifier.padding(start = 20.dp), color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun singleButton(icon: ImageVector? = null, navigationIconFunction: () -> Unit) {
    Card(
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .shadow( // Add shadow to the Card
                elevation = 3.dp, // Adjust the elevation value as needed
                shape = CircleShape
            )
    ) {
        IconButton(onClick = { navigationIconFunction() }, modifier = Modifier.fillMaxSize()) {
            icon?.let { icontoshow ->
                Icon(
                    imageVector = icontoshow,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}