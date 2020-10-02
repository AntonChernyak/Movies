package ru.educationalwork.movies

import android.app.Application
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.educationalwork.movies.domain.MoviesListInteractor
import ru.educationalwork.movies.repository.MoviesRepository
import ru.educationalwork.movies.repository.server.ApiService

class App : Application() {

    private val moviesRepository = MoviesRepository()
    lateinit var apiService: ApiService
    lateinit var moviesInteractor: MoviesListInteractor

    override fun onCreate() {
        super.onCreate()

        instance = this

        initRetrofit()
        initMoviesListInteractor()
    }

    private fun initMoviesListInteractor() {
        moviesInteractor = MoviesListInteractor(apiService, moviesRepository)
    }

    private fun initRetrofit() {
        // Подключаем интерцептор для логгирования
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // инициализация Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        // создаём реализацию
        apiService = retrofit.create(ApiService::class.java)

    }

    companion object {
        const val BASE_URL = "https://my-json-server.typicode.com/AntonChernyak/JsonHolder/"

        var instance: App? = null
            private set
    }

}