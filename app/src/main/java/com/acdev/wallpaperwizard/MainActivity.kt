package com.acdev.wallpaperwizard

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.acdev.wallpaperwizard.navigation.MainScreen
import com.acdev.wallpaperwizard.ui.theme.WallpaperWizardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WallpaperWizardTheme {
                val navController = rememberNavController()
                MainScreen()
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    WallpaperWizardTheme {
//
//    }
//}