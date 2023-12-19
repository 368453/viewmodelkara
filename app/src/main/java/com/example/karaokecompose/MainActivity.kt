package com.example.karaokecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {

    private val viewModel: KaraokeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "MainMenu"){
                    composable("MainMenu"){
                        MainMenuScreen.create(navController)
                    }
                    composable("SongMenu/{index}/{item}"){ backStackEntry ->
                        SongMenuScreen.create(navController, backStackEntry, applicationContext, id, viewModel)
                    }
                    composable("SongLyric/{id}/{artist}/{name}") { backStackEntry ->
                        SongLyricScreen.create(navController, backStackEntry, applicationContext, id, lifecycleScope, viewModel)
                    }
                }
            }
        }
    }
}
