package com.divinelink.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

@Composable
fun AppTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  blackBackground: Boolean = true,
  content: @Composable () -> Unit,
) {
  var colors = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    val context = LocalContext.current
    if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
  } else {
    if (useDarkTheme) darkScheme else lightScheme
  }

  if (blackBackground && useDarkTheme) {
    colors = colors.copy(background = Color.Black, surface = Color.Black)
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      updateStatusBarColor(view, !useDarkTheme)
    }
  }

  CompositionLocalProvider(
    LocalDarkThemeProvider provides useDarkTheme,
  ) {
    MaterialTheme(
      colorScheme = colors,
      typography = ScenePeekTypography,
      content = content,
    )
  }
}

val LocalDarkThemeProvider = staticCompositionLocalOf { false }
val LocalBottomNavigationPadding = compositionLocalOf { 0.dp }

val ListPaddingValues = PaddingValues(
  vertical = 16.dp,
  horizontal = 12.dp,
)

enum class Theme(val storageKey: String) {
  SYSTEM("system"),
  LIGHT("light"),
  DARK("dark"),
}

/**
 * Returns the matching [Theme] for the given [storageKey] value.
 */
fun themeFromStorageKey(storageKey: String): Theme? =
  Theme.entries.firstOrNull { it.storageKey == storageKey }
