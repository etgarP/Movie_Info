package com.example.myapplication.view.moviesbygenre

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    movie: Movie,
    onClick: () -> Unit
) {
    val year = movie.release_date.split("-")[0]
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(1f)
        .clickable { onClick() }
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 5.dp,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val path = movie.poster_path ?: ""
                ImageFromUrl("w500/${path}") // sometimes its null

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
        model = "https://image.tmdb.org/t/p/$imageUrl",
        contentDescription = "Movie poster",
    )
}

@Composable
fun RatingDisplay(modifier: Modifier = Modifier, voteAverage: Double) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.Default.Star, // Gold star icon
            contentDescription = "Rating Star",
            tint = Color(0xFFFFD700)
        )
        Text(
            text = "$voteAverage", // Display the rating number
            modifier = Modifier.padding(start = 8.dp)
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