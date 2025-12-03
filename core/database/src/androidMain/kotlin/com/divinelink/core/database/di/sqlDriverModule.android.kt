package com.divinelink.core.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.divinelink.core.database.Database
import org.koin.dsl.module

actual val sqlDriverModule = module {
  single<SqlDriver> {
    AndroidSqliteDriver(
      schema = Database.Schema,
      context = get(),
      name = "database.db",
    )
  }
}
