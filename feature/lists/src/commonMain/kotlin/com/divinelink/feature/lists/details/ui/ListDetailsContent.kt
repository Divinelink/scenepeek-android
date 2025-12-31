package com.divinelink.feature.lists.details.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.feature.lists.details.ListDetailsAction
import com.divinelink.feature.lists.details.ListDetailsUiState
import com.divinelink.feature.lists.details.ui.provider.ListDetailsUiStateParameterProvider

@Composable
fun ListDetailsContent(
  state: ListDetailsUiState,
  action: (ListDetailsAction) -> Unit,
  onUpdateProgress: (Float) -> Unit,
  onBackdropLoaded: () -> Unit,
  onSwitchViewMode: (ViewableSection) -> Unit,
  onNavigate: (Navigation) -> Unit,
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
        onRetry = {
          action(ListDetailsAction.Refresh)
        },
      )

      state.details is ListDetailsData.Initial || state.details is ListDetailsData.Data ->
        ListScrollableContent(
          state = state,
          action = action,
          onUpdateToolbarProgress = onUpdateProgress,
          onBackdropLoaded = onBackdropLoaded,
          onSwitchViewMode = onSwitchViewMode,
          onNavigate = onNavigate,
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
        onUpdateProgress = {},
        onBackdropLoaded = {},
        onNavigate = {},
        onSwitchViewMode = {},
      )
    }
  }
}
