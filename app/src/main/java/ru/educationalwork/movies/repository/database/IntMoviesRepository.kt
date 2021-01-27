package ru.educationalwork.movies.repository.database

import android.content.Context
import io.reactivex.Flowable
import ru.educationalwork.movies.repository.model.MovieItem

interface IntMoviesRepository {
    fun getCount(context: Context): Int

    fun addToFavorite(movieItem: MovieItem, context: Context)

    fun deleteFromFavorite(movieItem: MovieItem, context: Context)

    fun getAllFavoriteMovies(context: Context): Flowable<List<MovieItem>>?
}