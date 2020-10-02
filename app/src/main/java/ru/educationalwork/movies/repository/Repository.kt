package ru.educationalwork.movies.repository

import ru.educationalwork.movies.repository.model.MovieModel

class MoviesRepository : IntMoviesRepository{

    object Storage {
        val FAVORITE_LIST: MutableList<MovieModel> = mutableListOf()
        var items = arrayListOf<MovieModel>()
    }

    init {
        models
    }

    override val models: List<MovieModel>
        get() = Storage.items

    override val favoriteList: List<MovieModel>
        get() = Storage.FAVORITE_LIST

}