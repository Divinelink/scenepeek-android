package com.divinelink.core.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.divinelink.core.database.Database
import com.divinelink.core.database.credits.dao.CreditsDao
import com.divinelink.core.database.credits.dao.ProdCreditsDao
import com.divinelink.core.database.list.ListDao
import com.divinelink.core.database.list.ProdListDao
import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.media.dao.ProdMediaDao
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.database.person.ProdPersonDao
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {

  single<SqlDriver> {
    AndroidSqliteDriver(
      schema = Database.Schema,
      context = get(),
      name = "database.db",
    )
  }

  single<Database> { Database(get()) }

  singleOf(::ProdCreditsDao) { bind<CreditsDao>() }
  singleOf(::ProdPersonDao) { bind<PersonDao>() }
  singleOf(::ProdListDao) { bind<ListDao>() }
  singleOf(::ProdMediaDao) { bind<MediaDao>() }

  single { get<Database>().jellyseerrAccountDetailsQueries }
}
