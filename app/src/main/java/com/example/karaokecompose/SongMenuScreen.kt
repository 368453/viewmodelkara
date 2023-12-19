package com.example.karaokecompose

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.room.Room
import com.example.karaoke.db.database.LyricDatabase
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

class SongMenuScreen{
    companion object {
        @Composable
        fun create(navController: NavHostController, backStackEntry: NavBackStackEntry, applicationContext: Context, id: Any, viewModel: KaraokeViewModel): SongMenuScreen {

            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: -1
            val item = backStackEntry.arguments?.getString("item").toString()

            var currentSongs = viewModel.setSongMenu(index, applicationContext, id, navController)

            Column {
                TitleBar(item, true, navController)
                SongMenu(currentSongs, navController)
            }

            return SongMenuScreen()
        }
    }
}
