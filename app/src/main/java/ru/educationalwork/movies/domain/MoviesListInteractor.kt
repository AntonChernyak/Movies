package ru.educationalwork.movies.domain

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.educationalwork.movies.App.Companion.API_KEY
import ru.educationalwork.movies.presentation.view.fragments.SettingsFragment.Companion.LOCALE
import ru.educationalwork.movies.repository.database.IntMoviesRepository
import ru.educationalwork.movies.repository.model.MovieItem
import ru.educationalwork.movies.repository.server.ApiService

class MoviesListInteractor(
    private val apiService: ApiService,
    private val repository: IntMoviesRepository
) {


    @SuppressLint("CheckResult")
    fun getMoviesList(
        isLoading: MutableLiveData<Boolean>,
        callback: GetMovieCallback
    ) {
        isLoading.value = true

        val getPopularMovies = apiService.getPopularMovies(API_KEY, LOCALE)
        getPopularMovies
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { movieListResponse ->
                    callback.onSuccess(movieListResponse.results)
                    // Выключим loading progress bar
                    isLoading.value = false
                },
                { error ->
                    callback.onError(error.toString())
                }
            )

    }

    interface GetMovieCallback {
        fun onSuccess(repos: List<MovieItem>)
        fun onError(error: String)
    }

    fun insertMovie(movieItem: MovieItem, context: Context) {
        repository.addToFavorite(movieItem, context)
    }

    fun deleteMovie(movieItem: MovieItem, context: Context) {
        repository.deleteFromFavorite(movieItem, context)
    }

    @SuppressLint("CheckResult")
    fun getFavoritesMoviesList(
        context: Context,
        callback: GetMovieCallback
    ) {
        repository.getAllFavoriteMovies(context)
            ?.subscribe{callback.onSuccess(it)}
    }

    fun getCount(context: Context) : Int {
        return repository.getCount(context)
    }

}
