package com.divinelink.core.model.details

import com.divinelink.core.model.media.MediaType
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class MediaTypeTest {

  @Test
  fun `test isMedia is false for person`() {
    assertThat(MediaType.isMedia(MediaType.PERSON.value)).isFalse()
  }

  @Test
  fun `test isMedia is false for unknown`() {
    assertThat(MediaType.isMedia(MediaType.UNKNOWN.value)).isFalse()
  }

  @Test
  fun `test isMedia is true for tv`() {
    assertThat(MediaType.isMedia(MediaType.TV.value)).isTrue()
  }

  @Test
  fun `test isMedia is true for movie`() {
    assertThat(MediaType.isMedia(MediaType.MOVIE.value)).isTrue()
  }
}
