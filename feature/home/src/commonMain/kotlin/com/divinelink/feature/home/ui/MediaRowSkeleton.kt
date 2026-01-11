package com.divinelink.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.skeleton.MediaItemSkeleton

@Composable
fun MediaRowSkeleton() {
  LazyRow(
    contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_8),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    items(count = 10) {
      MediaItemSkeleton()
    }
  }
}
