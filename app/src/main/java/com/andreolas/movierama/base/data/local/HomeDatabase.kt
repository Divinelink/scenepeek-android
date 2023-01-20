package com.andreolas.movierama.base.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreolas.movierama.base.data.local.bean.BeanDAO
import com.andreolas.movierama.base.data.local.bean.PersistableBean

@Database(
    entities = [
        PersistableBean::class,
    ],
    version = HomeDatabase.LATEST_VERSION,
    exportSchema = true
)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun beanDAO(): BeanDAO

    companion object {
        const val DB_NAME = "App_Database"
        const val LATEST_VERSION = 0
    }
}
