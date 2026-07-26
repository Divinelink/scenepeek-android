package com.divinelink.core.commons

import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.model.jellyseerr.createdAtLocalDate
import io.kotest.matchers.shouldBe
import platform.Foundation.NSUserDefaults
import kotlin.test.Test

class DateExtensionTest {

  @Test
  fun `test formatLocalized all styles with italian Locale`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDate

    mockLocale("it")

    createdAt?.formatLocalized(format = DateFormat.MEDIUM_NO_DAY) shouldBe "ago 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.FULL) shouldBe "sabato 19 agosto 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.LONG) shouldBe "19 agosto 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.MEDIUM) shouldBe "19 ago 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.SHORT) shouldBe "19/08/23"
  }

  @Test
  fun `test formatLocalized all styles with english Locale`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDate

    mockLocale("en")

    createdAt?.formatLocalized(style = DateFormatStyle.FULL) shouldBe "Saturday, August 19, 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.LONG) shouldBe "August 19, 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.MEDIUM) shouldBe "Aug 19, 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.SHORT) shouldBe "8/19/23"
  }

  @Test
  fun `test formatLocalized all styles with german Locale`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDate

    mockLocale("de")

    createdAt?.formatLocalized(style = DateFormatStyle.FULL) shouldBe "Samstag, 19. August 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.LONG) shouldBe "19. August 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.MEDIUM) shouldBe "19.08.2023"
    createdAt?.formatLocalized(style = DateFormatStyle.SHORT) shouldBe "19.08.23"
  }

  @Test
  fun `test formatLocalized all styles with greek Locale`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDate

    mockLocale("el")

    createdAt?.formatLocalized(style = DateFormatStyle.FULL) shouldBe "Σάββατο 19 Αυγούστου 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.LONG) shouldBe "19 Αυγούστου 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.MEDIUM) shouldBe "19 Αυγ 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.SHORT) shouldBe "19/8/23"
  }

  private fun mockLocale(code: String) = NSUserDefaults.standardUserDefaults.apply {
    setObject(listOf(code), forKey = "AppleLanguages")
    synchronize()
  }
}
