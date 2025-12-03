package com.divinelink.core.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val ktorEngineModule = module {
  single<HttpClientEngine> { Darwin.create() }
}
