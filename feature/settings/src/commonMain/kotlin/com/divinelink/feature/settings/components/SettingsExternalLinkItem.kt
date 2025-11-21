package com.divinelink.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.manager.url.rememberUrlHandler
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.feature_settings_about__privacy_policy
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsExternalLinkItem(
  modifier: Modifier = Modifier,
  onNavigate: (Navigation) -> Unit,
  text: String,
  url: String,
) {
  val urlHandler = rememberUrlHandler()
  val privacyPolicyTitle = stringResource(Res.string.feature_settings_about__privacy_policy)

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    modifier = modifier
      .clickable {
        urlHandler.openUrl(
          url = url,
          onError = {
            onNavigate(
              Navigation.WebViewRoute(
                url = url,
                title = privacyPolicyTitle,
              ),
            )
          },
        )
      }
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.bodyLarge,
    )

    Spacer(modifier = Modifier.weight(1f))

    Icon(
      modifier = Modifier.padding(end = MaterialTheme.dimensions.keyline_8),
      imageVector = Icons.AutoMirrored.Filled.OpenInNew,
      contentDescription = null,
    )
  }
}

@Composable
@Previews
private fun SettingsScreenPreview() {
  AppTheme {
    Surface {
      SettingsExternalLinkItem(
        onNavigate = {},
        text = "Privacy Policy",
        url = "https://www.scenepeek.com/privacy-policy",
      )
    }
  }
}
