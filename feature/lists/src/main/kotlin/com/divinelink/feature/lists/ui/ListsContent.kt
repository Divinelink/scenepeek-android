package com.divinelink.feature.lists.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.feature.lists.ListData
import com.divinelink.feature.lists.ListsUiState
import com.divinelink.feature.lists.ListsUserInteraction
import com.divinelink.feature.lists.R
import com.divinelink.feature.lists.ui.provider.ListsUiStateParameterProvider

@Composable
fun ListsContent(
  uiState: ListsUiState,
  userInteraction: (ListsUserInteraction) -> Unit,
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
    else -> ListsDataContent(
      uiState = uiState,
      userInteraction = userInteraction,
    )
  }
}

@Composable
@Previews
fun ListsContentPreview(
  @PreviewParameter(ListsUiStateParameterProvider::class) state: ListsUiState,
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
