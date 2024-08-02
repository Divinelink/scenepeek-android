package com.divinelink.core.database.person

import kotlinx.coroutines.flow.Flow

interface PersonDao {

  fun fetchPersonById(id: Long): Flow<PersonEntity?>

  fun insertPerson(person: PersonEntity)
}
