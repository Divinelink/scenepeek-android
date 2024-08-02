package com.divinelink.core.commons.extensions

import com.divinelink.core.testing.factories.core.commons.ClockFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

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

  @Test
  fun `test calculateAge with valid date`() {
    val birthDate = "1990-01-01"

    val result = calculateAge(
      fromDate = birthDate,
      clock = ClockFactory.augustFirst2021(),
    )

    assertThat(result).isEqualTo(31)
  }

  @Test
  fun `test calculateAge when birthday has not yer accurred`() {
    val birthDate = "1990-12-02"

    val result = calculateAge(
      fromDate = birthDate,
      clock = ClockFactory.augustFirst2021(),
    )

    assertThat(result).isEqualTo(30)
  }

  @Test
  fun `test calculateAge with toDate`() {
    val birthDate = "1990-01-01"
    val toDate = "2015-12-01"

    val result = calculateAge(
      fromDate = birthDate,
      toDate = toDate,
      clock = ClockFactory.augustFirst2021(),
    )

    assertThat(result).isEqualTo(25)
  }
}
