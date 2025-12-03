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
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.coil.AvatarImage
import com.divinelink.core.ui.resources.core_ui_ic_jellyseerr
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_jellyseerr_integration
import com.divinelink.feature.settings.resources.feature_settings_logged_in
import org.jetbrains.compose.resources.stringResource

@Composable
fun JellyseerrAccountItem(
  accountDetails: JellyseerrProfile?,
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
            icon = IconWrapper.Image(UiDrawable.core_ui_ic_jellyseerr),
            text = stringResource(Res.string.feature_settings_jellyseerr_integration),
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
                text = stringResource(Res.string.feature_settings_jellyseerr_integration),
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
                  text = stringResource(Res.string.feature_settings_logged_in),
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
  SharedTransitionScopeProvider {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16)) {
      JellyseerrAccountItem(
        accountDetails = null,
        transitionScope = it,
        animatedVisibilityScope = this@SharedTransitionScopeProvider,
        onNavigateToJellyseerrLogin = {},
      )

      JellyseerrAccountItem(
        accountDetails = JellyseerrProfileFactory.jellyfin(),
        transitionScope = it,
        animatedVisibilityScope = this@SharedTransitionScopeProvider,
        onNavigateToJellyseerrLogin = {},
      )
    }
  }
}
