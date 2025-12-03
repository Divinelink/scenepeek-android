package com.divinelink.core.ui.extension

import com.divinelink.core.commons.extensions.toLocalDate
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.model.jellyseerr.createdAtLocalDateTime
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DateExtensionTest : ComposeTest() {

  @Test
  fun `test localise LocalDateTime with valid iso date`() = uiTest {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDateTime

    setContentWithTheme {
      createdAt.localizeIsoDate(useLong = true) shouldBe "August 19, 2023"
      createdAt.localizeIsoDate(useLong = false) shouldBe "Aug 19, 2023"
    }
  }

  @Test
  fun `test localise LocalDate with valid iso date`() = uiTest {
    val mediaDetails = MediaDetailsFactory.FightClub()
    val releaseDate = mediaDetails.releaseDate.toLocalDate()

    setContentWithTheme {
      releaseDate.localizeMonthYear(useLong = true) shouldBe "October 1999"
      releaseDate.localizeMonthYear(useLong = false) shouldBe "Oct 1999"
    }
  }
}
