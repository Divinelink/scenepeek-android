package com.divinelink.core.testing

import android.app.Application
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Ignore("This is a base class for all Compose tests")
@RunWith(RobolectricTestRunner::class)
@Config(
  application = Application::class,
  sdk = [Build.VERSION_CODES.P],
  instrumentedPackages = ["androidx.loader.content"],
)
open class ComposeTest : UnitTest() {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()
}

fun ComposeTest.getString(resId: Int): String = composeTestRule.activity.getString(resId)

fun ComposeTest.getString(
  resId: Int,
  vararg formatArgs: Any,
): String = composeTestRule.activity.getString(resId, *formatArgs)

fun ComposeTest.hasClickLabel(label: Int) =
  SemanticsMatcher("Clickable action with label: $label") {
    it.config.getOrNull(
      SemanticsActions.OnClick,
    )?.label == getString(label)
  }
