package com.divinelink.core.ui.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.divinelink.core.ui.Previews
import com.valentinilk.shimmer.shimmer

@Composable
@Previews
fun MovieImageSkeleton() {
  Box(
    modifier = Modifier
      .aspectRatio((2f / 3f))
      .shimmer()
      .clip(MaterialTheme.shapes.medium)
      .background(MaterialTheme.colorScheme.onSurfaceVariant),
  )
}
