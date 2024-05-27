package com.divinelink.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.divinelink.database.dao.MediaDao
import com.divinelink.database.model.PersistableMovie
import com.divinelink.database.model.PersistableTV

@Database(
  entities = [
    PersistableMovie::class,
    PersistableTV::class
  ],
  version = AppDatabase.LATEST_VERSION,
  exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun mediaDao(): MediaDao

  companion object {
    const val DB_NAME = "App_Database"
    const val LATEST_VERSION = 4
  }
}
