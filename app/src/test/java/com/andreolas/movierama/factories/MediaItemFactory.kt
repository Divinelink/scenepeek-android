package com.andreolas.movierama.factories

import com.andreolas.movierama.home.domain.model.MediaItem

object MediaItemFactory {

  fun Movie(): MediaItem.Media.Movie = MediaItem.Media.Movie(
    id = 1,
    posterPath = "movie - posterPath",
    releaseDate = "movie - releaseDate",
    name = "movie - name",
    rating = "10",
    overview = "overview",
    isFavorite = false,
  )

  fun TV(id: Int): MediaItem.Media.TV = MediaItem.Media.TV(
    id = id,
    posterPath = "tv - posterPath",
    releaseDate = "tv - releaseDate",
    name = "tv - name",
    rating = "10",
    overview = "overview",
    isFavorite = false,
  )

  class MovieMediaItemFactoryWizard(private var mediaItem: MediaItem.Media.Movie) {

    fun withId(id: Int) = apply {
      mediaItem = mediaItem.copy(id = id)
    }

    fun withName(name: String) = apply {
      mediaItem = mediaItem.copy(name = name)
    }

    fun withPosterPath(posterPath: String?) = apply {
      mediaItem = mediaItem.copy(posterPath = posterPath)
    }

    fun withReleaseDate(releaseDate: String) = apply {
      mediaItem = mediaItem.copy(releaseDate = releaseDate)
    }

    fun withRating(rating: String) = apply {
      mediaItem = mediaItem.copy(rating = rating)
    }

    fun withOverview(overview: String) = apply {
      mediaItem = mediaItem.copy(overview = overview)
    }

    fun withFavorite(isFavorite: Boolean) = apply {
      mediaItem = mediaItem.copy(isFavorite = isFavorite)
    }

    fun build(): MediaItem.Media.Movie = mediaItem
  }

  class TVMediaItemFactoryWizard(private var mediaItem: MediaItem.Media.TV) {

    fun withId(id: Int) = apply {
      mediaItem = mediaItem.copy(id = id)
    }

    fun withName(name: String) = apply {
      mediaItem = mediaItem.copy(name = name)
    }

    fun withPosterPath(posterPath: String?) = apply {
      mediaItem = mediaItem.copy(posterPath = posterPath)
    }

    fun withReleaseDate(releaseDate: String) = apply {
      mediaItem = mediaItem.copy(releaseDate = releaseDate)
    }

    fun withRating(rating: String) = apply {
      mediaItem = mediaItem.copy(rating = rating)
    }

    fun withOverview(overview: String) = apply {
      mediaItem = mediaItem.copy(overview = overview)
    }

    fun withIsFavorite(isFavorite: Boolean) = apply {
      mediaItem = mediaItem.copy(isFavorite = isFavorite)
    }

    fun build(): MediaItem.Media.TV = mediaItem
  }

  fun MediaItem.Media.TV.wizard(block: TVMediaItemFactoryWizard.() -> Unit) =
    TVMediaItemFactoryWizard(this).apply(block).build()

  fun MediaItem.Media.Movie.wizard(block: MovieMediaItemFactoryWizard.() -> Unit) =
    MovieMediaItemFactoryWizard(this).apply(block).build()
}
