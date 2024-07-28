package com.divinelink.core.database.person

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.database.Database
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProdPersonDao @Inject constructor(
  private val database: Database,
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : PersonDao {

  override fun fetchPersonById(id: Long): Flow<PersonEntity?> = database
    .personEntityQueries
    .fetchPersonById(id)
    .asFlow()
    .mapToOneOrNull(context = dispatcher)

  override fun insertPerson(person: PersonEntity) = database
    .personEntityQueries
    .insertPerson(person)
}
