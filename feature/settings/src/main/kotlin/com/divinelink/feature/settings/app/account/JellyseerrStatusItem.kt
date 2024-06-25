package com.divinelink.feature.settings.app.account

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.jellyseerr.JellyseerrDetails
import com.divinelink.core.ui.Previews

@Composable
fun JellyseerrStatusItem(jellyseerrDetails: JellyseerrDetails?) {

}

@Previews
@Composable
private fun JellyseerrStatusItemPreview() {
  AppTheme {
    Surface {
      JellyseerrStatusItem(
        jellyseerrDetails = JellyseerrDetails(
          address = "address",
          apiKey = "apiKey",
        ),
      )
    }
  }
}
