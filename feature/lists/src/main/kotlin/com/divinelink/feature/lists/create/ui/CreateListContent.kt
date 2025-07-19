package com.divinelink.feature.lists.create.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.Previews
import com.divinelink.feature.lists.create.CreateListAction
import com.divinelink.feature.lists.create.CreateListUiState
import com.divinelink.feature.lists.create.ui.provider.CreateListUiStateParameterProvider

@Composable
fun CreateListContent(
  uiState: CreateListUiState,
  action: (CreateListAction) -> Unit,
) {
}

@Composable
@Previews
fun CreateListContentPreview(
  @PreviewParameter(CreateListUiStateParameterProvider::class) state: CreateListUiState,
) {
  AppTheme {
    Surface {
      CreateListContent(
        uiState = state,
        action = { },
      )
    }
  }
}
