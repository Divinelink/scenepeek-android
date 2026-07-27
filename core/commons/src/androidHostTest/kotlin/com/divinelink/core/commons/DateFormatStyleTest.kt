package com.divinelink.core.commons

import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.model.jellyseerr.createdAtLocalDate
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class DateExtensionTest {

  @Test
  @Config(qualifiers = "+it")
  fun `test formatLocalized all styles with italian Locale`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDate

    createdAt?.formatLocalized(style = DateFormatStyle.FULL) shouldBe "sabato 19 agosto 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.LONG) shouldBe "19 agosto 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.MEDIUM) shouldBe "19 ago 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.SHORT) shouldBe "19/08/23"
  }

  @Test
  @Config(qualifiers = "+en")
  fun `test formatLocalized all styles with english Locale`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDate

    createdAt?.formatLocalized(style = DateFormatStyle.FULL) shouldBe "Saturday, August 19, 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.LONG) shouldBe "August 19, 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.MEDIUM) shouldBe "Aug 19, 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.SHORT) shouldBe "8/19/23"
  }

  @Test
  @Config(qualifiers = "+gb")
  fun `test formatLocalized all styles with GB Locale`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDate

    createdAt?.formatLocalized(style = DateFormatStyle.FULL) shouldBe "2023 Aug 19, Sat"
    createdAt?.formatLocalized(style = DateFormatStyle.LONG) shouldBe "2023 Aug 19"
    createdAt?.formatLocalized(style = DateFormatStyle.MEDIUM) shouldBe "2023 Aug 19"
    createdAt?.formatLocalized(style = DateFormatStyle.SHORT) shouldBe "2023-08-19"
  }

  @Test
  @Config(qualifiers = "+de")
  fun `test formatLocalized all styles with german Locale`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDate

    createdAt?.formatLocalized(style = DateFormatStyle.FULL) shouldBe "Samstag, 19. August 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.LONG) shouldBe "19. August 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.MEDIUM) shouldBe "19.08.2023"
    createdAt?.formatLocalized(style = DateFormatStyle.SHORT) shouldBe "19.08.23"
  }

  @Test
  @Config(qualifiers = "+el")
  fun `test formatLocalized all styles with greek Locale`() {
    val jellyseerrAccountDetails = JellyseerrProfileFactory.jellyfin()
    val createdAt = jellyseerrAccountDetails.createdAtLocalDate

    createdAt?.formatLocalized(style = DateFormatStyle.FULL) shouldBe "Σάββατο 19 Αυγούστου 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.LONG) shouldBe "19 Αυγούστου 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.MEDIUM) shouldBe "19 Αυγ 2023"
    createdAt?.formatLocalized(style = DateFormatStyle.SHORT) shouldBe "19/8/23"
  }
}
