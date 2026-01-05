package com.divinelink.feature.settings.app.appearance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape

@Composable
fun ColorSampleBox(colorLong: Long) {
  Box(
    modifier = Modifier
      .size(
        width = MaterialTheme.dimensions.keyline_48,
        height = MaterialTheme.dimensions.keyline_48,
      )
      .background(
        shape = MaterialTheme.shape.large,
        color = Color(colorLong.toULong()),
      ),
  )
}
