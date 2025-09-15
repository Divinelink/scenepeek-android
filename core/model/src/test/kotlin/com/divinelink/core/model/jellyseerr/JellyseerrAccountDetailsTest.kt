package com.divinelink.core.model.jellyseerr

import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class JellyseerrAccountDetailsTest {

  @Test
  fun `test formattedCreatedAt with unparsable date`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin().copy(
      createdAt = "2021-01-01T00:00:00Z",
    )

    assertThat(jellyseerrAccountDetails.formattedCreatedAt).isEqualTo(
      "2021-01-01T00:00:00Z",
    )
  }

  @Test
  fun `test formattedCreatedAt with correct date`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()

    assertThat(jellyseerrAccountDetails.formattedCreatedAt).isEqualTo(
      "August 19, 2023",
    )
  }
}
