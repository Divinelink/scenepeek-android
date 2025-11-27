package com.divinelink.core.ui.extension

import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.model.jellyseerr.createdAtLocalDateTime
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DateExtensionTest : ComposeTest() {

  @Test
  fun `test localise iso date`() = uiTest {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()

    setContentWithTheme {
      jellyseerrAccountDetails.createdAtLocalDateTime.localizeIsoDate() shouldBe "August 19, 2023"
    }
  }
}
