package com.example.movieapp.model

import com.example.myapplication.token
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

// defines the queries to the database
interface ApiService {
    // getting a list of genres
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Header("Authorization") authHeader: String = token,
        @Query("language") language: String = "en-US"
    ): GenreResponse

    // getting a list of movies by genre and by page - 1, 2, ...
    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Header("Authorization") authHeader: String = token,
        @Query("with_genres") genreId: Int,
        @Query("language") language: String = "en-US",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("page") page: Int = 1
    ): MovieResponse

    // getting extra details about the movie
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("Authorization") authHeader: String = token,
        @Path("movie_id") movieId: Int = 950396,
    ) : MovieDetails
}
