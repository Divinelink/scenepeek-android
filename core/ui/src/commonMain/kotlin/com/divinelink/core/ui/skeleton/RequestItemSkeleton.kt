package com.divinelink.core.ui.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.valentinilk.shimmer.shimmer

@Composable
fun RequestItemSkeleton(modifier: Modifier = Modifier) {
  Row(
    modifier = modifier
      .height(300.dp)
      .fillMaxWidth(),
  ) {
    Column {
      Row {
        Box(
          modifier = Modifier.widthIn(max = 60.dp),
          contentAlignment = Alignment.Center,
        ) {
          MovieImageSkeleton()
        }

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .height(100.dp)
            .padding(start = MaterialTheme.dimensions.keyline_12),
          verticalArrangement = Arrangement.Center,
        ) {
          Box(
            modifier = Modifier
              .width(78.dp)
              .height(20.dp)
              .shimmer()
              .clip(MaterialTheme.shapes.medium)
              .background(MaterialTheme.colorScheme.onSurfaceVariant),
          )

          Box(
            modifier = Modifier
              .height(32.dp)
              .width(100.dp)
              .padding(top = MaterialTheme.dimensions.keyline_8)
              .shimmer()
              .clip(MaterialTheme.shapes.medium)
              .background(MaterialTheme.colorScheme.onSurfaceVariant),
          )
        }
      }

      Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
        Box(
          modifier = Modifier
            .height(32.dp)
            .width(80.dp)
            .padding(top = MaterialTheme.dimensions.keyline_8)
            .shimmer()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )

        Box(
          modifier = Modifier
            .height(32.dp)
            .width(92.dp)
            .padding(top = MaterialTheme.dimensions.keyline_8)
            .shimmer()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
        Box(
          modifier = Modifier
            .height(32.dp)
            .width(100.dp)
            .padding(top = MaterialTheme.dimensions.keyline_8)
            .shimmer()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )

        Box(
          modifier = Modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(top = MaterialTheme.dimensions.keyline_8)
            .shimmer()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )
      }
      Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
        Box(
          modifier = Modifier
            .height(32.dp)
            .width(80.dp)
            .padding(top = MaterialTheme.dimensions.keyline_8)
            .shimmer()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )

        Box(
          modifier = Modifier
            .height(32.dp)
            .fillMaxWidth(0.4f)
            .padding(top = MaterialTheme.dimensions.keyline_8)
            .shimmer()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )
      }

      Spacer(modifier = Modifier.weight(1f))

      Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        Box(
          modifier = Modifier
            .height(52.dp)
            .weight(1f)
            .padding(top = MaterialTheme.dimensions.keyline_8)
            .shimmer()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )
        Box(
          modifier = Modifier
            .height(52.dp)
            .weight(1f)
            .padding(top = MaterialTheme.dimensions.keyline_8)
            .shimmer()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )
      }

      Box(
        modifier = Modifier
          .height(52.dp)
          .fillMaxWidth()
          .padding(top = MaterialTheme.dimensions.keyline_8)
          .shimmer()
          .clip(MaterialTheme.shapes.extraLarge)
          .background(MaterialTheme.colorScheme.onSurfaceVariant),
      )
    }
  }
}

@Previews
@Composable
fun RequestItemSkeletonPreview() {
  PreviewLocalProvider {
    RequestItemSkeleton()
  }
}
