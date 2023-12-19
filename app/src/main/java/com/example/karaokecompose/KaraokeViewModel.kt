package com.example.karaokecompose

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Room
import com.example.karaoke.db.database.Lyric
import com.example.karaoke.db.database.LyricDao
import com.example.karaoke.db.database.LyricDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KaraokeViewModel : ViewModel() {

    lateinit var db : LyricDatabase

    @Composable
    fun setSongMenu(index: Int, applicationContext: Context, id: Any, navController: NavController): List<Entry> {
        val (songs, setSongs) = remember { mutableStateOf<List<Entry>>(emptyList()) }

        // downloaded lyrics
        if (index == 0) {
            db = LyricDatabase.getDatabase(applicationContext)
            val downloadedSongs = mutableListOf<Entry>()
            LaunchedEffect(id) {
                db.lyricDao().getAllItems().collect { downloadedSong ->
                    for (song in downloadedSong) {
                        downloadedSongs.add(
                            Entry(
                                track_id = song.id,
                                track_name = song.songName,
                                track_artist = song.songArtist
                            )
                        )
                    }
                    setSongs(downloadedSongs)
                }
            }
        }

        // top 100 charts
        if (index == 1) {
            Top100List(object : GetRequestCallBackSongs {
                override fun onSuccess(songs: List<Entry>) {
                    setSongs(songs)
                }

                override fun onFailure(error: String) {
                    // do something with the error idk
                }
            })
        }

        // search by artist
        else if (index == 2) {
            QueryBox("artist", navController) {
                ArtistList(it, object : GetRequestCallBackSongs {
                    override fun onSuccess(songs: List<Entry>) {
                        setSongs(songs)
                    }

                    override fun onFailure(error: String) {
                        // do something with the error idk
                    }
                })
            }
        }

        // search by song
        else if (index == 3) {
            QueryBox("song", navController) {
                SongList(it, object : GetRequestCallBackSongs {
                    override fun onSuccess(songs: List<Entry>) {
                        setSongs(songs)
                    }

                    override fun onFailure(error: String) {
                        // do something with the error idk
                    }
                })
            }
        }

        return songs
    }

    @Composable
    fun setLyrics(applicationContext: Context, id: Any, songId: Int, setDownloaded: (Boolean) -> Unit, setLyrics: (String) -> Unit){
        db = LyricDatabase.getDatabase(applicationContext)
        LaunchedEffect(id) {
            db.lyricDao().getItem(songId).collect { dbLyrics ->
                if (dbLyrics != null){
                    setLyrics(dbLyrics.songLyric)
                    setDownloaded(true)
                }
                else{
                    SongLyrics(songId, object : GetRequestCallBackLyrics {
                        override fun onSuccess(lyrics: String) {
                            setLyrics(lyrics)
                        }
                        override fun onFailure(error: String) {
                            // do something with the error idk
                        }
                    })
                }
            }
        }
    }
}