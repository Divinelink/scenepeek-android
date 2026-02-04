package com.divinelink.feature.season.ui.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Person
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.toPersonRoute
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.credit.PersonItem

@Composable
fun PeopleFormContent(
  modifier: Modifier = Modifier,
  cast: List<Person>,
  onNavigate: (Navigation) -> Unit,
  blankSlateState: BlankSlateState,
) {
  ScenePeekLazyColumn(
    modifier = modifier.testTag(TestTags.Details.Cast.FORM),
    contentPadding = PaddingValues(
      top = MaterialTheme.dimensions.keyline_16,
      start = MaterialTheme.dimensions.keyline_16,
      end = MaterialTheme.dimensions.keyline_16,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    if (cast.isEmpty()) {
      item {
        BlankSlate(
          modifier = Modifier
            .padding(top = MaterialTheme.dimensions.keyline_12)
            .testTag(TestTags.Details.Cast.EMPTY),
          uiState = blankSlateState,
        )
      }
    } else {
      items(
        items = cast,
        key = { it.id },
      ) { person ->
        PersonItem(
          person = person,
          onClick = { onNavigate(it.toPersonRoute()) },
          isObfuscated = false,
        )
      }

      item {
        Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
      }
    }
  }
}
