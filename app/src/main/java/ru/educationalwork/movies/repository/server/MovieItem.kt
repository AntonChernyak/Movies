package ru.educationalwork.movies.repository.server

data class MovieItem (
    val posterPath: String,
    val description: String,
    val title: String,
    var isFavorite: Boolean,
    var isClick: Boolean
)