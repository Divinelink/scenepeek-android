package com.divinelink.scenepeek.feature.details.person.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.details.person.ui.PersonContentGridPreview
import com.divinelink.feature.details.person.ui.PersonContentListPreview
import com.divinelink.feature.details.person.ui.PersonUiState
import com.divinelink.feature.details.person.ui.provider.PersonUiStatePreviewParameterProvider

@Previews
@Composable
fun PersonContentScreenshots(
  @PreviewParameter(PersonUiStatePreviewParameterProvider::class) uiState: PersonUiState,
) {
  PersonContentListPreview(uiState)
}

@Previews
@Composable
fun PersonContentGridScreenshots() {
  PersonContentGridPreview()
}
