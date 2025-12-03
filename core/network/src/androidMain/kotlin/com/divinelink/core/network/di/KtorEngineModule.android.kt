package com.divinelink.core.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.koin.dsl.module

actual val ktorEngineModule = module {
  single<HttpClientEngine> { Android.create() }
}
