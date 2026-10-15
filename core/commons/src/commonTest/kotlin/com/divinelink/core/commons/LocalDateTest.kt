package com.divinelink.core.commons

import com.divinelink.core.commons.extensions.daysFromToday
import com.divinelink.core.commons.extensions.parseOrNull
import com.divinelink.core.fixtures.core.commons.ClockProviderFactory
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import kotlin.test.Test

class LocalDateTest {

  @Test
  fun `test parse local date with yyyy-MM-dd format`() {
    LocalDate.parseOrNull("2022-02-03") shouldBe LocalDate(year = 2022, month = 2, day = 3)
    LocalDate.parseOrNull("2022-12-30") shouldBe LocalDate(year = 2022, month = 12, day = 30)
    LocalDate.parseOrNull("2022-02-28") shouldBe LocalDate(year = 2022, month = 2, day = 28)
  }

  @Test
  fun `test parse local date with invalid format`() {
    LocalDate.parseOrNull("") shouldBe null
    LocalDate.parseOrNull("2022-13-03") shouldBe null
  }

  @Test
  fun `test parse local date with null`() {
    LocalDate.parseOrNull(null) shouldBe null
  }

  @Test
  fun `test local date to string format`() {
    LocalDate(year = 2022, month = 2, day = 28).toString() shouldBe "2022-02-28"
    LocalDate.parseOrNull("2022-12-03").toString() shouldBe "2022-12-03"
  }

  @Test
  fun `test daysFromNow calculates correct within the same year`() {
    val date = LocalDate(year = 2021, month = 12, day = 28)
    val clock = ClockProviderFactory.augustFirst2021()

    date.daysFromToday(clock = clock) shouldBe 149
  }

  @Test
  fun `test daysFromNow calculates correct with different year`() {
    val date = LocalDate(year = 2022, month = 2, day = 28)
    val clock = ClockProviderFactory.augustFirst2021()
    date.daysFromToday(clock = clock) shouldBe 211
  }

  @Test
  fun `test daysFromNow calculates correct with different year with large difference`() {
    val date = LocalDate(year = 2023, month = 12, day = 22)
    val clock = ClockProviderFactory.augustFirst2021()
    date.daysFromToday(clock = clock) shouldBe 873
  }
}
