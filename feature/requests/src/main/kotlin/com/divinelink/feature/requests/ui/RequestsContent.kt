package com.divinelink.feature.requests.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.DataState
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.jellyseerr.media.RequestUiItem
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.coil.OpaqueBackdropImage
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.components.modal.jellyseerr.manage.SeasonPill
import com.divinelink.core.ui.media.MediaImage
import com.divinelink.core.ui.skeleton.DetailedMediaItemSkeleton
import com.divinelink.feature.requests.R
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.RequestsUiState
import com.divinelink.feature.requests.ui.provider.RequestsUiStateParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsContent(
  state: RequestsUiState,
  action: (RequestsAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  PullToRefreshBox(
    isRefreshing = state.refreshing,
    onRefresh = {
      // TODO
    },
    modifier = Modifier
      .wrapContentSize()
      .testTag(TestTags.Components.PULL_TO_REFRESH),
  ) {
    when {
      state.error != null && state.data is DataState.Initial -> BlankSlate(
        modifier = Modifier
          .padding(horizontal = MaterialTheme.dimensions.keyline_16)
          .padding(bottom = LocalBottomNavigationPadding.current),
        uiState = state.error,
        onRetry = {
          // TODO
        },
      )

      state.data is DataState.Initial || state.data is DataState.Data -> RequestsScrollableContent(
        state = state,
        action = action,
        onNavigate = onNavigate,
      )
    }
  }
}

@Composable
fun RequestsScrollableContent(
  state: RequestsUiState,
  action: (RequestsAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val scrollState = rememberLazyListState()

  scrollState.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = { action(RequestsAction.LoadMore) },
  )

  ScenePeekLazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag(TestTags.Components.MEDIA_LIST_CONTENT),
    state = scrollState,
    contentPadding = PaddingValues(
      top = MaterialTheme.dimensions.keyline_16,
      start = MaterialTheme.dimensions.keyline_16,
      end = MaterialTheme.dimensions.keyline_16,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    when (state.data) {
      is DataState.Initial -> items(5) {
        DetailedMediaItemSkeleton()
      }

      is DataState.Data -> if (state.data.isEmpty) {
        item {
          Text(
            modifier = Modifier
              .fillMaxWidth()
              .padding(MaterialTheme.dimensions.keyline_32),
            text = "No requests available",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
          )
        }
      } else {
        items(
          items = state.data.data,
          key = { it.request.id },
        ) { request ->

          RequestMediaItem(
            modifier = Modifier.onFirstVisible {
              action.invoke(RequestsAction.FetchMediaItem(request))
            },
            item = request,
            onClick = {
              onNavigate(
                Navigation.DetailsRoute(
                  mediaType = it.mediaType,
                  id = it.id,
                  isFavorite = it.isFavorite,
                ),
              )
            },
            onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
          )
        }

        if (state.canLoadMore) {
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
}

@Composable
fun RequestMediaItem(
  modifier: Modifier = Modifier,
  item: RequestUiItem,
  onClick: (MediaItem.Media) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
) {
  if (item.mediaState == null) return

  AnimatedContent(
    modifier = modifier,
    targetState = item.mediaState,
    contentKey = { state ->
      when (state) {
        is ItemState.Data -> "Data"
        ItemState.Loading -> "Loading"
        null -> null
      }
    },
  ) { state ->
    when (state) {
      is ItemState.Data -> Card(
        modifier = Modifier
          .combinedClickable(
            onClick = { onClick(state.item) },
            onLongClick = { onLongClick(state.item) },
          )
          .fillMaxWidth(),
      ) {
        Box {
          OpaqueBackdropImage(path = state.item.backdropPath)

          Column(
            modifier = Modifier.padding(MaterialTheme.dimensions.keyline_8),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          ) {
            Row(
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              MediaImage(
                media = state.item,
                modifier = Modifier
                  .align(Alignment.Top)
                  .height(MaterialTheme.dimensions.keyline_96),
              )

              Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
              ) {
                Text(
                  text = state.item.releaseDate.take(4),
                  style = MaterialTheme.typography.titleSmall,
                )
                Text(text = state.item.name, style = MaterialTheme.typography.titleMedium)

                if (item.request.seasons.isNotEmpty()) {
                  val seasons = item.request.seasons
                  FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                      MaterialTheme.dimensions.keyline_8,
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
                    itemVerticalAlignment = Alignment.CenterVertically,
                  ) {
                    Text(
                      text = pluralStringResource(UiPlurals.core_ui_season, seasons.size),
                      style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                      ),
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    seasons.forEach { season ->
                      SeasonPill(season.seasonNumber)
                    }
                  }
                }
              }
            }

            Row(
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(UiString.core_ui_status),
              )
              JellyseerrStatusPill(
                status = item.request.requestStatus,
              )
            }

            Row(
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
            ) {
              Text(
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(R.string.feature_requests_requested),
              )
              Text(
                // TODO Localize
                text = "${item.request.requestDate} by ${item.request.requester.displayName}",
                style = MaterialTheme.typography.bodyMedium,
              )
            }

            item.request.profileName?.let { profileName ->
              ProfileNameSection(profileName)
            }
          }
        }
      }
      ItemState.Loading -> DetailedMediaItemSkeleton()
      null -> {
        // Do nothing
      }
    }
  }
}

@Composable
private fun ProfileNameSection(profileName: String) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      style = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      text = stringResource(R.string.feature_requests_profile),
    )
    Text(
      text = profileName,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Composable
@Previews
fun RequestsContentPreview(
  @PreviewParameter(RequestsUiStateParameterProvider::class) state: RequestsUiState,
) {
  AppTheme {
    Surface {
      RequestsContent(
        state = state,
        action = {},
        onNavigate = {},
      )
    }
  }
}
