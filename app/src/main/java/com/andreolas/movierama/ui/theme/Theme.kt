package com.andreolas.movierama.ui.theme

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import timber.log.Timber

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (useDarkTheme) {
        DarkColors
    } else {
        LightColors
    }

    // Used to converse Material 2 to Material 3 when needed.
    androidx.compose.material.MaterialTheme(
        typography = MD2Typography,
        colors = if (useDarkTheme) {
            DarkMD2Colors
        } else {
            LightMD2Colors
        }
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            content = content
        )
    }
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
    DARK("dark");
}

/**
 * Returns the matching [Theme] for the given [storageKey] value.
 */
fun themeFromStorageKey(storageKey: String): Theme? {
    return Theme.values().firstOrNull { it.storageKey == storageKey }
}

fun AppCompatActivity.updateForTheme(theme: Theme) = when (theme) {
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
