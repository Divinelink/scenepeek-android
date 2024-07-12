package com.divinelink.core.commons.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringExtensionsTest {

  @Test
  fun `test extractDetailsFromDeeplink with valid url`() {
    val url = "https://www.themoviedb.org/tv/693134-dune-part-two"

    val result = url.extractDetailsFromDeepLink()

    val expected = Pair(693134, "tv")
    assertThat(expected).isEqualTo(result)
  }

  @Test
  fun `test extractDetailsFromDeeplink with invalid url`() {
    val url = "https://www.themoviedb.org/tv/"

    val result = url.extractDetailsFromDeepLink()

    assertThat(result).isNull()
  }
}
