package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.coil.AvatarImage
import com.divinelink.feature.settings.R

@Composable
fun AccountItem(
  accountDetails: AccountDetails?,
  onLoginClick: () -> Unit,
  onLogoutClick: () -> Unit,
) {
  AnimatedContent(
    targetState = accountDetails,
    label = "Account details animation",
  ) { details ->
    if (details != null) {
      LoggedInContent(details = details, onLogoutClick = onLogoutClick)
    } else {
      NotLoggedInContent(onLoginClick = onLoginClick)
    }
  }
}

@Composable
private fun LoggedInContent(
  details: AccountDetails,
  onLogoutClick: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(MaterialTheme.dimensions.keyline_16),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    AvatarImage.Small(
      avatarUrl = details.avatarUrl,
      username = details.username,
    )
    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      Text(
        text = details.username,
        style = MaterialTheme.typography.titleMedium,
      )
      Text(
        text = stringResource(R.string.feature_settings_tmdb_account),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    FilledTonalButton(
      modifier = Modifier.testTag(TestTags.Settings.Account.LOGOUT_BUTTON),
      onClick = onLogoutClick,
      colors = ButtonDefaults.filledTonalButtonColors(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
      ),
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Rounded.Logout,
        contentDescription = null,
        modifier = Modifier.size(MaterialTheme.dimensions.keyline_16),
      )
      Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_8))
      Text(stringResource(com.divinelink.core.ui.R.string.core_ui_logout))
    }
  }
}

@Composable
private fun NotLoggedInContent(onLoginClick: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(MaterialTheme.dimensions.keyline_16),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = Icons.Rounded.Person,
      contentDescription = null,
      modifier = Modifier
        .size(MaterialTheme.dimensions.keyline_48)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(MaterialTheme.dimensions.keyline_12),
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      Text(
        text = stringResource(R.string.feature_settings_not_logged_in),
        style = MaterialTheme.typography.titleMedium,
      )
      Text(
        text = stringResource(R.string.feature_settings_sign_in_to_access_features),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    FilledTonalButton(
      modifier = Modifier.testTag(TestTags.Settings.Account.LOGIN_BUTTON),
      onClick = onLoginClick,
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Rounded.Login,
        contentDescription = null,
        modifier = Modifier.size(MaterialTheme.dimensions.keyline_16),
      )
      Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_8))
      Text(stringResource(R.string.login))
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
            tmdbAvatarPath = null,
          ),
          onLoginClick = {},
          onLogoutClick = {},
        )
      }
    }
  }
}
