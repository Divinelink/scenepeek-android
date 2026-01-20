package com.divinelink.core.model.discover

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.any
import com.divinelink.core.model.resources.decade
import com.divinelink.core.model.resources.range
import com.divinelink.core.model.resources.single
import org.jetbrains.compose.resources.StringResource

enum class YearType(val label: StringResource) {
  Any(label = Res.string.any),
  Decade(label = Res.string.decade),
  Single(label = Res.string.single),
  Range(label = Res.string.range),
}
