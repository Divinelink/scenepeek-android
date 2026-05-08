package com.divinelink.core.model.details

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.digital
import com.divinelink.core.model.resources.physical
import com.divinelink.core.model.resources.premiere
import com.divinelink.core.model.resources.theatrical
import com.divinelink.core.model.resources.theatrical_limited
import com.divinelink.core.model.resources.tv
import org.jetbrains.compose.resources.StringResource

enum class ReleaseType(val type: Int, val resource: StringResource) {
  Premiere(1, Res.string.premiere),
  TheatricalLimited(2, Res.string.theatrical_limited),
  Theatrical(3, Res.string.theatrical),
  Digital(4, Res.string.digital),
  Physical(5, Res.string.physical),
  TV(6, Res.string.tv),
  ;

  companion object {
    fun from(type: Int) = ReleaseType.entries.find { it.type == type }
  }
}
