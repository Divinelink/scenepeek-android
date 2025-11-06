package com.divinelink.feature.details.media.ui.forms.recommendation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.list.ScrollableMediaContent
import com.divinelink.feature.details.R

@Composable
fun RecommendationsFormContent(
  modifier: Modifier = Modifier,
  title: String,
  recommendations: DetailsData.Recommendations,
  onItemClick: (MediaItem.Media) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
  onSwitchViewMode: (ViewableSection) -> Unit,
) {
  if (recommendations.items.isEmpty()) {
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
  } else {
    ScrollableMediaContent(
      items = recommendations.items,
      section = ViewableSection.MEDIA_DETAILS,
      onLoadMore = {},
      onSwitchViewMode = onSwitchViewMode,
      onClick = onItemClick,
      onLongClick = onLongClick,
      modifier = modifier.testTag(TestTags.Details.Recommendations.FORM),
    )
  }
}
