package com.example.myapplication.view.moviesbygenre

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesByGenreScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel = hiltViewModel(),
) {
    val genres by viewModel.genres.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column (modifier = modifier) {
        CenterAlignedTopAppBar(
            title = {
                Text("genres")
            }
        )
        if (genres.isNotEmpty()) {
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
                            selectedTabIndex = index
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
        MoviesByGenre()
    }
}


@Composable
fun MoviesByGenre(modifier: Modifier = Modifier, viewModel: MovieViewModel = hiltViewModel()) {
    val movies by viewModel.movies.collectAsState()
    val scrollState = rememberLazyGridState()
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        state = scrollState
    ) {
        items(movies) { movie ->
            PictureHolder(movie)
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