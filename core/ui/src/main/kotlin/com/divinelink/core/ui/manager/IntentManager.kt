package com.divinelink.core.ui.manager

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Immutable
import com.divinelink.core.commons.BuildConfigProvider

@Immutable
interface IntentManager {
  val packageName: String

  fun startActivity(intent: Intent): Boolean

  fun shareText(text: String)

  fun shareErrorReport(throwable: Throwable)
}

fun createIntentManager(
  context: Context,
  buildConfigProvider: BuildConfigProvider,
): IntentManager = AndroidIntentManager(
  context = context,
  buildConfigProvider = buildConfigProvider,
)
