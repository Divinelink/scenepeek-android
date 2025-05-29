package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.AnimatedVisibilityScopeProvider
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.coil.AvatarImage
import com.divinelink.core.ui.getString
import com.divinelink.feature.settings.R
import com.divinelink.core.ui.R as uiR

@Composable
fun JellyseerrLoggedInContent(
  modifier: Modifier = Modifier,
  transitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  jellyseerrState: JellyseerrState.LoggedIn,
  onLogoutClock: (JellyseerrInteraction.OnLogoutClick) -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    item {
      with(transitionScope) {
        Row {
          AvatarImage.Large(
            modifier = Modifier
              .testTag(TestTags.Settings.Jellyseerr.LOGGED_IN_AVATAR)
              .sharedElement(
                sharedContentState = rememberSharedContentState(
                  key = SharedElementKeys.JELLYSEERR_AVATAR,
                ),
                animatedVisibilityScope = animatedVisibilityScope,
              ),
            avatarUrl = jellyseerrState.accountDetails.avatar,
            username = jellyseerrState.accountDetails.displayName,
          )

          Column {
            Text(
              modifier = Modifier
                .sharedBounds(
                  sharedContentState = rememberSharedContentState(
                    key = SharedElementKeys.JELLYSEERR_DISPLAY_NAME,
                  ),
                  animatedVisibilityScope = animatedVisibilityScope,
                )
                .padding(
                  start = MaterialTheme.dimensions.keyline_16,
                  top = MaterialTheme.dimensions.keyline_24,
                ),
              text = jellyseerrState.accountDetails.displayName,
              color = MaterialTheme.colorScheme.primary,
              style = MaterialTheme.typography.titleMedium,
            )

            jellyseerrState.accountDetails.email?.let { email ->
              Text(
                modifier = Modifier
                  .padding(
                    start = MaterialTheme.dimensions.keyline_16,
                    top = MaterialTheme.dimensions.keyline_8,
                  ),
                text = email,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
              )
            }

            Text(
              modifier = Modifier
                .padding(
                  start = MaterialTheme.dimensions.keyline_16,
                  top = MaterialTheme.dimensions.keyline_8,
                ),
              text = UIText.ResourceText(
                R.string.feature_settings_jellyseerr_joined_on,
                jellyseerrState.accountDetails.formattedCreatedAt,
                jellyseerrState.accountDetails.id,
              ).getString(),
              color = MaterialTheme.colorScheme.onSurface,
              fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
              style = MaterialTheme.typography.bodySmall,
            )
          }
        }
      }
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(
          modifier = Modifier.padding(
            top = MaterialTheme.dimensions.keyline_16,
            start = MaterialTheme.dimensions.keyline_16,
          ),
          text = stringResource(R.string.feature_settings_jellyseerr_total_requests),
          style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_8))
        Text(
          modifier = Modifier.padding(
            start = MaterialTheme.dimensions.keyline_16,
            bottom = MaterialTheme.dimensions.keyline_16,
          ),
          text = jellyseerrState.accountDetails.requestCount.toString(),
          style = MaterialTheme.typography.headlineSmall,
        )
      }
    }

    item {
      AnimatedContent(
        targetState = jellyseerrState.isLoading,
        label = "Logout Button Animated Content",
      ) { loading ->
        when (loading) {
          true -> Row(
            modifier = Modifier
              .testTag(TestTags.LOADING_PROGRESS)
              .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
          ) {
            CircularProgressIndicator()
          }
          false -> Button(
            modifier = Modifier
              .testTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON)
              .fillMaxWidth(),
            onClick = { onLogoutClock(JellyseerrInteraction.OnLogoutClick) },
          ) {
            Text(stringResource(id = uiR.string.core_ui_logout))
          }
        }
      }
    }
  }
}

@Previews
@Composable
private fun JellyseerrLoggedInContentPreview() {
  AnimatedVisibilityScopeProvider { transitionScope, visibilityScope ->
    JellyseerrLoggedInContent(
      transitionScope = transitionScope,
      animatedVisibilityScope = visibilityScope,
      jellyseerrState = JellyseerrState.LoggedIn(
        isLoading = false,
        accountDetails = JellyseerrAccountDetails(
          id = 1,
          avatar = "https://www.example.com/avatar.jpg",
          displayName = "Display Name",
          requestCount = 100,
          email = "jellyseerr@info.com",
          createdAt = "2023-08-19T00:00:00Z",
        ),
      ),
      onLogoutClock = {},
    )
  }
}

@Previews
@Composable
private fun JellyseerrLoggedInContentLoadingPreview() {
  AnimatedVisibilityScopeProvider { transitionScope, visibilityScope ->
    JellyseerrLoggedInContent(
      transitionScope = transitionScope,
      animatedVisibilityScope = visibilityScope,
      jellyseerrState = JellyseerrState.LoggedIn(
        isLoading = true,
        accountDetails = JellyseerrAccountDetails(
          id = 1,
          avatar = "https://www.example.com/avatar.jpg",
          displayName = "Display Name",
          requestCount = 100,
          email = "jellyseerr@info.com",
          createdAt = "2023-08-19T00:00:00Z",
        ),
      ),
      onLogoutClock = {},
    )
  }
}
