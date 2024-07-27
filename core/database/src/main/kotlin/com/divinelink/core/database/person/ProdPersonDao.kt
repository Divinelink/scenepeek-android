package com.divinelink.core.database.person

import com.divinelink.core.database.Database
import javax.inject.Inject

class ProdPersonDao @Inject constructor(private val database: Database) : PersonDao {

  override fun fetchPersonById(id: Long): PersonEntity? = database
    .personEntityQueries
    .fetchPersonById(id)
    .executeAsOneOrNull()

  override fun insertPerson(person: PersonEntity) = database
    .personEntityQueries
    .insertPerson(person)
}
