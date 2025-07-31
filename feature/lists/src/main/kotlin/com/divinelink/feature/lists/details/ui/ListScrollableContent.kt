package com.divinelink.feature.lists.details.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.toStub
import com.divinelink.core.ui.DetailedMediaItem
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.SelectableCard
import com.divinelink.core.ui.components.VisibilityBadge
import com.divinelink.core.ui.components.details.BackdropImage
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.core.ui.skeleton.DetailedMediaItemSkeleton
import com.divinelink.feature.add.to.account.list.delete.ui.RemoveFromListDialog
import com.divinelink.feature.add.to.account.list.delete.ui.RemoveItem
import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuModal
import com.divinelink.feature.lists.R
import com.divinelink.feature.lists.details.ListDetailsAction
import com.divinelink.feature.lists.details.ListDetailsUiState
import kotlinx.coroutines.launch

@Composable
fun ListScrollableContent(
  state: ListDetailsUiState,
  action: (ListDetailsAction) -> Unit,
  onShowTitle: (Boolean) -> Unit,
  onBackdropLoaded: () -> Unit,
  onNavigateToAddToList: (MediaItem) -> Unit,
) {
  var showActionModal by remember { mutableStateOf<MediaItem?>(null) }
  var showRemoveItemsDialog by rememberSaveable { mutableStateOf(false) }

  val scrollState = rememberLazyListState()
  val scope = rememberCoroutineScope()
  val density = LocalDensity.current
  val shouldShowTitle by remember {
    with(density) {
      derivedStateOf {
        scrollState.firstVisibleItemIndex > 0 ||
          scrollState.firstVisibleItemScrollOffset > 168.dp.roundToPx()
      }
    }
  }

  LaunchedEffect(shouldShowTitle) {
    onShowTitle(shouldShowTitle)
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
      entryPoint = ActionMenuEntryPoint.ListDetails(state.id),
      onMultiSelect = { media ->
        action(ListDetailsAction.SelectMedia(media))
        showActionModal = null
      },
      onNavigateToAddToList = onNavigateToAddToList,
    )
  }

  if (showRemoveItemsDialog) {
    RemoveFromListDialog(
      onDismissRequest = { showRemoveItemsDialog = false },
      onConfirm = {
        action(ListDetailsAction.OnRemoveItems)
        showRemoveItemsDialog = false
      },
      item = if (state.selectedMediaIds.size == 1) {
        RemoveItem.Item(
          name = (state.details as? ListDetailsData.Data)?.media?.find {
            it.id == state.selectedMediaIds.firstOrNull()?.mediaId &&
              it.mediaType == state.selectedMediaIds.firstOrNull()?.mediaType
          }?.name ?: "",
          listName = state.details.name,
        )
      } else {
        RemoveItem.Batch(
          size = state.selectedMediaIds.size,
          listName = state.details.name,
        )
      },
    )
  }

  Box(
    Modifier.fillMaxSize(),
  ) {
    ScenePeekLazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag(TestTags.Components.MEDIA_LIST_CONTENT),
      state = scrollState,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      item {
        BackdropImage(
          path = state.details.backdropPath,
          onBackdropLoaded = onBackdropLoaded,
          applyOffset = false,
        )
      }
      item {
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

      item {
        VisibilityBadge(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          isPublic = state.details.public,
        )
      }

      item {
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
        is ListDetailsData.Initial -> items(5) {
          DetailedMediaItemSkeleton()
        }

        is ListDetailsData.Data -> if (state.details.media.isEmpty()) {
          item {
            Text(
              modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.Lists.Details.EMPTY_ITEM)
                .padding(MaterialTheme.dimensions.keyline_32),
              text = stringResource(R.string.feature_lists_empty_list),
              textAlign = TextAlign.Center,
              style = MaterialTheme.typography.bodyLarge,
            )
          }
        } else {
          items(
            items = state.details.media,
            key = { it.uniqueIdentifier },
          ) { media ->
            val isSelected = state.selectedMediaIds.contains(media.toStub())

            SelectableCard(
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
                mediaItem = media,
                onClick = { onClick() },
                onLongClick = { onLongClick() },
              )
            }
          }

          if (state.details.data.canLoadMore() && state.loadingMore) {
            items(3) {
              DetailedMediaItemSkeleton()
            }
          }

          item {
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
      selectedItems = state.selectedMediaIds,
      totalItemCount = (state.details as? ListDetailsData.Data)?.media?.size ?: 0,
      onSelectAll = { action(ListDetailsAction.OnSelectAll) },
      onDeselectAll = { action(ListDetailsAction.OnDeselectAll) },
      onDismiss = { action(ListDetailsAction.OnDismissMultipleSelect) },
      onRemoveAction = { showRemoveItemsDialog = true },
    )
  }
}
