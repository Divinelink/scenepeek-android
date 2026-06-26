package com.divinelink.core.ui.skeleton

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews

@Composable
@Previews
fun MediaItemSkeleton(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.widthIn(max = MaterialTheme.dimensions.shortMediaCard),
    contentAlignment = Alignment.Center,
  ) {
    MovieImageSkeleton()
  }
}
