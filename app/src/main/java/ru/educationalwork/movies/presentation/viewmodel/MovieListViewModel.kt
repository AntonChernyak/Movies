package ru.educationalwork.movies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import ru.educationalwork.movies.App
import ru.educationalwork.movies.domain.MoviesListInteractor
import ru.educationalwork.movies.repository.model.MovieModel

class MovieListViewModel : ViewModel() {
    private val moviesListLiveData = MutableLiveData<List<MovieModel>>()
    private val errorLiveData = MutableLiveData<String>()

    private val moviesInteractor = App.instance?.moviesInteractor

    val moviesList: LiveData<List<MovieModel>>
        get() = moviesListLiveData

    fun getData() {
            moviesInteractor?.getMoviesList(object : MoviesListInteractor.GetMovieCallback{
                override fun onSuccess(repos: List<MovieModel>) {
                    moviesListLiveData.postValue(repos)
                }

                override fun onError(error: String) {
                    errorLiveData.postValue(error)
                }

            })
    }
}