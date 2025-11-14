package com.divinelink.core.commons.di

import com.divinelink.core.commons.domain.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

val dispatcherModule = module {

  single<DispatcherProvider> {
    object : DispatcherProvider {
      override val main: CoroutineDispatcher = Dispatchers.Main
      override val default: CoroutineDispatcher = Dispatchers.Default
      override val io: CoroutineDispatcher = Dispatchers.IO
      override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
    }
  }
}
