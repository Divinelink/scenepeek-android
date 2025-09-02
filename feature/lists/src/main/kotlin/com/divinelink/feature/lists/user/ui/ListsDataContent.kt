package com.divinelink.feature.lists.user.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.divinelink.core.model.UIText
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.scaffold.isMediumScreenWidthOrWider
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.button.switchview.SwitchViewButton
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.core.ui.list.ListItemCard
import com.divinelink.core.ui.composition.rememberViewModePreferences
import com.divinelink.feature.lists.R
import com.divinelink.feature.lists.user.ListsAction
import kotlinx.coroutines.launch

@Composable
fun ListsDataContent(
  modifier: Modifier = Modifier,
  data: PaginationData<ListItem>,
  userInteraction: (ListsAction) -> Unit,
) {
  val scrollState = rememberLazyGridState()
  val scope = rememberCoroutineScope()
  val isMediumScreenWidthOrWider = isMediumScreenWidthOrWider()
  var numberOfCells by rememberSaveable { mutableIntStateOf(1) }

  val viewMode = rememberViewModePreferences(ViewableSection.LISTS)

  val padding = when (viewMode) {
    ViewMode.GRID -> MaterialTheme.dimensions.keyline_16
    ViewMode.LIST -> MaterialTheme.dimensions.keyline_4
  }

  scrollState.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = { userInteraction(ListsAction.LoadMore) },
  )

  LaunchedEffect(isMediumScreenWidthOrWider) {
    numberOfCells = if (isMediumScreenWidthOrWider.value) {
      2
    } else {
      1
    }
  }

  if (data.isEmpty()) {
    BlankSlate(
      modifier = modifier
        .verticalScroll(rememberScrollState())
        .fillMaxSize()
        .padding(bottom = LocalBottomNavigationPadding.current),
      uiState = BlankSlateState.Custom(
        title = UIText.ResourceText(R.string.feature_lists_empty),
      ),
    )
  } else {
    Box(modifier.fillMaxSize()) {
      LazyVerticalGrid(
        modifier = Modifier
          .fillMaxSize()
          .testTag(TestTags.Lists.SCROLLABLE_CONTENT.format(viewMode.value)),
        columns = GridCells.Fixed(numberOfCells),
        contentPadding = PaddingValues(
          start = MaterialTheme.dimensions.keyline_8,
          end = MaterialTheme.dimensions.keyline_8,
          top = MaterialTheme.dimensions.keyline_16,
          bottom = LocalBottomNavigationPadding.current,
        ),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(padding),
        horizontalArrangement = Arrangement.spacedBy(padding),
      ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
          ListSettingsRow(
            onSwitchViewMode = {
              userInteraction.invoke(ListsAction.SwitchViewMode)
            },
          )
        }

        items(
          key = { list -> list.id },
          items = data.list,
        ) { listItem ->
          when (viewMode) {
            ViewMode.GRID -> GridItemListCard(
              modifier = Modifier
                .animateItem()
                .animateContentSize()
                .fillMaxWidth(),
              listItem = listItem,
              onClick = {
                userInteraction(
                  ListsAction.OnListClick(
                    id = listItem.id,
                    name = listItem.name,
                    backdropPath = listItem.backdropPath,
                    description = listItem.description,
                    public = listItem.public,
                  ),
                )
              },
            )
            ViewMode.LIST -> ListItemCard(
              modifier = Modifier
                .animateItem()
                .animateContentSize(),
              listItem = listItem,
              onClick = {
                userInteraction(
                  ListsAction.OnListClick(
                    id = listItem.id,
                    name = listItem.name,
                    backdropPath = listItem.backdropPath,
                    description = listItem.description,
                    public = listItem.public,
                  ),
                )
              },
            )
          }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
          Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
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
}

@Composable
fun ListSettingsRow(onSwitchViewMode: () -> Unit) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.End,
  ) {
    SwitchViewButton(
      section = ViewableSection.LISTS,
      onClick = onSwitchViewMode,
    )
  }
}
