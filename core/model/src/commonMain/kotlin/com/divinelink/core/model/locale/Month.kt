package com.divinelink.core.model.locale

import com.divinelink.core.model.Res
import com.divinelink.core.model.month_april
import com.divinelink.core.model.month_august
import com.divinelink.core.model.month_december
import com.divinelink.core.model.month_february
import com.divinelink.core.model.month_january
import com.divinelink.core.model.month_july
import com.divinelink.core.model.month_june
import com.divinelink.core.model.month_march
import com.divinelink.core.model.month_may
import com.divinelink.core.model.month_november
import com.divinelink.core.model.month_october
import com.divinelink.core.model.month_september
import org.jetbrains.compose.resources.StringResource

enum class Month(
  val number: Int,
  val resource: StringResource,
) {
  JANUARY(1, Res.string.month_january),
  FEBRUARY(2, Res.string.month_february),
  MARCH(3, Res.string.month_march),
  APRIL(4, Res.string.month_april),
  MAY(5, Res.string.month_may),
  JUNE(6, Res.string.month_june),
  JULY(7, Res.string.month_july),
  AUGUST(8, Res.string.month_august),
  SEPTEMBER(9, Res.string.month_september),
  OCTOBER(10, Res.string.month_october),
  NOVEMBER(11, Res.string.month_november),
  DECEMBER(12, Res.string.month_december);

  companion object {
    fun from(number: Int): Month = entries.first { it.number == number }
  }
}
