package com.acdev.wallpaperwizard.ui.Screens.Componenets

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "",
    navigationIconFunction: () -> Unit = {},
    trailingIconFunction: () -> Unit = {},
    headerIconID: ImageVector? = null,
    tailIconID: ImageVector? = null,
) {
    TopAppBar(
        title = { Text(text = title, color = MaterialTheme.colorScheme.onBackground) },
        modifier = Modifier.background(MaterialTheme.colorScheme.background), navigationIcon = {
            headerIconID?.let { hIcon ->
                IconButton(onClick = { navigationIconFunction.invoke() }) {
                    Icon(
                        imageVector = hIcon,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }, actions = {
            tailIconID?.let { tIcon ->
                IconButton(onClick = { trailingIconFunction.invoke() }) {
                    Icon(
                        imageVector = tIcon,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    )
}