package com.divinelink.feature.lists.details.ui

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.mediaCardSize
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.toStub
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.DetailedMediaItem
import com.divinelink.core.ui.ScreenSettingsRow
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.VisibilityBadge
import com.divinelink.core.ui.components.details.BackdropImage
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.core.ui.components.selectable.SelectableCard
import com.divinelink.core.ui.components.selectable.SelectableCardSmall
import com.divinelink.core.ui.composition.rememberViewModePreferences
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.skeleton.DetailedMediaItemSkeleton
import com.divinelink.feature.add.to.account.list.delete.ui.RemoveFromListDialog
import com.divinelink.feature.add.to.account.list.delete.ui.RemoveItem
import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuModal
import com.divinelink.feature.lists.details.ListDetailsAction
import com.divinelink.feature.lists.details.ListDetailsUiState
import com.divinelink.feature.lists.resources.Res
import com.divinelink.feature.lists.resources.feature_lists_empty_list
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ListScrollableContent(
  state: ListDetailsUiState,
  action: (ListDetailsAction) -> Unit,
  onUpdateToolbarProgress: (Float) -> Unit,
  onBackdropLoaded: () -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  var showActionModal by remember { mutableStateOf<MediaItem?>(null) }
  var showRemoveItemsDialog by rememberSaveable { mutableStateOf(false) }

  val scrollState = rememberLazyGridState()
  val scope = rememberCoroutineScope()
  val density = LocalDensity.current
  val viewMode = rememberViewModePreferences(ViewableSection.LIST_DETAILS)

  val columns = when (viewMode) {
    ViewMode.GRID -> GridCells.Adaptive(mediaCardSize())
    ViewMode.LIST -> GridCells.Fixed(1)
  }

  var backdropHeight by remember { mutableStateOf(0.dp) }
  val firstItemScrollProgress by remember {
    derivedStateOf {
      if (scrollState.firstVisibleItemIndex == 0) {
        // Convert dp to pixels for calculation
        val itemHeightPx = with(density) { backdropHeight.toPx() }
        (scrollState.firstVisibleItemScrollOffset.toFloat() / itemHeightPx).coerceIn(0f, 1f)
      } else {
        1f
      }
    }
  }

  LaunchedEffect(firstItemScrollProgress) {
    onUpdateToolbarProgress(firstItemScrollProgress)
  }

  BackHandler(enabled = state.multipleSelectMode) {
    action.invoke(ListDetailsAction.OnDismissMultipleSelect)
  }

  scrollState.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = { action(ListDetailsAction.LoadMore) },
  )

  if (showActionModal != null) {
    ActionMenuModal(
      mediaItem = showActionModal!!,
      onDismissRequest = { showActionModal = null },
      entryPoint = ActionMenuEntryPoint.ListDetails(
        listId = state.id,
        listName = state.details.name,
      ),
      onMultiSelect = { media ->
        if (media !is MediaItem.Media) return@ActionMenuModal
        action(ListDetailsAction.SelectMedia(media))
        showActionModal = null
      },
      onNavigateToAddToList = {
        onNavigate(
          Navigation.AddToListRoute(
            id = it.id,
            mediaType = it.mediaType.value,
          ),
        )
      },
    )
  }

  if (showRemoveItemsDialog) {
    RemoveFromListDialog(
      onDismissRequest = { showRemoveItemsDialog = false },
      onConfirm = {
        action(ListDetailsAction.OnRemoveItems)
        showRemoveItemsDialog = false
      },
      item = if (state.selectedMedia.size == 1) {
        RemoveItem.Item(
          name = state.selectedMedia.firstOrNull()?.name ?: "",
          listName = state.details.name,
        )
      } else {
        RemoveItem.Batch(
          size = state.selectedMedia.size,
          listName = state.details.name,
        )
      },
    )
  }

  Box(
    Modifier.fillMaxSize(),
  ) {
    LazyVerticalGrid(
      modifier = Modifier
        .fillMaxSize()
        .testTag(TestTags.Components.MEDIA_GRID_CONTENT.format(viewMode.value)),
      columns = columns,
      state = scrollState,
      contentPadding = PaddingValues(bottom = LocalBottomNavigationPadding.current),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      item(span = { GridItemSpan(maxLineSpan) }) {
        BackdropImage(
          modifier = Modifier
            .onSizeChanged {
              backdropHeight = with(density) { it.height.toDp() }
            },
          path = state.details.backdropPath,
          onBackdropLoaded = onBackdropLoaded,
          applyOffset = false,
        )
      }

      item(span = { GridItemSpan(maxLineSpan) }) {
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .padding(
              horizontal = MaterialTheme.dimensions.keyline_16,
              vertical = MaterialTheme.dimensions.keyline_4,
            ),
          text = state.details.name,
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onSurface,
        )
      }

      item(span = { GridItemSpan(maxLineSpan) }) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Start,
        ) {
          VisibilityBadge(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
            isPublic = state.details.public,
          )
        }
      }

      item(span = { GridItemSpan(maxLineSpan) }) {
        if (state.details.description.isNotBlank()) {
          Text(
            modifier = Modifier
              .fillMaxWidth()
              .padding(
                horizontal = MaterialTheme.dimensions.keyline_16,
                vertical = MaterialTheme.dimensions.keyline_4,
              ),
            text = state.details.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }
      }

      when (state.details) {
        is ListDetailsData.Initial -> items(
          count = 5,
          span = { GridItemSpan(maxLineSpan) },
        ) {
          DetailedMediaItemSkeleton()
        }

        is ListDetailsData.Data -> if (state.details.media.isEmpty()) {
          item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
              modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.Lists.Details.EMPTY_ITEM)
                .padding(MaterialTheme.dimensions.keyline_32),
              text = stringResource(Res.string.feature_lists_empty_list),
              textAlign = TextAlign.Center,
              style = MaterialTheme.typography.bodyLarge,
            )
          }
        } else {
          item(span = { GridItemSpan(maxLineSpan) }) {
            ScreenSettingsRow(
              section = ViewableSection.LIST_DETAILS,
              onSwitchPreferences = onSwitchPreferences,
            )
          }

          items(
            items = state.details.media,
            key = { it.uniqueIdentifier },
          ) { media ->
            val isSelected = state.selectedMedia.contains(media)

            when (viewMode) {
              ViewMode.GRID -> SelectableCardSmall(
                modifier = Modifier
                  .semantics {
                    contentDescription = TestTags.Lists.Details.SELECTED_CARD.format(
                      media.name,
                      isSelected,
                    )
                  }
                  .padding(horizontal = MaterialTheme.dimensions.keyline_8)
                  .animateItem(),
                onClick = {
                  if (state.multipleSelectMode) {
                    action(
                      ListDetailsAction.SelectMedia(media),
                    )
                  } else {
                    action(
                      ListDetailsAction.OnItemClick(
                        mediaId = media.id,
                        mediaType = media.mediaType,
                      ),
                    )
                  }
                },
                onLongClick = {
                  if (showActionModal == null) {
                    showActionModal = media
                  } else {
                    action(ListDetailsAction.SelectMedia(media = media))
                  }
                },
                isSelectionMode = state.multipleSelectMode,
                isSelected = isSelected,
                content = { onClick, onLongClick ->
                  MediaItem(
                    modifier = Modifier
                      .animateItem()
                      .animateContentSize(),
                    media = media,
                    onClick = { onClick() },
                    onLongClick = { onLongClick() },
                  )
                },
              )
              ViewMode.LIST -> SelectableCard(
                modifier = Modifier
                  .animateItem()
                  .semantics {
                    contentDescription = TestTags.Lists.Details.SELECTED_CARD.format(
                      media.name,
                      isSelected,
                    )
                  },
                isSelected = isSelected,
                isSelectionMode = state.multipleSelectMode,
                onClick = {
                  if (state.multipleSelectMode) {
                    action(
                      ListDetailsAction.SelectMedia(media),
                    )
                  } else {
                    action(
                      ListDetailsAction.OnItemClick(
                        mediaId = media.id,
                        mediaType = media.mediaType,
                      ),
                    )
                  }
                },
                onLongClick = {
                  if (showActionModal == null) {
                    showActionModal = media
                  } else {
                    action(ListDetailsAction.SelectMedia(media = media))
                  }
                },
              ) { onClick, onLongClick ->
                DetailedMediaItem(
                  modifier = Modifier
                    .animateItem()
                    .animateContentSize(),
                  mediaItem = media,
                  onClick = { onClick() },
                  onLongClick = { onLongClick() },
                )
              }
            }
          }

          if (state.details.data.canLoadMore() && state.loadingMore) {
            items(
              count = 3,
              span = { GridItemSpan(maxLineSpan) },
            ) {
              DetailedMediaItemSkeleton()
            }
          }

          item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
          }
        }
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

    MultipleSelectHeader(
      visible = state.multipleSelectMode,
      selectedItems = state.selectedMedia.map { it.toStub() },
      totalItemCount = (state.details as? ListDetailsData.Data)?.media?.size ?: 0,
      onSelectAll = { action(ListDetailsAction.OnSelectAll) },
      onDeselectAll = { action(ListDetailsAction.OnDeselectAll) },
      onDismiss = { action(ListDetailsAction.OnDismissMultipleSelect) },
      onRemoveAction = { showRemoveItemsDialog = true },
    )
  }
}
