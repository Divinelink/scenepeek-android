package com.divinelink.feature.add.to.account.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.VisibilityBadge
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.feature.add.to.account.R
import com.divinelink.feature.add.to.account.list.AddToListAction

@Composable
fun ListsDataContent(
  data: PaginationData<ListItem>,
  action: (AddToListAction) -> Unit,
) {
  val scrollState = rememberLazyListState()

  scrollState.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = { action(AddToListAction.LoadMore) },
  )

  LazyColumn(
    modifier = Modifier.testTag(TestTags.Lists.SCROLLABLE_CONTENT),
    contentPadding = PaddingValues(
      top = MaterialTheme.dimensions.keyline_16,
      bottom = LocalBottomNavigationPadding.current,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    item {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.dimensions.keyline_16),
        text = stringResource(R.string.feature_add_to_account_list_title),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
      )
    }
    items(
      key = { list -> list.id },
      items = data.list,
    ) { listItem ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            action(AddToListAction.OnListClick(listItem.id))
          }
          .padding(
            vertical = MaterialTheme.dimensions.keyline_8,
            horizontal = MaterialTheme.dimensions.keyline_16,
          ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        ListDetails(modifier = Modifier.weight(1f), listItem = listItem)
        VisibilityBadge(
          isPublic = listItem.public,
        )
      }
    }
  }
}

@Composable
private fun ListDetails(
  modifier: Modifier = Modifier,
  listItem: ListItem,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    Text(
      text = listItem.name,
      style = MaterialTheme.typography.titleSmall,
    )

    Text(
      text = pluralStringResource(
        com.divinelink.core.ui.R.plurals.core_ui_item_count,
        listItem.numberOfItems,
        listItem.numberOfItems,
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      style = MaterialTheme.typography.bodySmall,
    )
  }
}
