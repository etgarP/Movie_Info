package com.example.myapplication.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.viewmodel.MovieViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.view.moviedetails.MovieDetailsScreen
import com.example.myapplication.view.moviesbygenre.MoviesByGenreScreen
import dagger.hilt.android.AndroidEntryPoint

// starting point
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// stores the nav host with the two screens - the genre screen, and the details screen for
// a chosen movie
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel: MovieViewModel = hiltViewModel()
    val navController = rememberNavController() // assists with transitions between screens
    var genreIndex by remember ({ mutableIntStateOf(0) }) // helps with consistent transitions
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable(
            route = "home",
        ) { MoviesByGenreScreen( // on click of a movie goes to details
            onClick = { navController.navigate("details") },
            viewModel = viewModel,
            genreIndex = genreIndex,
            setGenreIndex = {index -> genreIndex = index}
        ) }
        composable("details") {
            MovieDetailsScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
            ) }
    }
}
