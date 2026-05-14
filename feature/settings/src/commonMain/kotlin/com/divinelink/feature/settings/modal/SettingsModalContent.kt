package com.divinelink.feature.settings.modal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.composition.LocalIntentManager
import com.divinelink.core.ui.manager.url.UrlHandler
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsModalContent(
  options: List<SettingsModalOption>,
  urlHandler: UrlHandler,
) {
  val intentManager = LocalIntentManager.current

  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    options.forEach { option ->
      val url = stringResource(option.url)

      Row(
        modifier = Modifier
          .height(MaterialTheme.dimensions.keyline_56)
          .fillMaxWidth()
          .clickable {
            when (option) {
              SettingsModalOption.GitHubStar -> urlHandler.openUrl(url)
              SettingsModalOption.OpenGithubIssue -> urlHandler.openUrl(url)
              SettingsModalOption.RateGooglePlay -> urlHandler.openUrl(url)
              SettingsModalOption.SendEmail -> intentManager.launchEmail(
                email = url,
                subject = null,
                body = null,
              )
            }
          },
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_16),
          imageVector = option.icon,
          contentDescription = null,
        )

        Text(
          text = stringResource(option.text),
          style = MaterialTheme.typography.bodyLarge,
        )
      }
    }

    Spacer(
      modifier = Modifier.height(MaterialTheme.dimensions.keyline_8),
    )
  }
}
