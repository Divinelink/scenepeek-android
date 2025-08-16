package com.divinelink.core.model.media

import com.divinelink.core.model.person.Gender
import kotlinx.serialization.Serializable

@Serializable
sealed class MediaItem {
  abstract val uniqueIdentifier: String
  abstract val id: Int
  abstract val name: String
  abstract val posterPath: String?
  abstract val backdropPath: String?
  abstract val mediaType: MediaType

  @Serializable
  sealed class Media(override val mediaType: MediaType) : MediaItem() {
    abstract override val id: Int
    abstract override val name: String
    abstract override val posterPath: String?
    abstract override val backdropPath: String?
    abstract val releaseDate: String
    abstract val voteAverage: Double
    abstract val voteCount: Int
    abstract val overview: String
    abstract val isFavorite: Boolean?
    abstract val popularity: Double
    abstract val accountRating: Int?

    @Serializable
    data class TV(
      override val id: Int,
      override val name: String,
      override val posterPath: String?,
      override val backdropPath: String?,
      override val releaseDate: String,
      override val voteAverage: Double,
      override val voteCount: Int,
      override val overview: String,
      override val popularity: Double,
      override val isFavorite: Boolean?,
      override val accountRating: Int? = null,
    ) : Media(
      mediaType = MediaType.TV,
    )

    @Serializable
    data class Movie(
      override val id: Int,
      override val name: String,
      override val posterPath: String?,
      override val backdropPath: String?,
      override val releaseDate: String,
      override val voteAverage: Double,
      override val voteCount: Int,
      override val overview: String,
      override val popularity: Double,
      override val isFavorite: Boolean?,
      override val accountRating: Int? = null,
    ) : Media(
      mediaType = MediaType.MOVIE,
    )

    override val uniqueIdentifier: String
      get() = "${mediaType.value}-$id"
  }

  data class Person(
    override val id: Int,
    override val name: String,
    val profilePath: String?,
    val gender: Gender,
    val knownForDepartment: String?,
  ) : MediaItem() {
    override val posterPath: String? = profilePath
    override val backdropPath: String? = null
    override val mediaType: MediaType = MediaType.PERSON
    override val uniqueIdentifier: String = "person-$id"
  }

  data object Unknown : MediaItem() {
    override val id: Int = -1
    override val name: String = ""
    override val posterPath: String? = null
    override val backdropPath: String? = null
    override val mediaType: MediaType = MediaType.UNKNOWN
    override val uniqueIdentifier: String = "unknown-$id"
  }
}
