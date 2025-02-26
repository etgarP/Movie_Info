package com.example.movieapp.model

import com.example.myapplication.token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getGenres(): Result<List<Genre>> {
        return try {
            val response = apiService.getGenres()
            Result.success(response.genres)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMoviesByGenre(genreId: Int, page: Int = 1): Result<List<Movie>> {
        return try {
            val response = apiService.getMoviesByGenre(page = page, genreId = genreId)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMovieDetails(moveId: Int): Result<MovieDetails> {
        return try {
            val response = apiService.getMovieDetails(movieId = moveId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}