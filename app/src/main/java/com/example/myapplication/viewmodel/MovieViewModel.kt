package com.example.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.model.Genre
import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieDetails
import com.example.movieapp.model.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


// view model for getting genres, getting movies by genre, getting their next page,
// caching the the retrieved information, and getting movie details
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    // stores the genres
    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    // stores a list of movies by genre
    private val _movies = MutableStateFlow<MutableList<List<Movie>>>(mutableListOf())
    val movies: StateFlow<List<List<Movie>>> = _movies.asStateFlow()

    // stores the selected genre
    private val _selectedGenre = MutableStateFlow<Int?>(null)
    val selectedGenre: StateFlow<Int?> = _selectedGenre.asStateFlow()

    // stores the selected movie id for the details page
    private var selectedMovieId = 0

    // stores the selected movie details
    private val _selectedMovie = MutableStateFlow<MovieDetails?>(null)
    val selectedMovie: StateFlow<MovieDetails?> = _selectedMovie.asStateFlow()

    // stores the current page retrieved per genre
    private var _currentPage: MutableList<Int> = mutableListOf()

    // the index of the genre were on
    private var _currentIndex: Int = 0

    // gets the genres on init
    init {
        fetchGenres()
    }

    // gets the genres and saves them
    private fun fetchGenres() {
        viewModelScope.launch {
            repository.getGenres().onSuccess { fetchedGenres ->
                _genres.value = fetchedGenres
                _movies.value = MutableList(fetchedGenres.size) { emptyList() }
                _currentPage = MutableList(fetchedGenres.size) { 0 }
                if (fetchedGenres.isNotEmpty()) {
                    selectGenre(fetchedGenres.first().id)
                }
            }.onFailure {

            }
        }
    }

    // fetch movie details
    private fun fetchMovieDetails() {
        viewModelScope.launch {
            repository.getMovieDetails(selectedMovieId).onSuccess { fetchedDetails ->
                _selectedMovie.value = fetchedDetails
            }.onFailure {
                it.message?.let { it1 -> Log.e("fetch movie details", it1) }
            }
        }
    }

    // selects a genre
    fun selectGenre(genreId: Int) {
        _selectedGenre.value = genreId
        _currentIndex = findGenreIndexById(genreId)
    }

    // selects a movie by id
    fun selectMovie(movieId: Int) {
        selectedMovieId = movieId
        fetchMovieDetails()
    }

    // adds another page for the movie list by genre
    fun addAPage() {
        _currentPage[_currentIndex] += 1 // Increment page for the selected genre
        fetchNextPage() // Fetch next page for the genre
    }

    // gets an extra page
    private fun fetchNextPage() {
        viewModelScope.launch {
            _selectedGenre.value?.let {
                repository.getMoviesByGenre(it, page = _currentPage[_currentIndex]).onSuccess { fetchedMovies ->
                    addMovies(fetchedMovies = fetchedMovies)
                }.onFailure {

                }
            }
        }
    }

    // added fetched movies
    private fun addMovies(fetchedMovies: List<Movie>) {
        // Get the current list of movies
        val currentMovies = _movies.value.toMutableList()
        // Update the list at the found index with the fetched movies
        currentMovies[_currentIndex] += fetchedMovies
        // Set the updated list back to the MutableStateFlow
        _movies.value = currentMovies
    }

    // gets index by genre id
    private fun findGenreIndexById(genreId: Int): Int {
        return _genres.value.indexOfFirst { it.id == genreId }
    }

}