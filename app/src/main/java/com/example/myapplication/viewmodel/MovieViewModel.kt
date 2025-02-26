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

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    private val _movies = MutableStateFlow<MutableList<List<Movie>>>(mutableListOf())
    val movies: StateFlow<List<List<Movie>>> = _movies.asStateFlow()

    private val _selectedGenre = MutableStateFlow<Int?>(null)
    val selectedGenre: StateFlow<Int?> = _selectedGenre.asStateFlow()

    private var selectedMovieId = 0

    private val _selectedMovie = MutableStateFlow<MovieDetails?>(null)
    val selectedMovie: StateFlow<MovieDetails?> = _selectedMovie.asStateFlow()

    private var _currentPage: MutableList<Int> = mutableListOf()

    private var _currentIndex: Int = 0

    init {
        fetchGenres()
    }

    private fun fetchMovieDetails() {
        viewModelScope.launch {
            repository.getMovieDetails(selectedMovieId).onSuccess { fetchedDetails ->
                _selectedMovie.value = fetchedDetails
            }.onFailure {
                it.message?.let { it1 -> Log.e("fetch movie details", it1) }
            }
        }
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            repository.getGenres().onSuccess { fetchedGenres ->
                _genres.value = fetchedGenres
                _movies.value = MutableList(fetchedGenres.size) { emptyList() }
                _currentPage = MutableList(fetchedGenres.size) { 1 }
                if (fetchedGenres.isNotEmpty()) {
                    selectGenre(fetchedGenres.first().id)
                }
            }.onFailure {

            }
        }
    }

    fun selectGenre(genreId: Int) {
        _selectedGenre.value = genreId
        _currentIndex = findGenreIndexById(genreId)
        fetchMoviesByGenre(genreId)

    }

    fun selectMovie(movieId: Int) {
        selectedMovieId = movieId
        fetchMovieDetails()
    }

    fun addAPage() {
        _currentPage[_currentIndex] += 1 // Increment page for the selected genre
        fetchNextPage(_currentIndex) // Fetch next page for the genre
    }

    private fun setMovies(genreId: Int, fetchedMovies: List<Movie>) {
        // Get the current list of movies
        val currentMovies = _movies.value.toMutableList()
        // Update the list at the found index with the fetched movies
        currentMovies[_currentIndex] = fetchedMovies
        // Set the updated list back to the MutableStateFlow
        _movies.value = currentMovies
    }

    private fun fetchNextPage(page: Int) {
        viewModelScope.launch {
            _selectedGenre.value?.let {
                repository.getMoviesByGenre(it, page = _currentPage[_currentIndex]).onSuccess { fetchedMovies ->
                    addMovies(genreId = it, fetchedMovies = fetchedMovies)
                }.onFailure {

                }
            }
        }
    }

    private fun addMovies(genreId: Int, fetchedMovies: List<Movie>) {
        // Get the current list of movies
        val currentMovies = _movies.value.toMutableList()
        // Update the list at the found index with the fetched movies
        currentMovies[_currentIndex] += fetchedMovies
        // Set the updated list back to the MutableStateFlow
        _movies.value = currentMovies
    }

    private fun findGenreIndexById(genreId: Int): Int {
        return _genres.value.indexOfFirst { it.id == genreId }
    }

    private fun fetchMoviesByGenre(genreId: Int) {
        viewModelScope.launch {
            repository.getMoviesByGenre(genreId).onSuccess { fetchedMovies ->
                setMovies(genreId = genreId, fetchedMovies = fetchedMovies)
            }.onFailure {

            }
        }
    }
}