package com.divinelink.core.network.trakt.util

import com.divinelink.core.model.media.MediaType
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TraktBuildUrlTest {

  @Test
  fun `buildTraktRatingUrl should return the correct url for movies`() {
    val mediaType = MediaType.MOVIE
    val imdbId = "tt1234567"
    val expected = "https://api.trakt.tv/movies/tt1234567/ratings"
    val actual = buildTraktRatingUrl(mediaType, imdbId)
    assertThat(expected).isEqualTo(actual)
  }

  @Test
  fun `buildTraktRatingUrl should return the correct url for tv shows`() {
    val mediaType = MediaType.TV
    val imdbId = "tt7654321"
    val expected = "https://api.trakt.tv/shows/tt7654321/ratings"
    val actual = buildTraktRatingUrl(mediaType, imdbId)
    assertThat(expected).isEqualTo(actual)
  }
}
