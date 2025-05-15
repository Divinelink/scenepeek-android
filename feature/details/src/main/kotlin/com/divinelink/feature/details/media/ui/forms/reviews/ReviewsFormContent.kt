package com.divinelink.feature.details.media.ui.forms.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.details.reviews.ReviewItemCard
import com.divinelink.feature.details.R
import com.divinelink.feature.details.media.DetailsData
import com.divinelink.feature.details.media.ui.forms.FormEmptyContent

@Composable
fun ReviewsFormContent(
  modifier: Modifier = Modifier,
  title: String,
  reviews: DetailsData.Reviews,
) {
  if (reviews.items.isEmpty()) {
    FormEmptyContent(
      modifier = modifier,
      title = UIText.ResourceText(R.string.feature_details_no_reviews_available),
      description = UIText.ResourceText(R.string.feature_details_no_reviews_available_desc, title),
    )
  } else {
    ScenePeekLazyColumn(
      modifier = modifier.testTag(TestTags.Watchlist.WATCHLIST_CONTENT),
      contentPadding = PaddingValues(
        top = MaterialTheme.dimensions.keyline_16,
        start = MaterialTheme.dimensions.keyline_16,
        end = MaterialTheme.dimensions.keyline_16,
      ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      items(items = reviews.items) { item ->
        ReviewItemCard(
          review = item,
        )
      }
    }
  }
}
