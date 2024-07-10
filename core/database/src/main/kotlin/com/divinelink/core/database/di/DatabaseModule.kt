package com.divinelink.core.database.di

import android.app.Application
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.divinelink.core.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

  @Singleton
  @Provides
  fun provideJellyseerrAccountQueries(database: Database) = database.jellyseerrAccountDetailsQueries
}
