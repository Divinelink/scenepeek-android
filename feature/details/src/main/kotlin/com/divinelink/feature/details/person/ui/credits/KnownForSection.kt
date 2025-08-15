package com.divinelink.feature.details.person.ui.credits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.divinelink.core.designsystem.theme.ListPaddingValues
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.feature.details.R

@Composable
fun KnownForSection(
  list: List<PersonCredit>,
  onMediaClick: (MediaItem) -> Unit,
) {
  Column(
    modifier = Modifier
      .testTag(TestTags.Person.KNOWN_FOR_SECTION)
      .padding(vertical = MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    Text(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_12),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      text = stringResource(id = R.string.feature_details_known_for_section),
    )

    key(list) {
      LazyRow(
        modifier = Modifier.testTag(TestTags.Person.KNOWN_FOR_SECTION_LIST),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        contentPadding = ListPaddingValues,
      ) {
        items(
          items = list,
          key = { it.id },
        ) { credit ->
          MediaItem(
            media = credit.mediaItem,
            subtitle = credit.role.title,
            showDate = true,
            onMediaItemClick = onMediaClick,
          )
        }
      }
    }
  }
}
