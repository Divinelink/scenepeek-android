package com.divinelink.core.network.omdb.util

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class BuildUrlOMDbTest {

  @Test
  fun `test buildUrlOMDb`() {
    val result = buildOMDbUrl(imdbId = "tt123456", apikey = "z15b2fuh")
    val expected = "https://omdbapi.com/?i=tt123456&apikey=z15b2fuh"

    assertThat(result).isEqualTo(expected)
  }
}
