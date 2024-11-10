package com.divinelink.scenepeek.base.initializers

import android.content.Context
import androidx.startup.Initializer
import com.divinelink.scenepeek.BuildConfig
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {

  override fun create(context: Context) {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
