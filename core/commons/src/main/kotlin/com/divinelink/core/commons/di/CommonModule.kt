package com.divinelink.core.commons.di

import kotlinx.datetime.Clock
import org.koin.dsl.module

val commonModule = module {
  single<Clock> { Clock.System }
}
