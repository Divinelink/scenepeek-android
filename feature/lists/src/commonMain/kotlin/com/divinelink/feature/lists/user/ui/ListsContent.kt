package com.divinelink.feature.lists.user.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.LocalUiPreferences
import com.divinelink.feature.lists.user.ListsAction
import com.divinelink.feature.lists.user.ListsUiState
import com.divinelink.feature.lists.user.ui.provider.ListsUiStateParameterProvider
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun ListsContent(
  uiState: ListsUiState,
  action: (ListsAction) -> Unit,
) {
  PullToRefreshBox(
    isRefreshing = uiState.refreshing,
    onRefresh = { action(ListsAction.Refresh) },
    modifier = Modifier
      .fillMaxSize()
      .testTag(TestTags.Lists.PULL_TO_REFRESH),
  ) {
    when {
      uiState.error != null -> BlankSlate(
        modifier = Modifier
          .padding(horizontal = MaterialTheme.dimensions.keyline_16)
          .padding(bottom = LocalBottomNavigationPadding.current),
        uiState = uiState.error,
        onRetry = { action(ListsAction.Refresh) },
      )
      uiState.isLoading -> LoadingContent()
      uiState.lists is ListData.Data -> ListsDataContent(
        modifier = Modifier.fillMaxSize(),
        data = uiState.lists.data,
        userInteraction = action,
      )
    }
  }
}

@Composable
@Previews
fun ListsContentListPreview(
  @PreviewParameter(ListsUiStateParameterProvider::class) state: ListsUiState,
) {
  CompositionLocalProvider(
    LocalUiPreferences provides UiPreferences.Initial,
  ) {
    AppTheme {
      Surface {
        ListsContent(
          uiState = state,
          action = { },
        )
      }
    }
  }
}

@Composable
@Previews
fun ListsContentGridPreview() {
  CompositionLocalProvider(
    LocalUiPreferences provides UiPreferences.Initial.copy(
      viewModes = ViewableSection.entries.associateWith { ViewMode.GRID },
    ),
  ) {
    AppTheme {
      Surface {
        ListsContent(
          uiState = ListsUiState(
            page = 1,
            isLoading = false,
            loadingMore = true,
            error = null,
            refreshing = false,
            lists = ListData.Data(ListItemFactory.page1()),
          ),
          action = { },
        )
      }
    }
  }
}
