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
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.credit.PersonItem
import com.divinelink.feature.details.media.DetailsData

@Composable
fun CastFormContent(
  modifier: Modifier = Modifier,
  cast: DetailsData.Cast,
  obfuscateSpoilers: Boolean,
  onPersonClick: (Person) -> Unit,
) {
  if (cast.items.isEmpty()) {
//    BlankSlate(uiState = BlankSlateState.Custom(title = )) // TODO
  } else {
    ScenePeekLazyColumn(
      modifier = modifier.testTag(TestTags.Credits.CAST_CREDITS_CONTENT),
      contentPadding = PaddingValues(
        top = MaterialTheme.dimensions.keyline_16,
        start = MaterialTheme.dimensions.keyline_16,
        end = MaterialTheme.dimensions.keyline_16,
      ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      items(
        items = cast.items,
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
