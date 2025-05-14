package com.divinelink.feature.details.media.ui.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.DetailedMediaItem
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.details.media.DetailsData

@Composable
fun RecommendationsFormContent(
  modifier: Modifier = Modifier,
  recommendations: DetailsData.Recommendations,
  onItemClick: (MediaItem.Media) -> Unit,
) {
  if (recommendations.items.isEmpty()) {
//    BlankSlate(uiState = BlankSlateState.Custom(title = )) // TODO
  } else {
    ScenePeekLazyColumn(
      modifier = modifier.testTag(TestTags.Watchlist.WATCHLIST_CONTENT),
      contentPadding = PaddingValues(
        top = MaterialTheme.dimensions.keyline_16,
        start = MaterialTheme.dimensions.keyline_16,
        end = MaterialTheme.dimensions.keyline_16,
      ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      items(
        items = recommendations.items,
        key = { it.id },
      ) { media ->
        DetailedMediaItem(
          mediaItem = media,
          onClick = onItemClick,
        )
      }
    }
  }
}
