package ru.educationalwork.movies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.educationalwork.movies.repository.model.MovieModel

class FavoriteListViewModel : ViewModel(){
    private val moviesLiveData = MutableLiveData<List<MovieModel>>()

    val movies: LiveData<List<MovieModel>>
        get() = moviesLiveData

    fun getRepos(){

    }

    // Обработка клика на кнопку
/*    fun onGetDataClick() {
        githubInteractor.getRepos("octocat", object : GithubInteractor.GetRepoCallback {
            override fun onSuccess(repos: List<Repo>) {
                reposLiveData.postValue(repos)
            }

            override fun onError(error: String) {
                errorLiveData.postValue(error)
            }
        })
    }

    fun onRepoSelect(repoUrl: String) {
        selectedRepoUrlLiveData.postValue(repoUrl)
    }*/


}