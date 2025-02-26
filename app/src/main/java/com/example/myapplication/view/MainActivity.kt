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
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.animation.scaleOut
import androidx.compose.animation.fadeOut
import androidx.compose.animation.fadeIn
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.viewmodel.MovieViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.view.moviedetails.MovieDetailsScreen
import com.example.myapplication.view.moviesbygenre.MoviesByGenreScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel: MovieViewModel = hiltViewModel()
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        // the navigation host
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = modifier.padding(paddingValues)
        ) {
            composable(
                route = "home",
                exitTransition = {
                    when (targetState.destination.route) {
                        "details" -> scaleOutOfContainer(direction = Direction.OUTWARDS)
                        else -> null
                    }
                }
            ) { MoviesByGenreScreen(
                onClick = { navController.navigate("details") },
                viewModel = viewModel
            ) }
            composable("details",
                enterTransition = {
                    when (initialState.destination.route) {
                        "home" -> scaleIntoContainer(direction = Direction.INWARDS)
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "home" -> scaleOutOfContainer(direction = Direction.OUTWARDS)
                        else -> null
                    }
                }) { MovieDetailsScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                ) }
        }
    }
}

enum class Direction {
    INWARDS,
    OUTWARDS
}

// scan in and out animation to make using them shorter and easier
fun scaleIntoContainer(
    direction: Direction = Direction.INWARDS,
    initialScale: Float = if (direction == Direction.OUTWARDS) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

fun scaleOutOfContainer(
    direction: Direction = Direction.OUTWARDS,
    targetScale: Float = if (direction == Direction.INWARDS) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 90))
}

