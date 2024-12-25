package com.divinelink.core.model.details

import com.divinelink.core.model.details.rating.RatingCount
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
  abstract val ratingCount: RatingCount
  abstract val genres: List<String>?
  abstract val isFavorite: Boolean
  abstract val imdbId: String?

  fun copy(
    id: Int = this.id,
    title: String = this.title,
    posterPath: String = this.posterPath,
    overview: String? = this.overview,
    releaseDate: String = this.releaseDate,
    genres: List<String>? = this.genres,
    isFavorite: Boolean = this.isFavorite,
    ratingCount: RatingCount = this.ratingCount,
    imdbId: String? = this.imdbId,
  ): MediaDetails = when (this) {
    is Movie -> Movie(
      id = id,
      title = title,
      posterPath = posterPath,
      overview = overview,
      director = director,
      releaseDate = releaseDate,
      ratingCount = ratingCount,
      isFavorite = isFavorite,
      genres = genres,
      cast = cast,
      runtime = runtime,
      imdbId = imdbId,
    )
    is TV -> TV(
      id = id,
      title = title,
      posterPath = posterPath,
      overview = overview,
      releaseDate = releaseDate,
      ratingCount = ratingCount,
      isFavorite = isFavorite,
      genres = genres,
      seasons = seasons,
      creators = creators,
      numberOfSeasons = numberOfSeasons,
      imdbId = imdbId,
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
