package com.divinelink.feature.details.media.ui.forms.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.provider.StreamingProvider
import com.divinelink.core.ui.coil.SmallLogoImage
import com.divinelink.core.ui.manager.url.rememberUrlHandler

@Composable
fun StreamingProviderItem(
  provider: StreamingProvider,
  subtitle: String,
  link: String,
) {
  val urlHandler = rememberUrlHandler()

  Card(
    onClick = { urlHandler.openUrl(link) },
  ) {
    Row(
      modifier = Modifier.padding(
        horizontal = MaterialTheme.dimensions.keyline_12,
        vertical = MaterialTheme.dimensions.keyline_12,
      ),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      SmallLogoImage(
        path = provider.logoPath,
        onClick = { urlHandler.openUrl(link) },
      )

      Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_2)) {
        Text(
          text = provider.providerName,
          style = MaterialTheme.typography.titleSmall,
        )

        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.secondary,
        )
      }
    }
  }
}
