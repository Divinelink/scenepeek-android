package com.divinelink.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object ScenePeekNavigationDefaults {
  @Composable
  fun BoxScope.FadeGradientEffect() = Box(
    modifier = Modifier
      .fillMaxWidth()
      .align(Alignment.BottomCenter)
      .height(120.dp)
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.surface,
          ),
        ),
      ),
  )

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
