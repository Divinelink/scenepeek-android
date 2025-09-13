package com.divinelink.feature.requests.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.DataState
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.skeleton.DetailedMediaItemSkeleton
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
  ScenePeekLazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag(TestTags.Components.MEDIA_LIST_CONTENT),
    state = scrollState,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
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
          key = { it.id },
        ) { request ->

          Text(
            request.mediaStatus.name,
          )
        }

        if (state.loadingMore) {
//        if (state.data.data.canLoadMore() && state.loadingMore) { // TODO Add canLoadMore
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
