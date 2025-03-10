package com.example.myapplication.view.moviesbygenre

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.movieapp.model.Movie
import com.example.myapplication.R
import java.util.Locale

// a holder for a poster and its info for the genre page
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
                val path = movie.poster_path ?: "" // fixes bad population
                ImageFromUrl("w500/${path}") // the image
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
        RatingDisplay(modifier = Modifier.align(Alignment.End), voteAverage = movie.vote_average) // rating
        Text(modifier = Modifier.fillMaxWidth(), // name and year
            text = movie.title + " (" + year + ")",
        )
    }
}

// gives an image from url
@Composable
fun ImageFromUrl(imageUrl: String) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = "https://image.tmdb.org/t/p/$imageUrl").apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                placeholder(R.drawable.poster)
            }).build()
    )

    Image(
        painter = painter,
        contentDescription = "Movie poster",
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}

// shows a start and a rating from 1 to 10 with max 1 point after the dot
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
            text = "%.1f".format(Locale.US, voteAverage), // Display the rating number
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}