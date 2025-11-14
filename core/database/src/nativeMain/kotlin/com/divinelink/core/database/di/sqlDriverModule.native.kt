package com.divinelink.core.database.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.divinelink.core.database.Database
import org.koin.dsl.module

actual val sqlDriverModule = module {
  single {
    NativeSqliteDriver(
      schema = Database.Schema,
      name = "database.db",
    )
  }
}
