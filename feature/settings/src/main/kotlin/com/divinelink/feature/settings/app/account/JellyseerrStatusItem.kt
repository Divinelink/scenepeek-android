package com.divinelink.feature.settings.app.account

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.Previews

@Composable
fun JellyseerrStatusItem(jellyseerrState: JellyseerrState?) {
}

@Previews
@Composable
private fun JellyseerrStatusItemPreview() {
  AppTheme {
    Surface {
      JellyseerrStatusItem(
        jellyseerrState = JellyseerrState.Initial(false, null),
      )
    }
  }
}
