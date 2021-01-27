package ru.educationalwork.movies

import android.app.Application
import com.google.gson.Gson
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.educationalwork.movies.domain.MoviesListInteractor
import ru.educationalwork.movies.repository.database.MoviesRepository
import ru.educationalwork.movies.repository.database.db
import ru.educationalwork.movies.repository.server.ApiService
import java.util.concurrent.Executors


class App : Application() {

    private val moviesRepository = MoviesRepository()
    lateinit var apiService: ApiService
    lateinit var moviesInteractor: MoviesListInteractor

    override fun onCreate() {
        super.onCreate()

        instance = this

        initRetrofit()
        initMoviesListInteractor()
        //initDb()

        RxJavaPlugins.setErrorHandler { throwable: Throwable? -> } // nothing or some logging

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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        // создаём реализацию
        apiService = retrofit.create(ApiService::class.java)

    }

/*
    fun initDb() {
        // Получаем данные в отдельном потоке
        Executors.newSingleThreadScheduledExecutor().execute(
            Runnable {
                db.getInstance(this)?.getMovieDao()?.getAllMovies()
            }
        )
    }
*/

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/movie/"
        const val API_KEY = "54006cca47fc8c9aec32e7516a2f4e64"

        var instance: App? = null
            private set
    }

}