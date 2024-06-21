package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsTextItem

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
      Column {
        SettingsTextItem(
          title = stringResource(
            id = R.string.AccountSettingsScreen__logged_in_as,
            details.username,
          ),
          summary = null,
        )
        Button(
          modifier = Modifier
            .testTag(TestTags.Settings.Account.LOGOUT_BUTTON)
            .padding(start = MaterialTheme.dimensions.keyline_16),
          onClick = onLogoutClick,
        ) {
          Text(text = stringResource(id = R.string.AccountSettingsScreen__logout))
        }
      }
    } else {
      SettingsClickItem(
        modifier = Modifier.testTag(TestTags.Settings.Account.LOGIN_BUTTON),
        text = stringResource(id = R.string.AccountSettingsScreen__login),
        onClick = onLoginClick,
      )
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
