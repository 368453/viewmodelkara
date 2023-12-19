package com.example.karaokecompose

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.karaoke.db.database.Lyric
import com.example.karaoke.db.database.LyricDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongLyricScreen {
    companion object {
        @Composable
        fun create(navController: NavHostController,backStackEntry: NavBackStackEntry, applicationContext: Context, id: Any, lifecycleScope: LifecycleCoroutineScope, viewModel: KaraokeViewModel) {
            val songId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            val artist = backStackEntry.arguments?.getString("artist").toString()
            val name = backStackEntry.arguments?.getString("name").toString()
            val (downloaded, setDownloaded) = remember { mutableStateOf<Boolean>(false) }
            val (lyrics, setLyrics) = remember { mutableStateOf<String>("") }

            viewModel.setLyrics(applicationContext, id, songId, setDownloaded, setLyrics)

            Column {
                TitleBar(name, true, navController)
                FullscreenTextBox(lyrics)
                DownloadButton(downloaded, onClick = {
                    lifecycleScope.launch(Dispatchers.IO) {
                        if (!downloaded){
                            viewModel.db.lyricDao().insert(
                                Lyric(
                                    id = songId,
                                    songName = name,
                                    songArtist = artist,
                                    songLyric = lyrics
                                )
                            )
                        }
                        else{
                            viewModel.db.lyricDao().removeItem(songId)
                        }

                    }
                    setDownloaded(!downloaded)
                })
            }
        }
    }
}
