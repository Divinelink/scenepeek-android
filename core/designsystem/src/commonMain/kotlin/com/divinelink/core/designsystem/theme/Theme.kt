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
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.materialkolor.DynamicMaterialTheme

@Composable
fun AppTheme(
  theme: ThemePreferences = ThemePreferences.initial,
  content: @Composable () -> Unit,
) {
  val useDarkTheme = when (theme.theme) {
    Theme.SYSTEM -> isSystemInDarkTheme()
    Theme.LIGHT -> false
    Theme.DARK -> true
  }

  var colors = systemAppearance(
    dynamicColor = theme.colorSystem == ColorSystem.Dynamic,
    blackBackground = theme.isPureBlack,
    isDark = useDarkTheme,
  )

  if (theme.isPureBlack && useDarkTheme) {
    colors = colors.copy(background = Color.Black, surface = Color.Black)
  }

  CompositionLocalProvider(
    LocalDarkThemeProvider provides useDarkTheme,
  ) {
    when (theme.colorSystem) {
      ColorSystem.Dynamic,
      ColorSystem.Default,
        -> MaterialTheme(
        colorScheme = colors,
        typography = scenePeekTypography(),
        content = { Surface { content() } },
      )
      ColorSystem.Custom -> DynamicMaterialTheme(
        seedColor = Color(theme.themeColor.toULong()),
        isDark = useDarkTheme,
        isAmoled = theme.isPureBlack,
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
