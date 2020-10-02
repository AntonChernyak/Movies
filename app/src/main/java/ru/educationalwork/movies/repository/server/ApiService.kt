package ru.educationalwork.movies.repository.server

import retrofit2.Call
import retrofit2.http.GET
import ru.educationalwork.movies.repository.model.MovieModel

interface ApiService {

    // Получить список фильмов с сервера
    @GET("films")
    fun getMoviesList() : Call<List<MovieModel>>

}