package com.divinelink.feature.details.media.ui.forms.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.credit.PersonItem
import com.divinelink.core.ui.resources.core_ui_cast_count
import com.divinelink.core.ui.resources.core_ui_view_all
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details_no_cast_available
import com.divinelink.feature.details.resources.feature_details_no_cast_available_desc
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CastFormContent(
  modifier: Modifier = Modifier,
  cast: DetailsData.Cast,
  title: String,
  obfuscateSpoilers: Boolean,
  onPersonClick: (Person) -> Unit,
  onViewAllClick: () -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = modifier.testTag(TestTags.Details.Cast.FORM),
    contentPadding = PaddingValues(
      top = if (cast.isTv) {
        MaterialTheme.dimensions.keyline_0
      } else {
        MaterialTheme.dimensions.keyline_16
      },
      start = MaterialTheme.dimensions.keyline_16,
      end = MaterialTheme.dimensions.keyline_16,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    if (cast.items.isEmpty()) {
      item {
        BlankSlate(
          modifier = Modifier.testTag(TestTags.Details.Cast.EMPTY),
          uiState = BlankSlateState.Custom(
            title = UIText.ResourceText(Res.string.feature_details_no_cast_available),
            description = UIText.ResourceText(
              Res.string.feature_details_no_cast_available_desc,
              title,
            ),
          ),
        )
      }
    } else {
      if (cast.isTv) {
        item {
          TotalTvCastRow(cast, onViewAllClick)
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

      item {
        Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
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
      text = pluralStringResource(
        UiPlurals.core_ui_cast_count,
        cast.items.size,
        cast.items.size,
      ),
    )
    Spacer(modifier = Modifier.weight(1f))
    TextButton(
      modifier = Modifier
        .testTag(TestTags.VIEW_ALL)
        .align(alignment = Alignment.CenterVertically),
      onClick = onViewAllClick,
    ) {
      Text(
        text = stringResource(UiString.core_ui_view_all),
        style = MaterialTheme.typography.bodyMedium,
      )
    }
  }
}
