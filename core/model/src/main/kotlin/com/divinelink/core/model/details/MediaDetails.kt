package com.divinelink.core.model.details

import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.media.MediaType

/**
 * Represents details or a movie or TV show.
 */
sealed class MediaDetails {
  abstract val id: Int
  abstract val title: String
  abstract val posterPath: String
  abstract val backdropPath: String
  abstract val tagline: String?
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
    backdropPath: String = this.backdropPath,
    tagline: String? = this.tagline,
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
      backdropPath = backdropPath,
      tagline = tagline,
      overview = overview,
      creators = creators,
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
      backdropPath = backdropPath,
      tagline = tagline,
      overview = overview,
      releaseDate = releaseDate,
      ratingCount = ratingCount,
      isFavorite = isFavorite,
      genres = genres,
      seasons = seasons,
      creators = creators,
      numberOfSeasons = numberOfSeasons,
      status = status,
      imdbId = imdbId,
    )
  }
}

fun MediaDetails.externalUrl(source: RatingSource = RatingSource.TMDB): String? {
  val mediaType = if (this is Movie) MediaType.MOVIE else MediaType.TV

  when (source) {
    RatingSource.TMDB -> {
      val urlTitle = title
        .lowercase()
        .replace(":", "")
        .replace(regex = "[\\s|/]".toRegex(), replacement = "-")

      return "${source.url}/${mediaType.value}/$id-$urlTitle"
    }
    RatingSource.IMDB -> {
      val imdbId = imdbId ?: return null

      return "${source.url}/title/$imdbId"
    }
    RatingSource.TRAKT -> {
      val imdbId = imdbId ?: return null

      return source.url + "/${mediaType.traktPath}/" + imdbId
    }
  }
}
