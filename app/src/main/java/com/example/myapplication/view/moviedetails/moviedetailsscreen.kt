package com.example.myapplication.view.moviedetails

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.model.Genre
import com.example.movieapp.model.MovieDetails
import com.example.movieapp.viewmodel.MovieViewModel
import com.example.myapplication.view.moviesbygenre.ImageFromUrl
import com.example.myapplication.view.moviesbygenre.RatingDisplay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel,
    onBack: () -> Unit
) {
    val movieDetails = viewModel.selectedMovie.collectAsState()
    Column {


        Button(onClick = {viewModel.selectMovie(950396)}) {
            Text("hello")
        }
    }


    Scaffold(
        topBar = {TransparentTopBar(onBack)},
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) {
        movieDetails.value?.let {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Backdrop(movieDetails = it)
                FirstDetails(movieDetails = it)
                HorizontalDivider(Modifier.padding(horizontal = 10.dp))
                OverView(movieDetails = it)
                HorizontalDivider(Modifier.padding(horizontal = 10.dp))
                FinalDetails(movieDetails = it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTopBar(onBack: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        title = { /* Empty title for transparency */ },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = Color.White
        ),
        modifier = Modifier.background(Color.Transparent).statusBarsPadding(),
    )
}


@Composable
fun Backdrop(modifier: Modifier = Modifier, movieDetails: MovieDetails) {
    val path = movieDetails.backdrop_path?: ""
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 16.dp, // Adjust the radius as needed
            bottomEnd = 16.dp
        ),
        shadowElevation = 10.dp,
        color = MaterialTheme.colorScheme.primary
    ) {
        ImageFromUrl("original/${path}")
    }
}

@Composable
fun FirstDetails(modifier: Modifier = Modifier, movieDetails: MovieDetails) {
    val year = movieDetails.release_date.split("-")[0]
    val title = movieDetails.title + " (" + year + ")"
    Column (modifier.padding(20.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Row (
            Modifier.padding(top = 10.dp).height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "movie time")
                Text(modifier = Modifier.padding(horizontal = 5.dp), text = "${movieDetails.runtime} minutes")
            }
            VerticalDivider(modifier = Modifier.fillMaxHeight())
            Row (Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                RatingDisplay(voteAverage = movieDetails.vote_average)
            }
        }
    }
}

@Composable
fun OverView(modifier: Modifier = Modifier, movieDetails: MovieDetails) {
    ContentWithTitle(modifier, title = "Overview") {
        Text(movieDetails.overview)
    }
}

@Composable
fun FinalDetails(modifier: Modifier = Modifier, movieDetails: MovieDetails) {
    ContentWithTitle(modifier, title = "Overview") {
        GenreChips(genres = movieDetails.genres)
    }
}

@Composable
fun ContentWithTitle(
    modifier: Modifier = Modifier, title: String = "",
    content: @Composable () -> Unit
) {
    Column (modifier.padding(20.dp)) {
        Text(modifier = Modifier.padding(vertical = 4.dp),
            text = "$title:", style = MaterialTheme.typography.titleMedium)
        content()
    }
}

@Composable
fun GenreChips(genres: List<Genre>) {
    // Create a horizontal scrollable row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        genres.forEach { genre ->
            GenreChip(genre = genre)
        }

    }
}

@Composable
fun GenreChip(genre: Genre) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        ),
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = genre.name,
            modifier = Modifier.padding(8.dp),
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGenreChips() {
    val genres = listOf(
        Genre(1, "Action"),
        Genre(2, "Comedy"),
        Genre(3, "Drama"),
        Genre(4, "Horror"),
        Genre(5, "Romance"),
        Genre(6, "Romance")
    )
    GenreChips(genres = genres)
}
