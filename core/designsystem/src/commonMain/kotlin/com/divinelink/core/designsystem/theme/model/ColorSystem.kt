package com.divinelink.core.designsystem.theme.model

import com.divinelink.scenepeek.designsystem.resources.Res
import com.divinelink.scenepeek.designsystem.resources.custom_theme
import com.divinelink.scenepeek.designsystem.resources.default_theme
import com.divinelink.scenepeek.designsystem.resources.dynamic_theme
import org.jetbrains.compose.resources.StringResource

enum class ColorSystem(
  val value: String,
  val resource: StringResource,
) {
  Default(
    value = "default",
    resource = Res.string.default_theme,
  ),
  Dynamic(
    value = "dynamic",
    resource = Res.string.dynamic_theme,
  ),
  Custom(
    value = "custom",
    resource = Res.string.custom_theme,
  );

  companion object {
    fun from(value: String): ColorSystem = entries.first { it.value == value }
  }
}
