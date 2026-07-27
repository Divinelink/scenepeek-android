package com.divinelink.core.model.jellyseerr

import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import kotlin.test.Test

class JellyseerrProfileTest {

  @Test
  fun `test formattedCreatedAt with unparsable date`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin().copy(
      createdAt = "2021-01-01T00:00:00Z",
    )

    jellyseerrAccountDetails.createdAtLocalDate.toString() shouldBe "2021-01-01"
  }

  @Test
  fun `test formattedCreatedAt with correct date`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()

    jellyseerrAccountDetails.createdAtLocalDate shouldBe LocalDate.parse("2023-08-19")
  }
}
