package com.divinelink.feature.lists.user.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.local.LocalUiPreferences
import com.divinelink.feature.lists.R
import com.divinelink.feature.lists.user.ListsAction
import com.divinelink.feature.lists.user.ListsUiState
import com.divinelink.feature.lists.user.ui.provider.ListsUiStateParameterProvider

@Composable
fun ListsContent(
  uiState: ListsUiState,
  userInteraction: (ListsAction) -> Unit,
) {
  when {
    uiState.error != null -> BlankSlate(
      modifier = Modifier
        .padding(horizontal = MaterialTheme.dimensions.keyline_16)
        .padding(bottom = LocalBottomNavigationPadding.current),
      uiState = uiState.error,
    )
    uiState.isLoading -> LoadingContent()
    uiState.lists is ListData.Data && uiState.lists.isEmpty -> BlankSlate(
      modifier = Modifier
        .padding(horizontal = MaterialTheme.dimensions.keyline_16)
        .padding(bottom = LocalBottomNavigationPadding.current),
      uiState = BlankSlateState.Custom(
        title = UIText.ResourceText(R.string.feature_lists_empty),
      ),
    )
    uiState.lists is ListData.Data -> ListsDataContent(
      data = uiState.lists.data,
      userInteraction = userInteraction,
    )
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
          userInteraction = { },
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
      listsViewMode = ViewMode.GRID,
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
            lists = ListData.Data(ListItemFactory.page1()),
          ),
          userInteraction = { },
        )
      }
    }
  }
}
