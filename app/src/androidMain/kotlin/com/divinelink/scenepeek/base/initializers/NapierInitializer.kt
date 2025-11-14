package com.divinelink.scenepeek.base.initializers

import android.content.Context
import androidx.startup.Initializer
import com.divinelink.scenepeek.BuildConfig
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class NapierInitializer : Initializer<Unit> {

  override fun create(context: Context) {
    if (BuildConfig.DEBUG) {
      Napier.base(DebugAntilog())
    }
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
