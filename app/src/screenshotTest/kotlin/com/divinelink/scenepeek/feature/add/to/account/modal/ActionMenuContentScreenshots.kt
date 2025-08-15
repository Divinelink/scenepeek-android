package com.divinelink.scenepeek.feature.add.to.account.modal

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.Previews
import com.divinelink.feature.add.to.account.modal.ActionMenuUiState
import com.divinelink.feature.add.to.account.modal.ui.ActionMenuContent
import com.divinelink.feature.add.to.account.modal.ui.provider.ActionMenuUiStateParameterProvider

@Previews
@PreviewTest
@Composable
fun ActionMenuContentScreenshots(
  @PreviewParameter(ActionMenuUiStateParameterProvider::class) state: ActionMenuUiState,
) {
  AppTheme {
    Surface {
      ActionMenuContent(
        uiState = state,
        onAction = {},
      )
    }
  }
}
