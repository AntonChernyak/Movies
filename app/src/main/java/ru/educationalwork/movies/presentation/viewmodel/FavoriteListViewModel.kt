package ru.educationalwork.movies.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.educationalwork.movies.App
import ru.educationalwork.movies.domain.MoviesListInteractor
import ru.educationalwork.movies.repository.model.MovieItem

class FavoriteListViewModel(application: Application) : AndroidViewModel(application){
    private val favoriteMovieLiveData = MutableLiveData<MovieItem>()
    private val favoriteMoviesListLiveData = MutableLiveData<List<MovieItem>>()
    private val moviesInteractor = App.instance?.moviesInteractor

    val favoriteMovie: LiveData<MovieItem>
        get() = favoriteMovieLiveData

    val favoriteMovieList: LiveData<List<MovieItem>>
        get() = favoriteMoviesListLiveData


    fun addToFavorite(movieItem: MovieItem) {
        moviesInteractor?.insertMovie(movieItem, getApplication())
    }

    fun deleteFromFavorite(movieItem: MovieItem) {
        moviesInteractor?.deleteMovie(movieItem, getApplication())
    }

    fun getData() {
        moviesInteractor?.getFavoritesMoviesList(getApplication(), object : MoviesListInteractor.GetMovieCallback{
            override fun onSuccess(repos: List<MovieItem>) {
                favoriteMoviesListLiveData.value = repos
            }

            override fun onError(error: String) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getFavoritesMoviesSize() : Int?{
        return moviesInteractor?.getCount(context = getApplication())
    }

}