package com.divinelink.core.ui

import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Preview(name = "light mode", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "dark mode", uiMode = UI_MODE_NIGHT_YES)
@Preview(
  name = "accessibility",
  heightDp = 640,
  widthDp = 360,
  device = "spec:width=360dp,height=640dp,dpi=480",
  fontScale = 2.0f,
)
annotation class Previews

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Preview(
  name = "phone",
  widthDp = 360,
  heightDp = 640,
  device = "spec:width=360dp,height=640dp,dpi=480",
)
@Preview(
  name = "landscape",
  widthDp = 640,
  heightDp = 360,
  device = "spec:width=640dp,height=360dp,dpi=480",
)
@Preview(
  name = "foldable",
  widthDp = 673,
  heightDp = 841,
  device = "spec:width=673dp,height=841dp,dpi=480",
)
@Preview(
  name = "tablet",
  widthDp = 1280,
  heightDp = 800,
  device = "spec:width=1280dp,height=800dp,dpi=480",
)
annotation class DevicePreviews
