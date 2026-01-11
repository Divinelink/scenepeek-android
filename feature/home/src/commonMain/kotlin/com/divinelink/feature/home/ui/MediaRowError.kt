package com.divinelink.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape

@Composable
fun MediaRowError(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  Column(modifier = modifier.fillMaxHeight()) {
    Spacer(modifier = Modifier.weight(1f))
    Card(
      shape = MaterialTheme.shape.medium,
      modifier = Modifier
        .widthIn(max = MaterialTheme.dimensions.shortMediaCard)
        .aspectRatio((2f / 3f))
        .clip(MaterialTheme.shape.medium),
      onClick = onClick,
    ) {
      Spacer(modifier = Modifier.weight(1f))

      IconButton(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        onClick = onClick,
      ) {
        Icon(
          imageVector = Icons.Default.Replay,
          contentDescription = "Retry",
        )
      }

      Spacer(modifier = Modifier.weight(1f))
    }
  }
}
