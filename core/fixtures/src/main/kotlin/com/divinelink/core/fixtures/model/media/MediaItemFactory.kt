package com.divinelink.core.fixtures.model.media

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
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

  fun theOffice(): MediaItem.Media.TV = MediaItem.Media.TV(
    id = 2316,
    posterPath = "the_office.jpg",
    releaseDate = "2005-03-24",
    name = "The Office",
    voteAverage = 9.5,
    voteCount = 12_345,
    overview = "Michael Scarn is thee best.",
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
      releaseDate = "2002-08-22",
      name = "Fight club $it",
      voteAverage = (it.toDouble() + 0.5) % 10,
      voteCount = 12_345 + it,
      overview = LoremIpsum(15).values.joinToString(),
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
