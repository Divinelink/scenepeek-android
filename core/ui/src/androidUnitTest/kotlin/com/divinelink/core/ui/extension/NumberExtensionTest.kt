package com.divinelink.core.ui.extension

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class NumberExtensionTest : ComposeTest() {

  @Test
  fun `getColorRating should return onSurface color when value is null`() {
    val value: Double? = null

    setContentWithTheme {
      val result = value.getColorRating()
      assertThat(result).isEqualTo(MaterialTheme.colorScheme.onSurface)
    }
  }

  @Test
  fun `getColorRating should return red color when value is between 0 1 and 3 5`() {
    val value = 1.0

    setContentWithTheme {
      val result = value.getColorRating()
      assertThat(result).isEqualTo(Color(0xFFDB2360))
    }
  }

  @Test
  fun `getColorRating should return yellow color when value is between 3 5 and 6 99`() {
    val value = 4.0

    setContentWithTheme {
      val result = value.getColorRating()
      assertThat(result).isEqualTo(Color(0xFFD2D531))
    }
  }

  @Test
  fun `getColorRating should return green color when value is between 7 0 and 10 0`() {
    val value = 8.0

    setContentWithTheme {
      val result = value.getColorRating()
      assertThat(result).isEqualTo(Color(0xFF21D07A))
    }
  }
}
