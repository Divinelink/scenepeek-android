package com.divinelink.core.database.di

import android.app.Application
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.database.Database
import com.divinelink.core.database.credits.dao.CreditsDao
import com.divinelink.core.database.credits.dao.ProdCreditsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @ApplicationContext
  @Provides
  fun providesApplicationContext() = Application()

  @Provides
  @Singleton
  fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver =
    AndroidSqliteDriver(Database.Schema, context, "database.db")

  @Provides
  @Singleton
  fun provideDatabase(sqlDriver: SqlDriver): Database = Database(sqlDriver)

  @Provides
  @Singleton
  fun provideCreditsDao(
    database: Database,
    @IoDispatcher dispatcher: CoroutineDispatcher,
  ): CreditsDao = ProdCreditsDao(
    database = database,
    dispatcher = dispatcher,
  )

  @Singleton
  @Provides
  fun provideJellyseerrAccountQueries(database: Database) = database.jellyseerrAccountDetailsQueries
}
