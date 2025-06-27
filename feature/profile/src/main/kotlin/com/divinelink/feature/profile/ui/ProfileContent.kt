package com.divinelink.feature.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.getString
import com.divinelink.feature.profile.ProfileSection
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
      .testTag(TestTags.Profile.CONTENT),
    contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    item {
      ProfileItem(
        tmdbAccount = uiState.accountUiState,
        onLoginClick = { userInteraction(ProfileUserInteraction.Login) },
      )
    }

    item {
      ProfileSectionItem(
        section = ProfileSection.Watchlist,
        onClick = {
          userInteraction(ProfileUserInteraction.NavigateToUserData(UserDataSection.Watchlist))
        },
      )
    }

    item {
      ProfileSectionItem(
        section = ProfileSection.Ratings,
        onClick = {
          userInteraction(ProfileUserInteraction.NavigateToUserData(UserDataSection.Ratings))
        },
      )
    }
  }
}

@Composable
private fun ProfileSectionItem(
  section: ProfileSection,
  onClick: () -> Unit,
) {
  ElevatedCard(
    modifier = Modifier
      .padding(MaterialTheme.dimensions.keyline_16),
    onClick = onClick,
  ) {
    Row(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_16),
    ) {
      Icon(
        imageVector = section.icon,
        contentDescription = null,
        modifier = Modifier.padding(MaterialTheme.dimensions.keyline_8),
      )

      Text(
        text = section.title.getString(),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
          .padding(MaterialTheme.dimensions.keyline_8)
          .weight(1f),
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
