package com.divinelink.feature.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.profile.ProfileUiState
import com.divinelink.feature.profile.ui.provider.ProfileUiStateParameterProvider

@Composable
fun ProfileContent(uiState: ProfileUiState) {
  ScenePeekLazyColumn(
    modifier = Modifier.testTag(TestTags.LAZY_COLUMN),
  ) {
    item {
      ProfileItem(uiState.tmdbAccount)
    }
  }
}

@Composable
@Previews
fun ProfileContentPreview(
  @PreviewParameter(ProfileUiStateParameterProvider::class) state: ProfileUiState,
) {
  ProfileContent(
    uiState = state,
  )
}
