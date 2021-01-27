package ru.educationalwork.movies.repository.database

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.educationalwork.movies.repository.model.MovieItem


class MoviesRepository: IntMoviesRepository {

    override fun getCount(context: Context): Int = runBlocking {
        val count = async  {
            db.getInstance(context)?.getMovieDao()?.getCount() ?: 0
        }
        count.start()
        count.await()
    }

    override fun addToFavorite(movieItem: MovieItem, context: Context) {
        Observable
            .fromCallable { db.getInstance(context)?.getMovieDao()?.addMovie(movieItem) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun deleteFromFavorite(movieItem: MovieItem, context: Context) {
        Observable
            .fromCallable { db.getInstance(context)?.getMovieDao()?.deleteMovie(movieItem) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun getAllFavoriteMovies(context: Context): Flowable<List<MovieItem>>? {
        return  db.getInstance(context)?.getMovieDao()?.getAllFavoriteMovies()
            ?.observeOn(AndroidSchedulers.mainThread())

    }
}