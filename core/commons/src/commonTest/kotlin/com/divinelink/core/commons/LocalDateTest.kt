package com.divinelink.core.commons

import com.divinelink.core.commons.extensions.parseOrNull
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
}
