package ru.educationalwork.movies.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.educationalwork.movies.repository.model.MovieItem

@Database(entities = [MovieItem::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao

}