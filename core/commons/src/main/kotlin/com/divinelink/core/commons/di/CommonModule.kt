package com.divinelink.core.commons.di

import org.koin.dsl.module
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val commonModule = module {
  single<Clock> { Clock.System }
}
