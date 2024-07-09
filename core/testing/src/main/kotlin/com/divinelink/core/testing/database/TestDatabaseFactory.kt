package com.divinelink.core.testing.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.divinelink.core.database.Database

object TestDatabaseFactory {
  fun createInMemoryDatabase(): Database {
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    Database.Schema.create(driver)
    return Database(driver)
  }
}
