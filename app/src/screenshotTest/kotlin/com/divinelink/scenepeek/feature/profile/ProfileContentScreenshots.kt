package com.divinelink.scenepeek.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.profile.ProfileUiState
import com.divinelink.feature.profile.ui.ProfileContentPreview
import com.divinelink.feature.profile.ui.provider.ProfileUiStateParameterProvider

@Composable
@Previews
fun ProfileContentScreenshots(
  @PreviewParameter(ProfileUiStateParameterProvider::class) state: ProfileUiState,
) {
  ProfileContentPreview(state)
}
