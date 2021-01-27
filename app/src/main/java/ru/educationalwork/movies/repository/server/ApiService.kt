package ru.educationalwork.movies.repository.server

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.educationalwork.movies.repository.model.MovieListResponse

interface ApiService {

    // Получить список фильмов с сервера
    @GET("top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String, @Query("language") language: String): Single<MovieListResponse>

    @GET("popular")
    fun getPopularMovies(@Query("api_key") apiKey: String, @Query("language") language: String): Single<MovieListResponse>



/*
    // Получить список фильмов с сервера
    @GET("top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String, @Query("language") language: String): List<MovieListResponse>
*/




/*    @GET("top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MovieResponse>



    @GET("popular")
    fun getPopularMovies(@Query("api_key") apiKey: String, @Query("language") language: String): Call<MovieResponse>*/

}