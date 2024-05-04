package com.andreolas.movierama.ui.theme

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import timber.log.Timber

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
    if (useDarkTheme) DarkColors else LightColors
  }

  if (blackBackground && useDarkTheme) {
    colors = colors.copy(background = Color.Black, surface = Color.Black)
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colors.background.toArgb()
      window.navigationBarColor = colors.background.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
    }
  }

  MaterialTheme(
    colorScheme = colors,
    typography = AppTypography,
    content = content
  )
}

@Composable
fun ColorScheme.textColorDisabled(): Color {
  return MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
}

@Composable
fun ColorScheme.fadedBackgroundColor(): Color {
  return MaterialTheme.colorScheme.scrim.copy(alpha = 0.62f)
}

@Composable
fun topBarColor(): Color {
  return MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.30f)
}

val FabSize = 56.dp
val SearchBarSize = 48.dp

val ListPaddingValues = PaddingValues(
  top = 16.dp,
  start = 12.dp,
  end = 12.dp,
  bottom = 16.dp,
)

enum class Theme(val storageKey: String) {
  SYSTEM("system"),
  LIGHT("light"),
  DARK("dark")
}

/**
 * Returns the matching [Theme] for the given [storageKey] value.
 */
fun themeFromStorageKey(storageKey: String): Theme? {
  return Theme.entries.firstOrNull { it.storageKey == storageKey }
}

fun updateForTheme(theme: Theme) = when (theme) {
  Theme.SYSTEM -> {
    Timber.d("Setting to follow system")
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
  }
  Theme.LIGHT -> {
    Timber.d("Setting to always night")
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
  }
  Theme.DARK -> {
    Timber.d("Setting to always night")
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
  }
}
