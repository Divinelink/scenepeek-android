package com.divinelink.feature.lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.scaffold.isMediumScreenWidthOrWider
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.feature.lists.ListsUserInteraction
import kotlinx.coroutines.launch

@Composable
fun ListsDataContent(
  data: PaginationData<ListItem>,
  userInteraction: (ListsUserInteraction) -> Unit,
) {
  val scrollState = rememberLazyListState()
  val scope = rememberCoroutineScope()
  val isMediumScreenWidthOrWider = isMediumScreenWidthOrWider()
  var numberOfCells by rememberSaveable { mutableIntStateOf(1) }

  scrollState.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = { userInteraction(ListsUserInteraction.LoadMore) },
  )

  LaunchedEffect(isMediumScreenWidthOrWider) {
    numberOfCells = if (isMediumScreenWidthOrWider.value) {
      2
    } else {
      1
    }
  }

  Box(Modifier.fillMaxSize()) {
    LazyVerticalGrid(
      modifier = Modifier.testTag(TestTags.Lists.SCROLLABLE_CONTENT),
      columns = GridCells.Fixed(numberOfCells),
      contentPadding = PaddingValues(
        top = MaterialTheme.dimensions.keyline_16,
        start = MaterialTheme.dimensions.keyline_16,
        end = MaterialTheme.dimensions.keyline_16,
        bottom = LocalBottomNavigationPadding.current,
      ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      items(
        key = { list -> list.id },
        items = data.list,
      ) { listItem ->
        ListCard(
          listItem = listItem,
          onClick = { userInteraction(ListsUserInteraction.OnListClick(listItem.id)) },
        )
      }
    }

    ScrollToTopButton(
      modifier = Modifier.align(Alignment.BottomCenter),
      visible = scrollState.canScrollToTop(),
      onClick = {
        scope.launch {
          scrollState.animateScrollToItem(0)
        }
      },
    )
  }
}
