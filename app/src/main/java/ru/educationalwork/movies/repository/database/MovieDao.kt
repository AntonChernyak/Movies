package ru.educationalwork.movies.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable
import ru.educationalwork.movies.repository.model.MovieItem


@Dao
interface MovieDao {

    @Insert
    fun addMovie(movie: MovieItem?)

    @Delete
    fun deleteMovie(movie: MovieItem?)

    @Query("SELECT COUNT(id) FROM favorites_movies_table")
    suspend fun getCount(): Int

    @Query("SELECT * FROM favorites_movies_table")
    fun getAllFavoriteMovies(): Flowable<List<MovieItem>>

}