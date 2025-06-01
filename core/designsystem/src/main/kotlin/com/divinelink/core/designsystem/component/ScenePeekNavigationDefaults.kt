package com.divinelink.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable

object ScenePeekNavigationDefaults {
  @Composable
  fun contentColor() = MaterialTheme.colorScheme.contentColorFor(containerColor())

  @Composable
  fun containerColor() = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)

  @Composable
  fun selectedTextColor() = MaterialTheme.colorScheme.primary

  @Composable
  fun selectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

  @Composable
  fun indicatorColor() = MaterialTheme.colorScheme.primaryContainer
}
