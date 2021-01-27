package ru.educationalwork.movies.repository.database

import android.content.Context
import android.util.Log
import androidx.room.Room

object db {

    private var INSTANCE: MovieDatabase? = null

    fun getInstance(context: Context): MovieDatabase? {
        // DoubleCheck проверка, что БД в INSTANCE создаётся
        Log.d("TAGGGG","getInstance = $INSTANCE")
        if (INSTANCE == null) {
            synchronized(MovieDatabase::class) {

                INSTANCE = Room.databaseBuilder(
                    context,
                    MovieDatabase::class.java, "movies.db"
                )
                    /*.allowMainThreadQueries()*/
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
        return INSTANCE
    }

    fun destroyInstance() {
        INSTANCE?.close()
        INSTANCE = null
    }
}