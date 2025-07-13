package com.divinelink.core.model.details

import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.model.details.rating.RatingSource
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class MediaDetailsTest {

  @Test
  fun `test movie share url is correct`() {
    val mediaDetails = MediaDetailsFactory.FightClub().copy(
      title = "Godzilla x Kong: The New Empire",
    )

    val shareUrl = mediaDetails.externalUrl()

    assertThat(
      shareUrl,
    ).isEqualTo("https://www.themoviedb.org/movie/550-godzilla-x-kong-the-new-empire")
  }

  @Test
  fun `test tv show share url is correct`() {
    val mediaDetails = MediaDetailsFactory.TheOffice().copy(
      title = "The Office: The New Empire",
    )

    val shareUrl = mediaDetails.externalUrl()

    assertThat(shareUrl).isEqualTo("https://www.themoviedb.org/tv/2316-the-office-the-new-empire")
  }

  @Test
  fun `test externalUrl for IMDb with valid imdbId`() {
    val mediaDetails = MediaDetailsFactory.FightClub().copy(
      imdbId = "tt0137523",
    )

    val externalUrl = mediaDetails.externalUrl(source = RatingSource.IMDB)

    assertThat(externalUrl).isEqualTo("https://www.imdb.com/title/tt0137523")
  }

  @Test
  fun `test externalUrl for IMDb with invalid imdbId`() {
    val mediaDetails = MediaDetailsFactory.FightClub().copy(
      imdbId = null,
    )

    val externalUrl = mediaDetails.externalUrl(source = RatingSource.IMDB)

    assertThat(externalUrl).isNull()
  }

  @Test
  fun `test externalUrl for Trakt for movie with valid imdbId`() {
    val mediaDetails = MediaDetailsFactory.FightClub().copy(
      imdbId = "tt0137523",
    )

    val externalUrl = mediaDetails.externalUrl(source = RatingSource.TRAKT)

    assertThat(externalUrl).isEqualTo("https://trakt.tv/movies/tt0137523")
  }

  @Test
  fun `test externalUrl for Trakt for movie with invalid imdbId`() {
    val mediaDetails = MediaDetailsFactory.FightClub().copy(
      imdbId = null,
    )

    val externalUrl = mediaDetails.externalUrl(source = RatingSource.TRAKT)

    assertThat(externalUrl).isNull()
  }

  @Test
  fun `test externalUrl for Trakt for tv show with valid imdbId`() {
    val mediaDetails = MediaDetailsFactory.TheOffice().copy(
      imdbId = "tt0386676",
    )

    val externalUrl = mediaDetails.externalUrl(source = RatingSource.TRAKT)

    assertThat(externalUrl).isEqualTo("https://trakt.tv/shows/tt0386676")
  }
}
