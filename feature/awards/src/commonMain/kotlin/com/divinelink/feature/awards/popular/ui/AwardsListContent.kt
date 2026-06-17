package com.divinelink.feature.awards.popular.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.model.awards.Ceremony
import com.divinelink.core.ui.coil.Image
import com.divinelink.feature.awards.popular.AwardsAction
import com.divinelink.feature.awards.popular.AwardsUiState

@Composable
fun AwardsListContent(
  uiState: AwardsUiState,
  action: (AwardsAction) -> Unit,
) {
  val state = rememberLazyGridState()

  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = MaterialTheme.dimensions.posterSizeSmall),
    state = state,
    modifier = Modifier,
    contentPadding = PaddingValues(
      start = MaterialTheme.dimensions.keyline_8,
      end = MaterialTheme.dimensions.keyline_8,
      top = MaterialTheme.dimensions.keyline_8,
      bottom = MaterialTheme.dimensions.keyline_16,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    items(
      items = uiState.ceremonies,
      key = { it.id },
    ) { ceremony ->
      CeremonyItem(
        ceremony = ceremony,
        onClick = { action(AwardsAction.OnCeremonyClick(ceremony.id)) },
      )
    }
  }
}

@Composable
fun CeremonyItem(
  ceremony: Ceremony,
  onClick: (String) -> Unit,
) {
  Card(
    shape = MaterialTheme.shape.medium,
    onClick = { onClick(ceremony.id) },
    modifier = Modifier
      .widthIn(max = MaterialTheme.dimensions.posterSizeSmall)
      .clip(MaterialTheme.shape.medium)
      .clipToBounds(),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
  ) {
    Image(
      modifier = Modifier
        .aspectRatio(2f / 2f),
      quality = ImageQuality.SQUARE,
      path = ceremony.imagePath,
    )

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_4))

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = MaterialTheme.dimensions.keyline_12)
        .padding(horizontal = MaterialTheme.dimensions.keyline_8),
      text = ceremony.title,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
    )
  }
}
