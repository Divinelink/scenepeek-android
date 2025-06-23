package com.divinelink.core.commons.extensions

import com.divinelink.core.commons.Constants
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class StringExtensionsTest {

  @Test
  fun `test formatTo with valid date`() {
    val date = "2023-08-19T00:00:00.000Z"

    val result = date.formatTo(
      inputFormat = Constants.ISO_8601,
      outputFormat = "MMMM dd, yyyy",
    )

    assertThat(result).isEqualTo("August 19, 2023")
  }

  @Test
  fun `test formatTo with unparsable date`() {
    val date = "2023-08-19T00:00:00"

    val result = date.formatTo(
      inputFormat = Constants.ISO_8601,
      outputFormat = "MMMM dd, yyyy",
    )

    assertThat(result).isNull()
  }

  @Test
  fun `test localizeIsoDate with valid iso date`() {
    val date = "2025-06-22T13:00:22.000Z"

    val result = date.localizeIsoDate()

    assertThat(result).isEqualTo("June 22, 2025")
  }

  @Test
  fun `test localizeIsoDate with invalid iso date`() {
    val date = "2025-06-22T13:00:22"

    val result = date.localizeIsoDate()

    assertThat(result).isEqualTo("2025-06-22T13:00:22")
  }

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

  @Test
  fun `test calculateFourteenDayRange`() {
    val timestamp = ClockFactory.augustFirst2021().now().epochSeconds.toString()

    val result = timestamp.calculateFourteenDayRange(
      clock = ClockFactory.septemberSecond2021(), // 2021-09-02
    )

    val expected = listOf(
      Pair("2021-08-01", "2021-08-15"),
      Pair("2021-08-16", "2021-08-30"),
      Pair("2021-08-31", "2021-09-02"),
    )
    assertThat(result).isEqualTo(expected)
  }

  @Test
  fun `test isDayToday when is the same day`() {
    val result = "2021-08-01".isDateToday(
      clock = ClockFactory.augustFirst2021(),
    )

    assertThat(result).isTrue()
  }

  @Test
  fun `test isDayToday when is not the same day`() {
    val result = "2021-08-01".isDateToday(
      clock = ClockFactory.augustFifteenth2021(), // 2021-08-15
    )

    assertThat(result).isFalse()
  }

  @Test
  fun `test isInstantToday when is the same day`() {
    val timestamp = ClockFactory.augustFirst2021().now().epochSeconds.toString()

    val result = timestamp.isInstantToday(
      clock = ClockFactory.augustFirst2021(),
    )

    assertThat(result).isTrue()
  }

  @Test
  fun `test isInstantToday when is not the same day`() {
    val timestamp = ClockFactory.augustFirst2021().now().epochSeconds.toString()

    val result = timestamp.isInstantToday(
      clock = ClockFactory.augustFifteenth2021(), // 2021-08-15
    )

    assertThat(result).isFalse()
  }

  @Test
  fun `test isValidEmail`() {
    val testCases = listOf(
      // Valid emails
      "test.email@example.com" to true,
      "test.email@sub.example.com" to true,
      "test.email+123@example.com" to true,
      "user123@example123.com" to true,
      "test..email@example.com" to true,
      "a@b.com" to true, // Edge case: very short valid email

      // Invalid emails
      "test.email.example.com" to false, // missing '@'
      "test.email@" to false, // missing domain
      "@example.com" to false, // missing local part
      "test email@example.com" to false, // space in email
      "test.email@example" to false, // missing top-level domain
      "test@invalid@example.com" to false, // multiple '@' characters
      "test.email!@example.com" to false, // special characters not allowed
      "" to false, // empty string
    )

    for ((email, expected) in testCases) {
      assertThat(email.isValidEmail()).isEqualTo(expected)
    }
  }
}
