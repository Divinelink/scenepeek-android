package com.divinelink.feature.requests.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.Previews
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.RequestsUiState
import com.divinelink.feature.requests.ui.provider.RequestsUiStateParameterProvider

@Composable
fun RequestsContent(
  uiState: RequestsUiState,
  action: (RequestsAction) -> Unit,
) {
}

@Composable
@Previews
fun RequestsContentPreview(
  @PreviewParameter(RequestsUiStateParameterProvider::class) state: RequestsUiState,
) {
  AppTheme {
    Surface {
      RequestsContent(
        uiState = state,
        action = { },
      )
    }
  }
}
