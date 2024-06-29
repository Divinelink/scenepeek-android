package com.divinelink.core.model.details

import com.divinelink.core.testing.factories.model.details.MediaDetailsFactory
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MediaDetailsTest {

  @Test
  fun `test movie share url is correct`() {
    val mediaDetails = MediaDetailsFactory.FightClub().copy(
      title = "Godzilla x Kong: The New Empire",
    )

    val shareUrl = mediaDetails.shareUrl()

    assertThat(
      shareUrl,
    ).isEqualTo("https://themoviedb.org/movie/1123-godzilla-x-kong-the-new-empire")
  }

  @Test
  fun `test tv show share url is correct`() {
    val mediaDetails = MediaDetailsFactory.TheOffice().copy(
      title = "The Office: The New Empire",
    )

    val shareUrl = mediaDetails.shareUrl()

    assertThat(shareUrl).isEqualTo("https://themoviedb.org/tv/2316-the-office-the-new-empire")
  }
}
