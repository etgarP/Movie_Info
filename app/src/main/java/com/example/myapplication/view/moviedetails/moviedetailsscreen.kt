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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.movieapp.model.Genre
import com.example.movieapp.model.MovieDetails
import com.example.movieapp.viewmodel.MovieViewModel
import com.example.myapplication.R
import com.example.myapplication.view.moviesbygenre.RatingDisplay

// the screen that gives extra details about a movie, nav host navigates to it on click
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel,
    onBack: () -> Unit,

) {
    val movieDetails = viewModel.selectedMovie.collectAsState()
    Scaffold(
        topBar = {TransparentTopBar(onBack)},
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        movieDetails.value?.let { // shows the page once movie details is populated
            Column {
                Backdrop(movieDetails = it)
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    FirstDetails(movieDetails = it)
                    HorizontalDivider(Modifier.padding(horizontal = 10.dp))
                    OverView(movieDetails = it)
                    HorizontalDivider(Modifier.padding(horizontal = 10.dp))
                    FinalDetails(movieDetails = it)
                }
            }

        }
    }
}

// transparent top bar for aesthetics with back button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTopBar(onBack: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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


// shows the back drop of rounded bottom corners
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
        ImageFromUrlBackdrop("w780/${path}")
    }
}

// gets image from the url of the backdrop
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageFromUrlBackdrop(imageUrl: String) {
    val context = LocalContext.current
    val placeholderDrawable = context.getDrawable(R.drawable.img_1)
    GlideImage(
        contentScale = ContentScale.FillWidth,
        model = "https://image.tmdb.org/t/p/$imageUrl",
        loading = placeholder(placeholderDrawable),
        contentDescription = "backdrop"
    )
}

// shows the name of the movie + year + time of the movie + rating
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

// shows the overview
@Composable
fun OverView(modifier: Modifier = Modifier, movieDetails: MovieDetails) {
    ContentWithTitle(modifier, title = "Overview") {
        Text(movieDetails.overview)
    }
}

// shows the genres as chips
@Composable
fun FinalDetails(modifier: Modifier = Modifier, movieDetails: MovieDetails) {
    ContentWithTitle(modifier, title = "Genres") {
        GenreChips(genres = movieDetails.genres)
    }
}

// basically just has a title and content
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

// takes a list of genres and returns chips for them
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

// shows a single chip
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
