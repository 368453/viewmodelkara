package com.example.karaokecompose

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

class MainMenuScreen {
    companion object {

        @Composable
        fun create(navController: NavController) {
            val items =
                listOf("Downloaded Lyrics", "Top 100 Charts", "Search by Artist", "Search by Song")
            Column {
                TitleBar("Karaoke App", false, navController)
                MainMenu(items, navController)
            }
        }
    }
}
