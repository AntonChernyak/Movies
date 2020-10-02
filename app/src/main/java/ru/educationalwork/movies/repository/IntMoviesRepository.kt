package ru.educationalwork.movies.repository

import ru.educationalwork.movies.repository.model.MovieModel

interface IntMoviesRepository {
    val models: List<MovieModel>
    val favoriteList: List<MovieModel>
}