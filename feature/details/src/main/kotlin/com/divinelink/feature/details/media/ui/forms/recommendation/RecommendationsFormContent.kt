package com.divinelink.feature.details.media.ui.forms.recommendation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.DetailedMediaItem
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.details.R

@Composable
fun RecommendationsFormContent(
  modifier: Modifier = Modifier,
  title: String,
  recommendations: DetailsData.Recommendations,
  onItemClick: (MediaItem.Media) -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = modifier.testTag(TestTags.Details.Recommendations.FORM),
    contentPadding = PaddingValues(
      top = MaterialTheme.dimensions.keyline_16,
      start = MaterialTheme.dimensions.keyline_16,
      end = MaterialTheme.dimensions.keyline_16,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    if (recommendations.items.isEmpty()) {
      item {
        BlankSlate(
          modifier = Modifier.testTag(TestTags.Details.Recommendations.EMPTY),
          uiState = BlankSlateState.Custom(
            title = UIText.ResourceText(R.string.feature_details_no_recommendation_available),
            description = UIText.ResourceText(
              R.string.feature_details_no_recommendations_available_desc,
              title,
            ),
          ),
        )
      }
    } else {
      items(
        items = recommendations.items,
        key = { it.id },
      ) { media ->
        DetailedMediaItem(
          mediaItem = media,
          onClick = onItemClick,
        )
      }

      item {
        Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
      }
    }
  }
}
