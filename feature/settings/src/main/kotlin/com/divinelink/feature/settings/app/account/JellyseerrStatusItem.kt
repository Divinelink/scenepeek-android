package com.divinelink.feature.settings.app.account

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.jellyseerr.JellyseerrIntegration
import com.divinelink.core.ui.Previews

@Composable
fun JellyseerrStatusItem(jellyseerrIntegration: JellyseerrIntegration?) {
}

@Previews
@Composable
private fun JellyseerrStatusItemPreview() {
  AppTheme {
    Surface {
      JellyseerrStatusItem(
        jellyseerrIntegration = JellyseerrIntegration(
          address = "address",
          apiKey = "apiKey",
        ),
      )
    }
  }
}
