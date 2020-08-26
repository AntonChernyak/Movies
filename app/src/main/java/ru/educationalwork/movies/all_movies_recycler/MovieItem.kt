package ru.educationalwork.movies.all_movies_recycler

data class MovieItem(
    val title: Int,
    val description: Int,
    val poster: Int,
    var isClick: Boolean = false,
    var isFavorite: Boolean = false
)