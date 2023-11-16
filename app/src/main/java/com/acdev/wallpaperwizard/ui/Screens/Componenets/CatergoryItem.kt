package com.acdev.wallpaperwizard.ui.Screens.Componenets


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CategoryItem(
    icon: ImageVector? = null,
    title: String,
    onClick: () -> Unit={},
    vertical: Dp = 14.dp,
    horizontal: Dp = 7.dp
) {
    Card(
        modifier = Modifier
            .padding(vertical = vertical, horizontal = horizontal)
            .shadow( // Add shadow to the Card
                elevation = 4.dp, // Adjust the elevation value as needed
                shape = CircleShape
            )
    ) {
        Box(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        contentDescription = ""
                    )
                }

                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }
        }
    }
}
