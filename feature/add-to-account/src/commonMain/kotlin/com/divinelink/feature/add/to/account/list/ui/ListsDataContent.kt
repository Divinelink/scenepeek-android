package com.divinelink.feature.add.to.account.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.list.ListItemCard
import com.divinelink.feature.add.to.account.Res
import com.divinelink.feature.add.to.account.feature_add_to_account_empty_lists
import com.divinelink.feature.add.to.account.list.AddToListAction
import org.jetbrains.compose.resources.stringResource

@Composable
fun ListsDataContent(
  data: PaginationData<ListItem>,
  addedToLists: Set<Int>,
  action: (AddToListAction) -> Unit,
) {
  val scrollState = rememberLazyListState()

  scrollState.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = { action(AddToListAction.LoadMore) },
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxWidth()
      .testTag(TestTags.Lists.SCROLLABLE_CONTENT),
    contentPadding = PaddingValues(
      top = MaterialTheme.dimensions.keyline_16,
      bottom = LocalBottomNavigationPadding.current,
    ),
    state = scrollState,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    if (data.isEmpty()) {
      item {
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimensions.keyline_32),
          text = stringResource(Res.string.feature_add_to_account_empty_lists),
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodyLarge,
        )
      }
    } else {
      items(
        key = { list -> list.id },
        items = data.list,
      ) { listItem ->
        ListItemCard(
          modifier = Modifier
            .fillMaxWidth()
            .onFirstVisible {
              action(AddToListAction.CheckItemStatus(listItem.id))
            },
          listItem = listItem,
          onClick = {
            action(AddToListAction.OnListClick(listItem.id))
          },
          isAdded = listItem.id in addedToLists,
        )
      }

      item {
        Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
      }
    }
  }
}
