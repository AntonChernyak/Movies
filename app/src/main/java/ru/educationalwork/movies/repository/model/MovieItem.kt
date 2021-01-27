package ru.educationalwork.movies.repository.model

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorites_movies_table")
class MovieItem(
    posterPath: String? = null,
    @SerializedName("overview")
    @ColumnInfo(name = "movie_overview")
    var overview: String?,
    @PrimaryKey
    @SerializedName("id")
    var id: Int?,
    @SerializedName("title")
    @ColumnInfo(name = "movie_title")
    var title: String?,
    var favoriteStatus: Boolean,
    var onClickStatus: Boolean
) {
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    var posterPath: String? = posterPath
        get() {
            return if (field != null && field!!.startsWith(BASE_POSTER_URL)) field
            else "$BASE_POSTER_URL$field"
        }

    override fun toString(): String {
        return "MovieItem(poster=${posterPath}, overview=$overview, id=$id, title=$title)"
    }

    companion object {
        const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/w500"
    }

}