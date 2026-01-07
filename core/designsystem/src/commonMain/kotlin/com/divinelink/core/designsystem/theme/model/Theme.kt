package com.divinelink.core.designsystem.theme.model

import com.divinelink.scenepeek.designsystem.resources.Res
import com.divinelink.scenepeek.designsystem.resources.dark
import com.divinelink.scenepeek.designsystem.resources.light
import com.divinelink.scenepeek.designsystem.resources.system
import org.jetbrains.compose.resources.StringResource

enum class Theme(
  val storageKey: String,
  val label: StringResource,
) {
  SYSTEM("system", Res.string.system),
  LIGHT("light", Res.string.light),
  DARK("dark", Res.string.dark),
  ;

  companion object {
    fun from(key: String) = Theme.entries.firstOrNull { it.storageKey == key }
  }
}
