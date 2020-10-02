package ru.educationalwork.movies.repository.model

import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("poster_path") val posterPath : String,
    @SerializedName("description") val description : String,
    @SerializedName("title") val title : String,
    @SerializedName("isFavorite") val isFavorite : Boolean,
    @SerializedName("isClick") val isClick : Boolean
)