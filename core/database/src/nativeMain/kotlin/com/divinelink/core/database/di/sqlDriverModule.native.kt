package com.divinelink.core.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.divinelink.core.database.Database
import org.koin.dsl.module

actual val sqlDriverModule = module {
  single<SqlDriver> {
    NativeSqliteDriver(
      schema = Database.Schema,
      name = "database.db",
    )
  }
}
