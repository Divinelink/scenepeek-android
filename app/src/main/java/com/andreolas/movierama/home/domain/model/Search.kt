package com.andreolas.movierama.home.domain.model

sealed class Search(
  open val id: Int,
  open val name: String,
  open val posterPath: String?,
) {

  data class TV(
    override val id: Int,
    override val posterPath: String?,
    override val name: String,
    val releaseDate: String,
    val rating: String,
    val overview: String,
    val isFavorite: Boolean,
  ) : Search(
    id = id,
    posterPath = posterPath,
    name = name,
  )

  data class Movie(
    override val id: Int,
    override val name: String,
    override val posterPath: String?,
    val releaseDate: String,
    val rating: String,
    val overview: String,
    val isFavorite: Boolean,
  ) : Search(
    id = id,
    posterPath = posterPath,
    name = name,
  )

  data class Person(
    override val id: Int,
    override val name: String,
    override val posterPath: String,
  ) : Search(
    id = id,
    posterPath = name,
    name = posterPath
  )

  object Unknown : Search(
    id = -1,
    posterPath = null,
    name = ""
  )
}
