package com.divinelink.feature.details.media.ui.forms.recommendation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.media.DetailSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.details.reviews.ReviewItemCard
import com.divinelink.core.ui.list.ScrollableMediaContent
import com.divinelink.feature.details.media.ui.DetailsSectionViewModel
import com.divinelink.feature.details.media.ui.components.DetailFormLoading
import com.divinelink.feature.details.media.ui.section.DetailSectionData
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details_no_recommendation_available
import com.divinelink.feature.details.resources.feature_details_no_recommendations_available_desc
import com.divinelink.feature.details.resources.feature_details_no_reviews_available
import com.divinelink.feature.details.resources.feature_details_no_reviews_available_desc
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailsSectionFormContent(
  modifier: Modifier = Modifier,
  section: DetailSection,
  viewModel: DetailsSectionViewModel = koinViewModel(
    key = section.toString(),
  ) { parametersOf(section) },
  title: String,
  onItemClick: (MediaItem) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  when (val data = state.data) {
    is DetailSectionData.Error -> Column(
      modifier = Modifier
        .fillMaxHeight()
        .padding(vertical = MaterialTheme.dimensions.keyline_32)
        .verticalScroll(rememberScrollState()),
    ) {
      BlankSlate(uiState = data.state)
    }
    DetailSectionData.Loading -> DetailFormLoading()
    is DetailSectionData.Reviews -> ScenePeekLazyColumn(
      modifier = modifier.testTag(TestTags.Details.Reviews.FORM),
      contentPadding = PaddingValues(
        top = MaterialTheme.dimensions.keyline_16,
        start = MaterialTheme.dimensions.keyline_16,
        end = MaterialTheme.dimensions.keyline_16,
      ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      if (data.items.isEmpty()) {
        item {
          BlankSlate(
            modifier = Modifier.testTag(TestTags.Details.Reviews.EMPTY),
            uiState = BlankSlateState.Custom(
              title = UIText.ResourceText(Res.string.feature_details_no_reviews_available),
              description = UIText.ResourceText(
                Res.string.feature_details_no_reviews_available_desc,
                title,
              ),
            ),
          )
        }
      } else {
        items(items = data.items) { item ->
          ReviewItemCard(
            review = item,
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
      }
    }
    is DetailSectionData.Media -> ScrollableMediaContent(
      modifier = modifier.testTag(TestTags.Details.Recommendations.FORM),
      items = data.media,
      section = ViewableSection.MEDIA_DETAILS,
      onLoadMore = {},
      onSwitchPreferences = onSwitchPreferences,
      onClick = onItemClick,
      onLongClick = onLongClick,
      canLoadMore = false,
      emptyContent = {
        BlankSlate(
          modifier = Modifier
            .testTag(TestTags.Details.Recommendations.EMPTY)
            .padding(top = MaterialTheme.dimensions.keyline_12),
          uiState = BlankSlateState.Custom(
            title = UIText.ResourceText(Res.string.feature_details_no_recommendation_available),
            description = UIText.ResourceText(
              Res.string.feature_details_no_recommendations_available_desc,
              title,
            ),
          ),
        )
      },
    )
  }
}
