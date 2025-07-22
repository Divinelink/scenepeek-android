package com.divinelink.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.media.model.PersistableMovie
import com.divinelink.core.database.media.model.PersistableTV

@Database(
  entities = [
    PersistableMovie::class,
    PersistableTV::class,
  ],
  version = AppDatabase.LATEST_VERSION,
  exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun mediaDao(): MediaDao

  companion object {
    const val DB_NAME = "App_Database"
    const val LATEST_VERSION = 6
  }
}
