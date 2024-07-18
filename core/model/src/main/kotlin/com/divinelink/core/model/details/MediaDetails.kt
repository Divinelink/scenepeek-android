package com.divinelink.core.model.details

import com.divinelink.core.model.media.MediaType

/**
 * Represents details or a movie or TV show.
 */
sealed class MediaDetails {
  abstract val id: Int
  abstract val title: String
  abstract val posterPath: String
  abstract val overview: String?
  abstract val releaseDate: String
  abstract val rating: String
  abstract val genres: List<String>?
  abstract val isFavorite: Boolean

  fun copy(
    id: Int = this.id,
    title: String = this.title,
    posterPath: String = this.posterPath,
    overview: String? = this.overview,
    releaseDate: String = this.releaseDate,
    genres: List<String>? = this.genres,
    rating: String = this.rating,
    isFavorite: Boolean = this.isFavorite,
  ): MediaDetails = when (this) {
    is Movie -> Movie(
      id = id,
      title = title,
      posterPath = posterPath,
      overview = overview,
      director = director,
      releaseDate = releaseDate,
      rating = rating,
      isFavorite = isFavorite,
      genres = genres,
      cast = cast,
      runtime = runtime,
    )
    is TV -> TV(
      id = id,
      title = title,
      posterPath = posterPath,
      overview = overview,
      credits = credits,
      releaseDate = releaseDate,
      rating = rating,
      isFavorite = isFavorite,
      genres = genres,
      seasons = seasons,
      creators = creators,
      numberOfSeasons = numberOfSeasons,
    )
  }
}

fun MediaDetails.shareUrl(): String {
  val urlTitle = title
    .lowercase()
    .replace(":", "")
    .replace(regex = "[\\s|/]".toRegex(), replacement = "-")

  val mediaType = if (this is Movie) MediaType.MOVIE else MediaType.TV

  return "https://themoviedb.org/${mediaType.value}/$id-$urlTitle"
}
