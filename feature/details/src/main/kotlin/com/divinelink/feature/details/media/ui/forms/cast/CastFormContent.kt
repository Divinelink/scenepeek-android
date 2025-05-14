package com.divinelink.feature.details.media.ui.forms.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.credit.PersonItem
import com.divinelink.feature.details.R
import com.divinelink.feature.details.media.DetailsData
import com.divinelink.feature.details.media.ui.forms.FormEmptyContent
import com.divinelink.core.ui.R as uiR

@Composable
fun CastFormContent(
  modifier: Modifier = Modifier,
  cast: DetailsData.Cast,
  title: String,
  obfuscateSpoilers: Boolean,
  onPersonClick: (Person) -> Unit,
  onViewAllClick: () -> Unit,
) {
  if (cast.items.isEmpty()) {
    FormEmptyContent(
      modifier = modifier,
      title = UIText.ResourceText(R.string.feature_details_no_cast_available),
      description = UIText.ResourceText(R.string.feature_details_no_cast_available_desc, title),
    )
  } else {
    ScenePeekLazyColumn(
      modifier = modifier.testTag(TestTags.Credits.CAST_CREDITS_CONTENT),
      contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_16),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      if (cast.isTv) {
        item {
          TotalTvCastRow(cast, onViewAllClick)
        }
      } else {
        item {
          Spacer(modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16))
        }
      }

      items(
        items = cast.items.apply {
          if (cast.isTv) {
            take(30)
          }
        },
        key = { it.id },
      ) { person ->
        PersonItem(
          person = person,
          onClick = onPersonClick,
          isObfuscated = obfuscateSpoilers,
        )
      }
    }
  }
}

@Composable
private fun TotalTvCastRow(
  cast: DetailsData.Cast,
  onViewAllClick: () -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center,
  ) {
    Text(
      modifier = Modifier.align(alignment = Alignment.CenterVertically),
      style = MaterialTheme.typography.bodyMedium,
      text = stringResource(id = uiR.string.core_ui_total_cast, cast.items.size),
    )
    Spacer(modifier = Modifier.weight(1f))
    TextButton(
      modifier = Modifier.align(alignment = Alignment.CenterVertically),
      onClick = onViewAllClick,
    ) {
      Text(
        text = stringResource(id = uiR.string.core_ui_view_all),
        style = MaterialTheme.typography.bodyMedium,
      )
    }
  }
}
