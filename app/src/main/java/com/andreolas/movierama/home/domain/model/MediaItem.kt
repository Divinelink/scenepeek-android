package com.andreolas.movierama.home.domain.model

sealed class MediaItem(
  open val id: Int,
  open val name: String,
  open val posterPath: String?,
) {

  sealed class Media(
    override val id: Int,
    override val name: String,
    override val posterPath: String?,
    open val releaseDate: String,
    open val rating: String,
    open val overview: String,
    open val isFavorite: Boolean,
  ) : MediaItem(
    id = id,
    posterPath = posterPath,
    name = name
  ) {

    data class TV(
      override val id: Int,
      override val name: String,
      override val posterPath: String?,
      override val releaseDate: String,
      override val rating: String,
      override val overview: String,
      override val isFavorite: Boolean,
    ) : Media(
      id = id,
      posterPath = posterPath,
      name = name,
      releaseDate = releaseDate,
      rating = rating,
      overview = overview,
      isFavorite = isFavorite
    )

    data class Movie(
      override val id: Int,
      override val name: String,
      override val posterPath: String?,
      override val releaseDate: String,
      override val rating: String,
      override val overview: String,
      override val isFavorite: Boolean,
    ) : Media(
      id = id,
      posterPath = posterPath,
      name = name,
      releaseDate = releaseDate,
      rating = rating,
      overview = overview,
      isFavorite = isFavorite
    )
  }

  data class Person(
    override val id: Int,
    override val name: String,
    override val posterPath: String,
  ) : MediaItem(
    id = id,
    posterPath = name,
    name = posterPath
  )

  object Unknown : MediaItem(
    id = -1,
    posterPath = null,
    name = ""
  )
}
