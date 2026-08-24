package com.divinelink.core.testing

import android.os.Build
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
// Temporarily add specific version to fix robolectric to running on compileSdk 37
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
open class RoboTest : UnitTest()
