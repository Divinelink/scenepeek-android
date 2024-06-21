package com.andreolas.movierama.details.ui

import com.andreolas.factories.MediaDetailsFactory
import com.divinelink.core.model.media.MediaType
import com.divinelink.feature.details.ui.DetailsViewState
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DetailsViewStateTest {

  @Test
  fun `test movie share url is correct`() {
    val mediaType = MediaType.MOVIE
    val movieId = 1
    val mediaDetails = MediaDetailsFactory.FightClub().copy(
      title = "Godzilla x Kong: The New Empire",
    )

    val viewState = DetailsViewState(
      mediaType = mediaType,
      mediaId = movieId,
      mediaDetails = mediaDetails,
    )

    val shareUrl = viewState.shareUrl

    assertThat(shareUrl).isEqualTo("https://themoviedb.org/movie/1-godzilla-x-kong-the-new-empire")
  }

  @Test
  fun `test tv show share url is correct`() {
    val mediaType = MediaType.TV
    val movieId = 1
    val mediaDetails = MediaDetailsFactory.TheOffice().copy(
      title = "The Office: The New Empire",
    )

    val viewState = DetailsViewState(
      mediaType = mediaType,
      mediaId = movieId,
      mediaDetails = mediaDetails,
    )

    val shareUrl = viewState.shareUrl

    assertThat(shareUrl).isEqualTo("https://themoviedb.org/tv/1-the-office-the-new-empire")
  }
}
