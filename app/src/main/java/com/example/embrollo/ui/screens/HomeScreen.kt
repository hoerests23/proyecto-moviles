package com.example.embrollo.ui.screens

import HomeScreenMedium
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import obtenerWindowSizeClass


@Composable
fun HomeScreen(){
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompact()
        WindowWidthSizeClass.Medium -> HomeScreenMedium()
        WindowWidthSizeClass.Expanded -> HomeScreenExpanded()
    }
}