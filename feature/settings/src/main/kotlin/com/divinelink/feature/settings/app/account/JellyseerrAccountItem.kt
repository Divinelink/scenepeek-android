package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.ui.CoilImage
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.core.ui.R as uiR

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.JellyseerrAccountItem(
  accountDetails: JellyseerrAccountDetails?,
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigateToJellyseerrLogin: () -> Unit,
) {
  AnimatedContent(
    targetState = accountDetails,
    label = "Account details animation",
  ) { details ->
    Column(
      Modifier.clickable { onNavigateToJellyseerrLogin() },
    ) {
      if (details == null) {
        SettingsClickItem(
          icon = IconWrapper.Image(uiR.drawable.core_ui_ic_jellyseerr),
          text = stringResource(R.string.feature_settings_jellyseerr_integration),
          onClick = onNavigateToJellyseerrLogin,
        )
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
          modifier = Modifier
            .padding(MaterialTheme.dimensions.keyline_16)
            .fillMaxWidth(),
        ) {
          CoilImage(
            modifier = Modifier
              .sharedElement(
                state = rememberSharedContentState(key = SharedElementKeys.JELLYSEERR_AVATAR),
                animatedVisibilityScope = animatedVisibilityScope,
              )
              .size(MaterialTheme.dimensions.keyline_36),
            url = details.avatar,
          )

          Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          ) {
            Text(
              text = stringResource(R.string.feature_settings_jellyseerr_integration),
              style = MaterialTheme.typography.bodyLarge,
            )
            Text(
              modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(
                  key = SharedElementKeys.JELLYSEERR_DISPLAY_NAME,
                ),
                animatedVisibilityScope = animatedVisibilityScope,
              ),
              text = stringResource(
                id = R.string.AccountSettingsScreen__logged_in_as,
                details.displayName,
              ),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.secondary,
            )
          }
        }
      }
    }
  }
}

@Previews
@Composable
private fun AccountItemPreview() {
  AppTheme {
    Surface {
      Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16)) {
        AccountItem(
          accountDetails = null,
          onLoginClick = {},
          onLogoutClick = {},
        )

        AccountItem(
          accountDetails = AccountDetails(
            id = 123,
            username = "Jessee Pinkman",
            name = "name",
          ),
          onLoginClick = {},
          onLogoutClick = {},
        )
      }
    }
  }
}
