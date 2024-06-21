package com.divinelink.core.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class Previews
