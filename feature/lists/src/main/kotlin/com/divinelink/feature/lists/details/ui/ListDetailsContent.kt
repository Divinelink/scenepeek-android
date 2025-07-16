package com.divinelink.feature.lists.details.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.lists.details.ListDetailsAction
import com.divinelink.feature.lists.details.ListDetailsUiState
import com.divinelink.feature.lists.details.ui.provider.ListDetailsUiStateParameterProvider
import com.divinelink.core.ui.R as uiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailsContent(
  state: ListDetailsUiState,
  action: (ListDetailsAction) -> Unit,
  onShowTitle: (Boolean) -> Unit,
  onBackdropLoaded: () -> Unit,
) {
  PullToRefreshBox(
    isRefreshing = state.refreshing,
    onRefresh = { action(ListDetailsAction.Refresh) },
    modifier = Modifier
      .wrapContentSize()
      .testTag(TestTags.Lists.Details.PULL_TO_REFRESH),
  ) {
    when {
      state.error != null && state.details is ListDetailsData.Initial -> BlankSlate(
        modifier = Modifier
          .padding(horizontal = MaterialTheme.dimensions.keyline_16)
          .padding(bottom = LocalBottomNavigationPadding.current),
        uiState = state.error,
        actionText = when (state.error) {
          BlankSlateState.Offline, BlankSlateState.Generic -> UIText.ResourceText(
            uiR.string.core_ui_retry,
          )
          else -> null
        },
        onRetry = {
          action(ListDetailsAction.Refresh)
        },
      )

      state.details is ListDetailsData.Initial || state.details is ListDetailsData.Data ->
        ListScrollableContent(
          state = state,
          onMediaClick = {
            action(
              ListDetailsAction.OnItemClick(
                mediaId = it.id,
                mediaType = it.mediaType,
              ),
            )
          },
          onLoadMore = { action(ListDetailsAction.LoadMore) },
          onShowTitle = onShowTitle,
          onBackdropLoaded = onBackdropLoaded,
        )
    }
  }
}

@Composable
@Previews
fun ListDetailsContentPreview(
  @PreviewParameter(ListDetailsUiStateParameterProvider::class) state: ListDetailsUiState,
) {
  AppTheme {
    Surface {
      ListDetailsContent(
        state = state,
        action = {},
        onShowTitle = {},
        onBackdropLoaded = {},
      )
    }
  }
}
