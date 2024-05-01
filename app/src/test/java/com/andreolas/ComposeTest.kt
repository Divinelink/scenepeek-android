package com.andreolas

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
open class ComposeTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()
}
