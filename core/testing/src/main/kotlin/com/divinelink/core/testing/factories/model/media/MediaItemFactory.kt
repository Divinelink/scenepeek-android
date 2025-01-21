package com.divinelink.core.testing.factories.model.media

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender

object MediaItemFactory {

  fun FightClub(): MediaItem.Media.Movie = MediaItem.Media.Movie(
    id = 1123,
    posterPath = "123456",
    releaseDate = "2022",
    name = "Flight Club",
    voteAverage = 7.3,
    voteCount = 123_456,
    overview = "This movie is good.",
    isFavorite = false,
  )

  fun TV(): MediaItem.Media.TV = MediaItem.Media.TV(
    id = 1,
    posterPath = "tv - posterPath",
    releaseDate = "tv - releaseDate",
    name = "tv - name",
    voteAverage = 10.0,
    voteCount = 12_345,
    overview = "overview",
    isFavorite = false,
  )

  fun Person() = MediaItem.Person(
    id = 1215572,
    name = "Randall Einhorn",
    profilePath = null,
    knownForDepartment = "Directing",
    gender = Gender.MALE,
  )

  fun MoviesList(range: IntProgression = 1..10): List<MediaItem.Media.Movie> = range.map {
    MediaItem.Media.Movie(
      id = it,
      posterPath = "movie $it - posterPath",
      releaseDate = "movie $it - releaseDate",
      name = "movie $it - name",
      voteAverage = it.toDouble() + 0.7,
      voteCount = 12_345 + it,
      overview = "overview $it",
      isFavorite = false,
    )
  }

  fun TVList(range: IntProgression = 1..10): List<MediaItem.Media.TV> = range.map {
    MediaItem.Media.TV(
      id = it,
      posterPath = "tv $it - posterPath",
      releaseDate = "tv $it - releaseDate",
      name = "tv $it  - name",
      voteAverage = it.toDouble(),
      voteCount = 12_345 + it,
      overview = "overview $it",
      isFavorite = false,
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

    fun withVoteAverage(rating: Double) = apply {
      mediaItem = mediaItem.copy(voteAverage = rating)
    }

    fun withVoteCount(voteCount: Int) = apply {
      mediaItem = mediaItem.copy(voteCount = voteCount)
    }

    fun withOverview(overview: String) = apply {
      mediaItem = mediaItem.copy(overview = overview)
    }

    fun withFavorite(isFavorite: Boolean?) = apply {
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

    fun withVoteAverage(rating: Double) = apply {
      mediaItem = mediaItem.copy(voteAverage = rating)
    }

    fun withVoteCount(voteCount: Int) = apply {
      mediaItem = mediaItem.copy(voteCount = voteCount)
    }

    fun withOverview(overview: String) = apply {
      mediaItem = mediaItem.copy(overview = overview)
    }

    fun withFavorite(isFavorite: Boolean) = apply {
      mediaItem = mediaItem.copy(isFavorite = isFavorite)
    }

    fun build(): MediaItem.Media.TV = mediaItem
  }

  fun MediaItem.Media.TV.toWizard(block: TVMediaItemFactoryWizard.() -> Unit) =
    TVMediaItemFactoryWizard(this).apply(block).build()

  fun MediaItem.Media.Movie.toWizard(block: MovieMediaItemFactoryWizard.() -> Unit) =
    MovieMediaItemFactoryWizard(this).apply(block).build()
}
