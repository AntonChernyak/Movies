package ru.educationalwork.movies.all_movies_recycler

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieItem(
    val title: String,
    val description: String,
    val poster: Int,
    var isClick: Boolean = false,
    var isFavorite: Boolean = false,
    var originalPosition: Int = 0
) : Parcelable