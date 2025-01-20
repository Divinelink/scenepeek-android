package com.divinelink.core.network.media.util

import com.divinelink.core.model.media.MediaType
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class BuildUrlTest {

  @Test
  fun `test buildFetchDetailsUrl for TV`() {
    val url = buildFetchDetailsUrl(
      id = 1234,
      media = MediaType.TV,
    )

    assertThat(url).isEqualTo(
      "https://api.themoviedb.org/3/tv/1234?language=en-US&append_to_response=external_ids",
    )
  }

  @Test
  fun `test buildFetchDetailsUrl for MOVIE`() {
    val url = buildFetchDetailsUrl(
      id = 1234,
      media = MediaType.MOVIE,
    )

    assertThat(url).isEqualTo(
      "https://api.themoviedb.org/3/movie/1234?language=en-US&append_to_response=credits",
    )
  }

  @Test
  fun `test buildFindByIdUrl`() {
    val url = buildFindByIdUrl(
      externalId = "tt1234",
    )

    assertThat(url).isEqualTo(
      "https://api.themoviedb.org/3/find/tt1234?external_source=imdb_id",
    )
  }
}
