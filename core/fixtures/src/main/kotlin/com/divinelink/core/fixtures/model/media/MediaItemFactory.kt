package com.divinelink.core.fixtures.model.media

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender

object MediaItemFactory {

  fun FightClub(): MediaItem.Media.Movie = MediaItem.Media.Movie(
    id = 550,
    posterPath = "/jSziioSwPVrOy9Yow3XhWIBDjq1.jpg",
    releaseDate = "1999-10-15",
    name = "Fight Club",
    voteAverage = 8.4,
    voteCount = 30_452,
    overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel " +
      "primal male aggression into a shocking new form of therapy." +
      " Their concept catches on, with underground \"fight clubs\" forming in every town, " +
      "until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
    isFavorite = false,
  )

  fun theOffice(): MediaItem.Media.TV = MediaItem.Media.TV(
    id = 2316,
    overview = "The everyday lives of office employees in the Scranton, " +
      "Pennsylvania branch of the fictional Dunder Mifflin Paper Company.",
    posterPath = "/dg9e5fPRRId8PoBE0F6jl5y85Eu.jpg",
    releaseDate = "2005-03-24",
    name = "The Office",
    voteAverage = 8.6,
    voteCount = 4503,
    isFavorite = false,
  )

  fun theWire() = MediaItem.Media.TV(
    id = 1438,
    overview = "Told from the points of view of both the Baltimore homicide and " +
      "narcotics detectives and their targets, the series captures a " +
      "universe in which the national war on drugs has become a permanent," +
      " self-sustaining bureaucracy, and distinctions between good " +
      "and evil are routinely obliterated.",
    posterPath = "/4lbclFySvugI51fwsyxBTOm4DqK.jpg",
    releaseDate = "2002-06-02",
    name = "The Wire",
    voteAverage = 8.6,
    voteCount = 2358,
    isFavorite = false,
  )

  fun tvAll() = listOf(
    theWire(),
    theOffice(),
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

  /**
   * Migrate to these values for new tests
   */

  fun moviesPagination() = PaginationData<MediaItem.Media>(
    page = 1,
    totalPages = 3,
    totalResults = 60,
    list = MoviesList(),
  )

  fun tvPagination() = PaginationData<MediaItem.Media>(
    page = 1,
    totalPages = 3,
    totalResults = 60,
    list = tvAll(),
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
