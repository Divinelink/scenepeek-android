package com.andreolas.movierama.base.data.local.bean

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BeanDAO {

    @Query("SELECT * FROM bean")
    fun fetchAllBeans(): Flow<List<PersistableBean>>

    @Query("SELECT * FROM bean WHERE id = :id")
    fun fetchBeanById(
        id: String,
    ): PersistableBean

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
    )
    suspend fun insertBean(bean: PersistableBean)

    @Update
    suspend fun updateBean(bean: PersistableBean)

    @Query("DELETE FROM bean WHERE id=:id")
    fun removeBean(id: String)
}
