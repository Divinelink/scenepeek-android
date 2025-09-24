package com.divinelink.feature.requests.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.DataState
import com.divinelink.core.model.filter.MediaRequestFilter
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import com.divinelink.core.model.jellyseerr.permission.canPerform
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.skeleton.RequestItemSkeleton
import com.divinelink.feature.requests.R
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.RequestsUiState
import com.divinelink.feature.requests.ui.provider.RequestsUiStateParameterProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsContent(
  state: RequestsUiState,
  action: (RequestsAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  PullToRefreshBox(
    isRefreshing = state.refreshing,
    onRefresh = { action(RequestsAction.Refresh) },
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
        onRetry = { action(RequestsAction.Refresh) },
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
  val scope = rememberCoroutineScope()

  scrollState.EndlessScrollHandler(
    buffer = 2,
    onLoadMore = { action(RequestsAction.LoadMore) },
  )

  ScenePeekLazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag(TestTags.Components.SCROLLABLE_CONTENT),
    state = scrollState,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    stickyHeader {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        FilterButton(
          filter = state.filter,
          filters = MediaRequestFilter.entries,
          onApplyFilter = {
            scope.launch {
              if (it != state.filter) scrollState.scrollToItem(0)
            }
            action(RequestsAction.UpdateFilter(it))
          },
        )
      }
    }

    when (state.data) {
      DataState.Initial -> items(5) {
        RequestItemSkeleton(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_12),
        )
      }
      is DataState.Data -> {
        if (state.data.isEmpty) {
          item {
            Text(
              modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimensions.keyline_32),
              text = stringResource(R.string.feature_requests_no_requests_available),
              textAlign = TextAlign.Center,
              style = MaterialTheme.typography.titleMedium,
            )
          }
        } else {
          items(
            items = state.data.data,
            key = { it.request.id },
          ) { request ->

            RequestMediaItem(
              modifier = Modifier
                .padding(horizontal = MaterialTheme.dimensions.keyline_12)
                .onFirstVisible(minFractionVisible = 0.1f) {
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
              onAction = action,
              canManageRequest = state.permissions.canPerform(ProfilePermission.MANAGE_REQUESTS),
            )
          }

          if (state.canLoadMore && state.loadingMore) {
            items(2) {
              RequestItemSkeleton(
                modifier = Modifier
                  .padding(horizontal = MaterialTheme.dimensions.keyline_12),
              )
            }
          }

          item {
            Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
          }
        }
      }
    }
  }
}

@Composable
@Previews
fun RequestsContentPreview(
  @PreviewParameter(RequestsUiStateParameterProvider::class) state: RequestsUiState,
) {
  PreviewLocalProvider {
    RequestsContent(
      state = state,
      action = {},
      onNavigate = {},
    )
  }
}
