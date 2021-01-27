package ru.educationalwork.movies.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import ru.educationalwork.movies.App
import ru.educationalwork.movies.domain.MoviesListInteractor
import ru.educationalwork.movies.repository.model.MovieItem

class MovieListViewModel(application: Application) : AndroidViewModel(application) {
    private val moviesListLiveData = MutableLiveData<List<MovieItem>>()
    private val errorLiveData = MutableLiveData<String>()
    private val showProgressLiveData = MutableLiveData<Boolean>()

    private val moviesInteractor = App.instance?.moviesInteractor

    val moviesList: LiveData<List<MovieItem>>
        get() = moviesListLiveData

    val showProgress: LiveData<Boolean>
        get() = showProgressLiveData

    val error: LiveData<String>
        get() = errorLiveData

    fun getData() {
            moviesInteractor?.getMoviesList(showProgressLiveData, object : MoviesListInteractor.GetMovieCallback{
                override fun onSuccess(repos: List<MovieItem>) {
                    moviesListLiveData.postValue(repos)
                }

                override fun onError(error: String) {
                    errorLiveData.postValue(error)
                }
            })
    }

}