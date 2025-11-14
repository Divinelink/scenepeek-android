package com.divinelink.core.ui.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.valentinilk.shimmer.shimmer

@Composable
@Previews
fun DetailedMediaItemSkeleton() {
  Row(
    modifier = Modifier
      .padding(MaterialTheme.dimensions.keyline_8)
      .height(120.dp)
      .fillMaxWidth(),
  ) {
    Column {
      Box(
        modifier = Modifier.widthIn(max = 80.dp),
        contentAlignment = Alignment.Center,
      ) {
        MovieImageSkeleton()
      }
    }

    Column(
      Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(start = MaterialTheme.dimensions.keyline_12),
    ) {
      Box(
        modifier = Modifier
          .width(120.dp)
          .height(26.dp)
          .shimmer()
          .clip(MaterialTheme.shapes.medium)
          .background(MaterialTheme.colorScheme.onSurfaceVariant),
      )
      Box(
        modifier = Modifier
          .padding(top = MaterialTheme.dimensions.keyline_4)
          .width(96.dp)
          .height(16.dp)
          .shimmer()
          .clip(MaterialTheme.shapes.medium)
          .background(MaterialTheme.colorScheme.onSurfaceVariant),
      )

      Box(
        modifier = Modifier
          .height(86.dp)
          .fillMaxWidth()
          .padding(top = MaterialTheme.dimensions.keyline_8)
          .shimmer()
          .clip(MaterialTheme.shapes.medium)
          .background(MaterialTheme.colorScheme.onSurfaceVariant),
      )
    }
  }
}
