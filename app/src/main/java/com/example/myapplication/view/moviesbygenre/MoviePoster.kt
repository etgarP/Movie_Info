package com.example.myapplication.view.moviesbygenre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieapp.model.Movie

@Composable
fun PictureHolder(
    movie: Movie
) {
    val year = movie.release_date.split("-")[0]
    Column(modifier = Modifier.padding(5.dp).fillMaxWidth(1f)) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 5.dp,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ImageFromUrl(movie.poster_path ?: "") // sometimes its null

//                BottomBlock(modifier = Modifier.align(Alignment.BottomEnd)) {
//                    RatingDisplay(movie.vote_average)
//                }
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
        RatingDisplay(modifier = Modifier.align(Alignment.End), voteAverage = movie.vote_average)
        Text(modifier = Modifier.fillMaxWidth(),
            text = movie.title + " (" + year + ")",
//            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImageFromUrl(imageUrl: String) {
    AsyncImage(
        model = "https://image.tmdb.org/t/p/w500$imageUrl",
        contentDescription = "Movie poster",
    )
}

@Composable
fun RatingDisplay(modifier: Modifier = Modifier, voteAverage: Double) {
    // Map vote_average (0-10) to a 1-5 scale
//    val rating = ((voteAverage / 2).coerceIn(1.0, 5.0)).toInt() // Ensures it's between 1 and 5

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$voteAverage", // Display the rating number
            modifier = Modifier.padding(start = 8.dp)
        )
        Icon(
            imageVector = Icons.Default.Star, // Gold star icon
            contentDescription = "Rating Star",
            tint = Color.Yellow
        )
    }
}

@Preview
@Composable
fun BottomBlock(modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {
    Row (modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Surface (
            modifier = Modifier.padding(5.dp),
            color = MaterialTheme.colorScheme.surfaceDim,
            shape = RoundedCornerShape(4.dp),
        ) {
            content()
        }
    }
}