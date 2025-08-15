package com.divinelink.scenepeek.feature.settings.account

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrLoginContentPreview
import com.divinelink.feature.settings.app.account.jellyseerr.preview.JellyseerrLoginStatePreviewParameterProvider

@Previews
@PreviewTest
@Composable
fun JellyseerrLoginContentScreenshots(
  @PreviewParameter(JellyseerrLoginStatePreviewParameterProvider::class) state:
  JellyseerrState.Login,
) {
  JellyseerrLoginContentPreview(state)
}
