package com.andreolas

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.andreolas.movierama.Application
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Ignore

@Ignore("This is a base class for all Compose tests")
@RunWith(RobolectricTestRunner::class)
@Config(
  application = Application::class,
  sdk = [Build.VERSION_CODES.Q],
  instrumentedPackages = ["androidx.loader.content"]
)
open class ComposeTest : UnitTest() {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()
}
