package com.divinelink.feature.lists.details.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.Previews
import com.divinelink.feature.lists.details.ListDetailsAction
import com.divinelink.feature.lists.details.ListDetailsUiState
import com.divinelink.feature.lists.details.ui.provider.ListDetailsUiStateParameterProvider

@Composable
fun ListDetailsContent(
  uiState: ListDetailsUiState,
  action: (ListDetailsAction) -> Unit,
) {
}

@Composable
@Previews
fun ListDetailsContentPreview(
  @PreviewParameter(ListDetailsUiStateParameterProvider::class) state: ListDetailsUiState,
) {
  AppTheme {
    Surface {
      ListDetailsContent(
        uiState = state,
        action = { },
      )
    }
  }
}

@Previews
@Composable
fun ListDetailsContentScreenshots(
  @PreviewParameter(ListDetailsUiStateParameterProvider::class) uiState: ListDetailsUiState,
) {
  ListDetailsContentPreview(uiState)
}
