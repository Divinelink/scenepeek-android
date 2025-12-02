package com.divinelink.core.model.locale

import com.divinelink.core.model.Res
import com.divinelink.core.model.month_april
import com.divinelink.core.model.month_april_short
import com.divinelink.core.model.month_august
import com.divinelink.core.model.month_august_short
import com.divinelink.core.model.month_december
import com.divinelink.core.model.month_december_short
import com.divinelink.core.model.month_february
import com.divinelink.core.model.month_february_short
import com.divinelink.core.model.month_january
import com.divinelink.core.model.month_january_short
import com.divinelink.core.model.month_july
import com.divinelink.core.model.month_july_short
import com.divinelink.core.model.month_june
import com.divinelink.core.model.month_june_short
import com.divinelink.core.model.month_march
import com.divinelink.core.model.month_march_short
import com.divinelink.core.model.month_may
import com.divinelink.core.model.month_may_short
import com.divinelink.core.model.month_november
import com.divinelink.core.model.month_november_short
import com.divinelink.core.model.month_october
import com.divinelink.core.model.month_october_short
import com.divinelink.core.model.month_september
import com.divinelink.core.model.month_september_short
import org.jetbrains.compose.resources.StringResource

enum class Month(
  val number: Int,
  val long: StringResource,
  val short: StringResource,
) {
  JANUARY(1, Res.string.month_january, Res.string.month_january_short),
  FEBRUARY(2, Res.string.month_february, Res.string.month_february_short),
  MARCH(3, Res.string.month_march, Res.string.month_march_short),
  APRIL(4, Res.string.month_april, Res.string.month_april_short),
  MAY(5, Res.string.month_may, Res.string.month_may_short),
  JUNE(6, Res.string.month_june, Res.string.month_june_short),
  JULY(7, Res.string.month_july, Res.string.month_july_short),
  AUGUST(8, Res.string.month_august, Res.string.month_august_short),
  SEPTEMBER(9, Res.string.month_september, Res.string.month_september_short),
  OCTOBER(10, Res.string.month_october, Res.string.month_october_short),
  NOVEMBER(11, Res.string.month_november, Res.string.month_november_short),
  DECEMBER(12, Res.string.month_december, Res.string.month_december_short),
  ;

  companion object {
    fun from(number: Int): Month = entries.first { it.number == number }
  }
}
