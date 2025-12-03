package com.divinelink.core.testing

import android.app.Application
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.BeforeTest

@RunWith(RobolectricTestRunner::class)
@Config(
  application = Application::class,
  sdk = [Build.VERSION_CODES.R],
  instrumentedPackages = ["androidx.loader.content"],
)
actual open class ComposeTest : KoinTest {

  @BeforeTest
  open fun setUp() {
    // No-op
  }
}

@Composable
actual fun InitPreviewContextConfiguration() {
  CompositionLocalProvider(LocalInspectionMode provides true) {
    PreviewContextConfigurationEffect()
  }
}
