package com.andreolas.movierama.base.data.local.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bean")
data class PersistableBean(
    @PrimaryKey
    val id: String,
    val name: String,
    val roasterName: String,
    val origin: String,
    val roastDate: String?,
    val roastLevel: String,
    val process: String,
    val rating: Int,
    val tastingNotes: String,
    val additionalNotes: String,
)
