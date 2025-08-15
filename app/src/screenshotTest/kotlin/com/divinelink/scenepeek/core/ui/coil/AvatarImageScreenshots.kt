package com.divinelink.scenepeek.core.ui.coil

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.coil.AvatarImagePreview
import com.divinelink.core.ui.coil.AvatarImageWithInitialsPreview

@Composable
@PreviewTest
@Previews
fun AvatarImageWithUrlScreenshots() {
  AvatarImagePreview()
}

@Composable
@PreviewTest
@Previews
fun AvatarImageWithInitialsScreenshots() {
  AvatarImageWithInitialsPreview()
}
