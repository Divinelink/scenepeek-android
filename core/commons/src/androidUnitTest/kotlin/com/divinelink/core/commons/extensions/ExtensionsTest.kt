package com.divinelink.core.commons.extensions

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class ExtensionsTest {

  @Test
  fun `test round should return 1 2 when value is 1 2345 and decimals is 1`() {
    val value = 1.2345
    val decimals = 1

    val result = value.round(decimals)

    assertThat(result).isEqualTo(1.2)
  }

  @Test
  fun `test round should return 1 3 when value is 1 2545 and decimals is 1`() {
    val value = 1.2545
    val decimals = 1

    val result = value.round(decimals)

    assertThat(result).isEqualTo(1.3)
  }

  @Test
  fun `test round should return 1 23 when value is 1 2345 and decimals is 2`() {
    val value = 1.2345
    val decimals = 2

    val result = value.round(decimals)

    assertThat(result).isEqualTo(1.23)
  }

  @Test
  fun `test isWholeNumber should return true when value is 1 0`() {
    val value = 1.0

    val result = value.isWholeNumber()

    assertThat(result).isTrue()
  }

  @Test
  fun `test isWholeNumber should return false when value is 1 1`() {
    val value = 1.1

    val result = value.isWholeNumber()

    assertThat(result).isFalse()
  }

  @Test
  fun `test toShortString should return 100 when value is 100`() {
    val value = 100
    val result = value.toShortString()

    assertThat(result).isEqualTo("100")
  }

  @Test
  fun `test toShortString should return 1m when value is 1_010_000`() {
    val value = 1_010_000

    val result = value.toShortString()

    assertThat(result).isEqualTo("1m")
  }

  @Test
  fun `test toShortString should return 1 1m when value is 1_120_000`() {
    val value = 1_120_000

    val result = value.toShortString()

    assertThat(result).isEqualTo("1.1m")
  }

  @Test
  fun `test toShortString should return 1 3m when value is 1_320_000`() {
    val value = 1_320_000

    val result = value.toShortString()

    assertThat(result).isEqualTo("1.3m")
  }

  @Test
  fun `test toShortString should return 1 3m when value is 1_350_000`() {
    val value = 1_350_000

    val result = value.toShortString()

    assertThat(result).isEqualTo("1.3m")
  }

  /**
   * 2_992_583.toShortString() → "2.9m"
   * 2_900_000.toShortString() → "2.9m"
   * 2_999_999.toShortString() → "2.9m"
   * 1_000_000.toShortString() → "1m"
   * 1_090_000.toShortString() → "1m"
   */

  @Test
  fun `test toShortString should return 2 9m when value is 2_992_583`() {
    val value = 2_992_583

    val result = value.toShortString()

    assertThat(result).isEqualTo("2.9m")
  }

  @Test
  fun `test toShortString should return 2 9m when value is 2_900_000`() {
    val value = 2_900_000

    val result = value.toShortString()

    assertThat(result).isEqualTo("2.9m")
  }

  @Test
  fun `test toShortString should return 2 9m when value is 2_999_999`() {
    val value = 2_999_999

    val result = value.toShortString()

    assertThat(result).isEqualTo("2.9m")
  }

  @Test
  fun `test toShortString should return 1 3k when value is 1_350`() {
    val value = 1_350

    val result = value.toShortString()

    assertThat(result).isEqualTo("1.3k")
  }

  @Test
  fun `test toShortString should return 999 3k when value is 999_320`() {
    val value = 999_320

    val result = value.toShortString()

    assertThat(result).isEqualTo("999.3k")
  }

  @Test
  fun `test toShortString should return 1 3k when value is 1_320`() {
    val value = 1_320

    val result = value.toShortString()

    assertThat(result).isEqualTo("1.3k")
  }

  @Test
  fun `test toShortString should return 1k when value is 1_010`() {
    val value = 1_010

    val result = value.toShortString()

    assertThat(result).isEqualTo("1k")
  }
}
