package com.divinelink.feature.profile.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.profile.ProfileUiState
import com.divinelink.feature.profile.ProfileUserInteraction
import com.divinelink.feature.profile.ui.provider.ProfileUiStateParameterProvider

@Composable
fun ProfileContent(
  uiState: ProfileUiState,
  userInteraction: (ProfileUserInteraction) -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag(TestTags.LAZY_COLUMN),
    contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_16),
  ) {
    item {
      ProfileItem(
        tmdbAccount = uiState.tmdbAccount,
        onLoginClick = { userInteraction(ProfileUserInteraction.Login) },
      )
    }
  }
}

@Composable
@Previews
fun ProfileContentPreview(
  @PreviewParameter(ProfileUiStateParameterProvider::class) state: ProfileUiState,
) {
  AppTheme {
    Surface {
      ProfileContent(
        uiState = state,
        userInteraction = {},
      )
    }
  }
}
