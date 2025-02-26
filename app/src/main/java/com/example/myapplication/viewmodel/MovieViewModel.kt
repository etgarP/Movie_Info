package com.example.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.model.Genre
import com.example.movieapp.model.Movie
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

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _selectedGenre = MutableStateFlow<Int?>(null)
    val selectedGenre: StateFlow<Int?> = _selectedGenre.asStateFlow()

    private var _currentPage = 0

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            repository.getGenres().onSuccess { fetchedGenres ->
                _genres.value = fetchedGenres
                if (fetchedGenres.isNotEmpty()) {
                    selectGenre(fetchedGenres.first().id)
                }
            }.onFailure {
                // Handle error (e.g., show a snackbar)
            }
        }
    }

    fun selectGenre(genreId: Int) {
        _selectedGenre.value = genreId
        fetchMoviesByGenre(genreId)
        _currentPage = 1
    }

    fun addAPage() {
        _currentPage += 1
        fetchNextPage(_currentPage)
    }

    private fun fetchNextPage(page: Int) {
        viewModelScope.launch {
            _selectedGenre.value?.let {
                repository.getMoviesByGenre(it, page = _currentPage).onSuccess { fetchedMovies ->
                    _movies.value += fetchedMovies
                }.onFailure {
                    // Handle error
                }
            }
        }
    }

    private fun fetchMoviesByGenre(genreId: Int) {
        viewModelScope.launch {
            repository.getMoviesByGenre(genreId).onSuccess { fetchedMovies ->
                _movies.value = fetchedMovies
            }.onFailure {
                // Handle error
            }
        }
    }
}