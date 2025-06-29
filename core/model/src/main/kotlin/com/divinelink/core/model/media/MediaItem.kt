package com.divinelink.core.model.media

import com.divinelink.core.model.person.Gender

sealed class MediaItem(
  open val id: Int,
  open val name: String,
  open val posterPath: String?,
  open val mediaType: MediaType,
) {

  sealed class Media(
    override val id: Int,
    override val name: String,
    override val posterPath: String?,
    open val releaseDate: String,
    open val voteAverage: Double,
    open val voteCount: Int,
    open val overview: String,
    open val isFavorite: Boolean?,
    override val mediaType: MediaType,
    open val accountRating: Int?,
  ) : MediaItem(
    id = id,
    posterPath = posterPath,
    name = name,
    mediaType = mediaType,
  ) {

    data class TV(
      override val id: Int,
      override val name: String,
      override val posterPath: String?,
      override val releaseDate: String,
      override val voteAverage: Double,
      override val voteCount: Int,
      override val overview: String,
      override val isFavorite: Boolean?,
      override val accountRating: Int? = null,
    ) : Media(
      id = id,
      posterPath = posterPath,
      name = name,
      releaseDate = releaseDate,
      voteAverage = voteAverage,
      voteCount = voteCount,
      overview = overview,
      isFavorite = isFavorite,
      mediaType = MediaType.TV,
      accountRating = accountRating,
    )

    data class Movie(
      override val id: Int,
      override val name: String,
      override val posterPath: String?,
      override val releaseDate: String,
      override val voteAverage: Double,
      override val voteCount: Int,
      override val overview: String,
      override val isFavorite: Boolean?,
      override val accountRating: Int? = null,
    ) : Media(
      id = id,
      posterPath = posterPath,
      name = name,
      releaseDate = releaseDate,
      voteAverage = voteAverage,
      voteCount = voteCount,
      overview = overview,
      isFavorite = isFavorite == true,
      mediaType = MediaType.MOVIE,
      accountRating = accountRating,
    )
  }

  data class Person(
    override val id: Int,
    override val name: String,
    val profilePath: String?,
    val gender: Gender,
    val knownForDepartment: String?,
  ) : MediaItem(
    id = id,
    name = name,
    posterPath = profilePath,
    mediaType = MediaType.PERSON,
  )

  data object Unknown : MediaItem(
    id = -1,
    posterPath = null,
    name = "",
    mediaType = MediaType.UNKNOWN,
  )
}
