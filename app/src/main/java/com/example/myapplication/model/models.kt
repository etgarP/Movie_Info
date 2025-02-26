package com.example.movieapp.model

data class Genre(val id: Int, val name: String)

data class Movie(
    val id: Int = 0,
    val title: String = "Blank",
    val release_date: String = "0000-00-00",
    val poster_path: String = "",
    val backdrop_path: String = "",
    val overview: String = "",
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
)

data class MovieDetails(
    val posterPath: String?,
    val backdropPath: String?,
    val title: String,
    val tagline: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val runtime: Int,
    val genres: List<Genre>,
    val overview: String
)

data class MovieResponse(val results: List<Movie>)
data class GenreResponse(val genres: List<Genre>)