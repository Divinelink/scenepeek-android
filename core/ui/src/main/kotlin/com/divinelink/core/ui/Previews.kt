package com.divinelink.core.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Preview(name = "light mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "accessibility", device = "spec:width=360dp,height=640dp,dpi=480", fontScale = 2.0f)
@Preview(name = "api 30", uiMode = Configuration.UI_MODE_NIGHT_NO, apiLevel = 30)
annotation class Previews

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Preview(name = "phone", device = "spec:width=360dp,height=640dp,dpi=480")
@Preview(name = "landscape", device = "spec:width=640dp,height=360dp,dpi=480")
@Preview(name = "foldable", device = "spec:width=673dp,height=841dp,dpi=480")
@Preview(name = "tablet", device = "spec:width=1280dp,height=800dp,dpi=480")
annotation class DevicePreviews
