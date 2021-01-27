package ru.educationalwork.movies.repository.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse (
    var page: Int,
    var results: List<MovieItem>,
    @SerializedName("total_results")
    var totalResults: Int,
    @SerializedName("total_pages")
    var totalPages: Int
)