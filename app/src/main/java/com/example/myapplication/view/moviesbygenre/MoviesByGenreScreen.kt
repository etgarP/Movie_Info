package com.example.myapplication.view.moviesbygenre

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movieapp.model.Genre
import com.example.movieapp.model.Movie
import com.example.movieapp.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesByGenreScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    viewModel: MovieViewModel
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Movies are fun") })
        }
    ) { paddingValues ->
        MoviesByGenre(modifier = Modifier.padding(paddingValues), onClick = onClick, viewModel = viewModel)
    }
}

@Composable
fun MoviesByGenre(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel,
    onClick: () -> Unit,
) {
    val genres by viewModel.genres.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val movies by viewModel.movies.collectAsState()

    Column (modifier = modifier) {
        if (genres.isNotEmpty()) {
            ScrollableTabs(selectedTabIndex, genres, setSelectedTabIndex = {
                selectedTabIndex = it
            }, viewModel = viewModel)
        }
        for (i in genres.indices) {
            AnimatedVisibility(
                visible = selectedTabIndex == i,
                enter = fadeIn(tween()),
                exit = fadeOut(tween())
            ) {
                MoviesByGenre(movies = movies[i], onClick = onClick, viewModel = viewModel)
            }
        }

    }
}

@Composable
fun ScrollableTabs(
    selectedTabIndex: Int,
    genres: List<Genre>,
    setSelectedTabIndex: (Int) -> Unit,
    viewModel: MovieViewModel
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth(),
        edgePadding = 0.dp
    ) {
        genres.forEachIndexed { index, genre ->
            val color by animateColorAsState(
                targetValue = if (index == selectedTabIndex)
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                label = "tab row color"
            )
            Tab(
                selected = index == selectedTabIndex,
                onClick = {
                    setSelectedTabIndex(index)
                    viewModel.selectGenre(genre.id)
                },
                text = {
                    Text(
                        text = genre.name,
                        color = color
                    )
                }
            )
        }
    }
}


@Composable
fun MoviesByGenre(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel,
    movies: List<Movie>,
    onClick: () -> Unit,
) {
    val scrollState = rememberLazyGridState()
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        state = scrollState
    ) {
        items(movies) { movie ->
            PictureHolder(movie, onClick = {
                viewModel.selectMovie(movieId = movie.id)
                onClick()
            })
        }
    }
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.canScrollForward }
            .collect { canScrollForward ->
                if (!canScrollForward) {
                    viewModel.addAPage()
                }
            }
    }
}