package com.divinelink.core.commons.di

import com.divinelink.core.commons.util.JsonHelper
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val commonModule = module {
  single<Clock> { Clock.System }

  single {
    Json {
      prettyPrint = true
      isLenient = true
      coerceInputValues = true
      ignoreUnknownKeys = true
    }
  }

  singleOf(::JsonHelper)
}
