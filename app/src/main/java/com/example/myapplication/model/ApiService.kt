package com.example.movieapp.model

import com.example.myapplication.token
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Header("Authorization") authHeader: String = token,
        @Query("language") language: String = "en-US"
    ): GenreResponse

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

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("Authorization") authHeader: String = token,
        @Path("movie_id") movieId: Int = 950396,
    ) : MovieDetails
}
