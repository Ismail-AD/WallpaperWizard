package com.acdev.wallpaperwizard.ui.Screens.Componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.acdev.wallpaperwizard.utils.BlurHashDecoder

@Composable
fun UnsplashImageItem(unsplashImageUrl: String, blurHash: String? , onClick: () -> Unit) {
    val context = LocalContext.current
    val blurHashDrawable = BlurHashDecoder.blurHashBitmap(context.resources, blurHash)
    Card(
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(1.dp)
            .height(240.dp)
            .clickable { onClick() }) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context).data(unsplashImageUrl)
                    .placeholder(blurHashDrawable).error(blurHashDrawable)
                    .crossfade(500).build(),
                contentDescription = null, contentScale = ContentScale.Crop
            ) {
                SubcomposeAsyncImageContent(modifier = Modifier.fillMaxSize())
            }
        }

    }
}