package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.AnimatedVisibilityScopeProvider
import com.divinelink.core.ui.CoilImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.core.ui.R as uiR

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.JellyseerrLoggedInContent(
  modifier: Modifier = Modifier,
  animatedVisibilityScope: AnimatedVisibilityScope,
  jellyseerrState: JellyseerrState.LoggedIn,
  onLogoutClock: (JellyseerrInteraction.OnLogoutClick) -> Unit,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Row {
      CoilImage(
        modifier = Modifier
          .testTag(TestTags.Settings.Jellyseerr.LOGGED_IN_AVATAR)
          .sharedElement(
            state = rememberSharedContentState(key = SharedElementKeys.JELLYSEERR_AVATAR),
            animatedVisibilityScope = animatedVisibilityScope,
          )
          .size(MaterialTheme.dimensions.keyline_96),
        url = jellyseerrState.accountDetails.avatar,
      )

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
    }

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

    Spacer(Modifier.weight(1f))

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

@OptIn(ExperimentalSharedTransitionApi::class)
@Previews
@Composable
private fun JellyseerrBottomSheetContentPreview() {
  AnimatedVisibilityScopeProvider {
    JellyseerrLoggedInContent(
      animatedVisibilityScope = it,
      jellyseerrState = JellyseerrState.LoggedIn(
        isLoading = false,
        accountDetails = JellyseerrAccountDetails(
          id = 1,
          avatar = "https://www.example.com/avatar.jpg",
          displayName = "Display Name",
          requestCount = 100,
        ),
      ),
      onLogoutClock = {},
    )
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Previews
@Composable
private fun JellyseerrBottomSheetContentLoadingPreview() {
  AnimatedVisibilityScopeProvider {
    JellyseerrLoggedInContent(
      animatedVisibilityScope = it,
      jellyseerrState = JellyseerrState.LoggedIn(
        isLoading = true,
        accountDetails = JellyseerrAccountDetails(
          id = 1,
          avatar = "https://www.example.com/avatar.jpg",
          displayName = "Display Name",
          requestCount = 100,
        ),
      ),
      onLogoutClock = {},
    )
  }
}