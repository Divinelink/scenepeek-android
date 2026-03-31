package com.divinelink.scenepeek.initializer

import android.content.Context
import androidx.startup.Initializer
import com.divinelink.core.android.BuildConfig
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid
import kotlinx.coroutines.flow.MutableStateFlow

class SentryInitializer : Initializer<Unit> {

  override fun create(context: Context) {
    val isCrashReportsEnabled = MutableStateFlow(true)

    SentryAndroid.init(context.applicationContext) { options: SentryOptions ->
      options.dsn = BuildConfig.SENTRY_DSN
      options.setBeforeSend { event, _ ->
        if (isCrashReportsEnabled.value) event else null
      }
    }
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
