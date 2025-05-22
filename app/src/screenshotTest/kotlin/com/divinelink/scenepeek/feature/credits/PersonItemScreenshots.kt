package com.divinelink.scenepeek.feature.credits

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.credit.PersonItemPreview

@Previews
@Preview(name = "api 30", uiMode = Configuration.UI_MODE_NIGHT_NO, apiLevel = 30)
@Preview(name = "api 30 dark", uiMode = Configuration.UI_MODE_NIGHT_YES, apiLevel = 30)
@Composable
fun PersonItemScreenshots() {
  PersonItemPreview()
}
