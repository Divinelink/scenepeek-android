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

  fun TV(): MediaItem.Media.TV = MediaItem.Media.TV(
    id = 1,
    posterPath = "tv - posterPath",
    releaseDate = "tv - releaseDate",
    name = "tv - name",
    rating = "10",
    overview = "overview",
    isFavorite = false,
  )

  fun MoviesList(
    range: IntProgression = 1..10,
  ): List<MediaItem.Media.Movie> = (range).map {
    MediaItem.Media.Movie(
      id = it,
      posterPath = "movie $it - posterPath",
      releaseDate = "movie $it - releaseDate",
      name = "movie $it  - name",
      rating = it.toString(),
      overview = "overview $it",
      isFavorite = it % 2 == 0,
    )
  }

  fun TVList(): List<MediaItem.Media.TV> = (1..10).map {
    MediaItem.Media.TV(
      id = it,
      posterPath = "tv $it - posterPath",
      releaseDate = "tv $it - releaseDate",
      name = "tv $it  - name",
      rating = it.toString(),
      overview = "overview $it",
      isFavorite = it % 2 == 0,
    )
  }

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

    fun withFavorite(isFavorite: Boolean) = apply {
      mediaItem = mediaItem.copy(isFavorite = isFavorite)
    }

    fun build(): MediaItem.Media.TV = mediaItem
  }

  fun MediaItem.Media.TV.wizard(block: TVMediaItemFactoryWizard.() -> Unit) =
    TVMediaItemFactoryWizard(this).apply(block).build()

  fun MediaItem.Media.Movie.wizard(block: MovieMediaItemFactoryWizard.() -> Unit) =
    MovieMediaItemFactoryWizard(this).apply(block).build()
}
