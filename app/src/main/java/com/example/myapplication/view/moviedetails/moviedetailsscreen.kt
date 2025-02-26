package com.example.myapplication.view.moviedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.model.MovieDetails
import com.example.movieapp.viewmodel.MovieViewModel

@Composable
fun Moviedetailsscreen(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel = hiltViewModel()
) {
    Column {
        BackdropPath()
        FirstDetails()
        OverView()
        FinalDetails()
    }
}

@Composable
fun BackdropPath(modifier: Modifier = Modifier, movieDetails: MovieDetails) {

}

@Composable
fun FirstDetails(modifier: Modifier = Modifier, movieDetails: MovieDetails) {

}

@Composable
fun OverView(modifier: Modifier = Modifier, movieDetails: MovieDetails) {

}

@Composable
fun FinalDetails(modifier: Modifier = Modifier, movieDetails: MovieDetails) {

}
