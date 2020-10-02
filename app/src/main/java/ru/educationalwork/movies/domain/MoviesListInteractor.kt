package ru.educationalwork.movies.domain

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.educationalwork.movies.repository.IntMoviesRepository
import ru.educationalwork.movies.repository.model.MovieModel
import ru.educationalwork.movies.repository.server.ApiService

class MoviesListInteractor(private val apiService: ApiService, private val repository: IntMoviesRepository) {

    fun getMoviesList(callback: GetMovieCallback){
        apiService.getMoviesList().enqueue(object : Callback<List<MovieModel>>{
            // если нет коннекта с сервером
            override fun onFailure(call: Call<List<MovieModel>>, t: Throwable) {
                callback.onError("Error. Unable to connect to server")
            }

            override fun onResponse(
                call: Call<List<MovieModel>>,
                response: Response<List<MovieModel>>
            ) {
                if (response.isSuccessful) {
                    // callBack'и, которые мы вернём во ViewModel. Возвращаем список MovieModel
                    response.body()?.let { callback.onSuccess(it) }
                } else {
                    // Вернём код серверной ошибки
                    callback.onError(response.code().toString() + "")
                }
            }

        })

    }

    interface GetMovieCallback {
        fun onSuccess(repos: List<MovieModel>)
        fun onError(error: String)
    }
}