package com.divinelink.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.model.ColorPreference
import com.divinelink.scenepeek.designsystem.resources.Res
import com.divinelink.scenepeek.designsystem.resources.dark
import com.divinelink.scenepeek.designsystem.resources.light
import com.divinelink.scenepeek.designsystem.resources.system
import com.materialkolor.DynamicMaterialTheme
import org.jetbrains.compose.resources.StringResource

@Composable
fun AppTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  colorPreference: ColorPreference = ColorPreference.Default,
  seedColor: Long = seedLong,
  blackBackground: Boolean = true,
  content: @Composable () -> Unit,
) {
  var colors = systemAppearance(
    dynamicColor = colorPreference == ColorPreference.Dynamic,
    blackBackground = blackBackground,
    isDark = useDarkTheme,
  )

  if (blackBackground && useDarkTheme) {
    colors = colors.copy(background = Color.Black, surface = Color.Black)
  }

  CompositionLocalProvider(
    LocalDarkThemeProvider provides useDarkTheme,
  ) {
    when (colorPreference) {
      ColorPreference.Dynamic,
      ColorPreference.Default,
        -> MaterialTheme(
        colorScheme = colors,
        typography = scenePeekTypography(),
        content = { Surface { content() } },
      )
      ColorPreference.Custom -> DynamicMaterialTheme(
        seedColor = Color(seedColor.toULong()),
        isDark = useDarkTheme,
        isAmoled = blackBackground,
        animate = false,
        typography = scenePeekTypography(),
        content = { Surface { content() } },
      )
    }
  }
}

@Composable
internal expect fun systemAppearance(
  dynamicColor: Boolean = false,
  blackBackground: Boolean = false,
  isDark: Boolean,
): ColorScheme

val LocalDarkThemeProvider = staticCompositionLocalOf { false }
val LocalBottomNavigationPadding = compositionLocalOf { 0.dp }

val ListPaddingValues = PaddingValues(
  vertical = 16.dp,
  horizontal = 12.dp,
)

enum class Theme(
  val storageKey: String,
  val label: StringResource,
) {
  SYSTEM("system", Res.string.system),
  LIGHT("light", Res.string.light),
  DARK("dark", Res.string.dark),
}

/**
 * Returns the matching [Theme] for the given [storageKey] value.
 */
fun themeFromStorageKey(storageKey: String): Theme? =
  Theme.entries.firstOrNull { it.storageKey == storageKey }
