package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.ui.AnimatedVisibilityScopeProvider
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.core.ui.coil.AvatarImage
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.core.ui.R as uiR

@Composable
fun JellyseerrAccountItem(
  accountDetails: JellyseerrAccountDetails?,
  transitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigateToJellyseerrLogin: () -> Unit,
) {
  with(transitionScope) {
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
            AvatarImage.Small(
              modifier = Modifier
                .sharedElement(
                  sharedContentState = rememberSharedContentState(
                    key = SharedElementKeys.JELLYSEERR_AVATAR,
                  ),
                  animatedVisibilityScope = animatedVisibilityScope,
                ),
              avatarUrl = details.avatar,
              username = details.displayName,
            )

            Column(
              verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
            ) {
              Text(
                text = stringResource(R.string.feature_settings_jellyseerr_integration),
                style = MaterialTheme.typography.bodyLarge,
              )
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Canvas(
                  modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(MaterialTheme.dimensions.keyline_4)
                    .size(MaterialTheme.dimensions.keyline_8),
                ) {
                  drawCircle(
                    color = Color.Green,
                    radius = 4.dp.toPx(),
                  )
                }

                Text(
                  text = stringResource(R.string.feature_settings_logged_in),
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.secondary,
                )

                Text(
                  modifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(
                      key = SharedElementKeys.JELLYSEERR_DISPLAY_NAME,
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                  ),
                  text = details.displayName,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.primary,
                )
              }
            }
          }
        }
      }
    }
  }
}

@Previews
@Composable
private fun AccountItemPreview() {
  AnimatedVisibilityScopeProvider { transitionScope, visibilityScope ->
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16)) {
      JellyseerrAccountItem(
        accountDetails = null,
        transitionScope = transitionScope,
        animatedVisibilityScope = visibilityScope,
        onNavigateToJellyseerrLogin = {},
      )

      JellyseerrAccountItem(
        accountDetails = JellyseerrAccountDetails(
          id = 1,
          displayName = "John Doe",
          avatar = "https://example.com/avatar.jpg",
          requestCount = 100,
          email = null,
          createdAt = "August 9th, 2021",
        ),
        transitionScope = transitionScope,
        animatedVisibilityScope = visibilityScope,
        onNavigateToJellyseerrLogin = {},
      )
    }
  }
}
