package com.andreolas.movierama.home.domain.model

sealed class Search {

  data class TV(
    val id: Int,
    val posterPath: String?,
    val releaseDate: String,
    val name: String,
    val rating: String,
    val overview: String,
    val isFavorite: Boolean,
  ) : Search()

  data class Movie(
    val id: Int,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val rating: String,
    val overview: String,
    val isFavorite: Boolean,
  ) : Search()

  data class Person(
    val todo: Any = TODO()
  ) : Search()
}
