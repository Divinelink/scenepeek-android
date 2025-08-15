package com.divinelink.scenepeek.feature.user.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.ui.Previews
import com.divinelink.feature.user.data.UserDataScreenContentPreview
import com.divinelink.feature.user.data.UserDataUiState
import com.divinelink.feature.user.data.ui.provider.UserDataUiStateParameterProvider

@Composable
@PreviewTest
@Previews
fun UserDataScreenContentScreenshots(
  @PreviewParameter(UserDataUiStateParameterProvider::class) uiState: UserDataUiState,
) {
  UserDataScreenContentPreview(uiState)
}
